package com.barocert.passcert;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.barocert.BarocertException;
import com.barocert.crypto.Encryptor;
import com.barocert.passcert.cms.CMS;
import com.barocert.passcert.cms.CMSReceipt;
import com.barocert.passcert.cms.CMSResult;
import com.barocert.passcert.cms.CMSStatus;
import com.barocert.passcert.cms.CMSVerify;
import com.barocert.passcert.identity.Identity;
import com.barocert.passcert.identity.IdentityReceipt;
import com.barocert.passcert.identity.IdentityResult;
import com.barocert.passcert.identity.IdentityStatus;
import com.barocert.passcert.identity.IdentityVerify;
import com.barocert.passcert.sign.Sign;
import com.barocert.passcert.sign.SignReceipt;
import com.barocert.passcert.sign.SignResult;
import com.barocert.passcert.sign.SignStatus;
import com.barocert.passcert.sign.SignVerify;
import com.google.gson.Gson;

import kr.co.linkhub.auth.Base64;
import kr.co.linkhub.auth.LinkhubException;
import kr.co.linkhub.auth.Token;
import kr.co.linkhub.auth.TokenBuilder;

public class PasscertServiceImp implements PasscertService {

    private static final String SERVICE_ID = "BAROCERT";

    private static final String AUTH_STATIC_URL = "https://static-auth.linkhub.co.kr";
    private static final String SERVICEURL_STATIC = "https://static-barocert.linkhub.co.kr";
    private static final String SERVICEURL = "https://barocert.linkhub.co.kr";
    
    private static final String APIVERSION = "2.1"; // sha256
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private static final TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");
    private String proxyIP;
    private Integer proxyPort;

    private boolean isIPRestrictOnOff = true; // 인증통큰 발급 IP 제한 - 기본값(true)
    private boolean useStaticIP = false;    // API 서비스고정 IP - 기본값(false)
    private boolean useLocalTimeYN = true;  // 로컬시스템 시간 사용여부 - 기본값(true)

    private String _linkID;
    private String _SECRETKEY;
    private TokenBuilder tokenBuilder;
    
    private Encryptor encryptor;

    private Gson _gsonParser = new Gson();
    private static final Map<String, Token> tokenTable = new HashMap<String, Token>();

    public String getServiceURL() {
        if (useStaticIP)
            return SERVICEURL_STATIC;
        else
            return SERVICEURL;
    }

    private TokenBuilder getTokenbuilder() {
        if (this.tokenBuilder == null) { // token 이 없다면.
            tokenBuilder = TokenBuilder.newInstance(getLinkID(), getSecretKey()) // LinkID, SecretKey
                    .useLocalTimeYN(useLocalTimeYN)       // 로컬시스템 시간 사용여부.
                    .ServiceID(SERVICE_ID)                // 서비스아이디.
                    .addScope("partner")            // partner
                    .addScope("441")                // 전자서명
                    .addScope("442")                // 본인인증
                    .addScope("443")                // 출금동의
                    .addScope("444");               // 간편로그인

            // AuthURL 이 null 이고, useStaticIP 가 true 이면, ServiceURL 이 Auth_Static_URL 적용.
            if (useStaticIP)
                tokenBuilder.setServiceURL(AUTH_STATIC_URL);

            if (proxyIP != null && proxyPort != null) { 
                tokenBuilder.setProxyIP(proxyIP);
                tokenBuilder.setProxyPort(proxyPort);
            }
        }

        return tokenBuilder;
    }

    private String getSessionToken() throws BarocertException {
        Token token = null;
        Date UTCTime = null;

        if (tokenTable.containsKey(_linkID)) token = tokenTable.get(_linkID);

        boolean expired = true;

        if (token != null) { // 토큰이 있다면, 토큰 유효성체크.
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            format.setTimeZone(TIMEZONE);

            SimpleDateFormat subFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            subFormat.setTimeZone(TIMEZONE);

            try { // 토큰기간 검증.
                Date expiration = format.parse(token.getExpiration()); // 토큰 만료기간.
                UTCTime = subFormat.parse(getTokenbuilder().getTime()); // APIServerTime.
                expired = expiration.before(UTCTime); // 토큰 만료기간과 APIServerTime 과의 비교.
            } catch (LinkhubException le) {
                throw new BarocertException(le);
            } catch (Exception e) {
                throw new BarocertException(-99999999, "Passcert Parse Exception", e);
            }
        }

        if (expired) {
            if (tokenTable.containsKey(_linkID))
                tokenTable.remove(_linkID);

            try {
                if (isIPRestrictOnOff) { // 인증토큰 발급 IP 제한. 기본값(true)
                    token = getTokenbuilder().build(null, null);
                } else {
                    token = getTokenbuilder().build(null, "*");
                }
                tokenTable.put(_linkID, token);
            } catch (LinkhubException le) {
                throw new BarocertException(le);
            }
        }

        return token.getSession_token();
    }

    /**
     * Convert Object to Json String.
     * 
     * @param Graph
     * @return jsonString
     */
    protected String toJsonString(Object Graph) {
        return _gsonParser.toJson(Graph);
    }

    /**
     * Convert JsonString to Object of Clazz
     * 
     * @param json
     * @param clazz
     * @return Object of Clazz
     */
    protected <T> T fromJsonString(String json, Class<T> clazz) {
        return _gsonParser.fromJson(json, clazz);
    }

    /**
     * 
     * @param <T>
     * @param url
     * @param clientCode
     * @param clazz
     * @return
     * @throws BarocertException
     */
    protected <T> T httpGet(String url, Class<T> clazz) throws BarocertException {
        HttpURLConnection httpURLConnection;
        try {
            URL uri = new URL(getServiceURL() + url);

            if (proxyIP != null && proxyPort != null) { // 프록시 정보가 있다면,
                // 프록시 객체를 통한 연결설정.
                Proxy prx = new Proxy(Type.HTTP, new InetSocketAddress(proxyIP, proxyPort)); // 프록시서버 및 포트 설정
                httpURLConnection = (HttpURLConnection) uri.openConnection(prx); // URL Connection
            } else {
                httpURLConnection = (HttpURLConnection) uri.openConnection();
            }
        } catch (Exception e) {
            throw new BarocertException(-99999999, "Passcert API 서버 접속 실패", e);
        }

        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken());
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        httpURLConnection.setRequestProperty("x-bc-version".toLowerCase(), APIVERSION);

        String Result = parseResponse(httpURLConnection);

        return fromJsonString(Result, clazz);
    }

    /**
     * 
     * @param <T>
     * @param url
     * @param clientCode
     * @param PostData
     * @param clazz
     * @return
     * @throws BarocertException
     */
    protected <T> T httpPost(String url, String PostData, Class<T> clazz) throws BarocertException {
        setupEncryptor();
        HttpURLConnection httpURLConnection;
        try {
            URL uri = new URL(getServiceURL() + url);

            // 프록시 설정
            if (proxyIP != null && proxyPort != null) {
                Proxy prx = new Proxy(Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
                httpURLConnection = (HttpURLConnection) uri.openConnection(prx);
            } else {
                httpURLConnection = (HttpURLConnection) uri.openConnection();
            }
        } catch (Exception e) {
            throw new BarocertException(-99999999, "Passcert API 서버 접속 실패", e);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = format.format(new Date());

        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken());
        httpURLConnection.setRequestProperty("x-bc-date", date);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");

        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e1) {
            throw new BarocertException(-99999999, "Passcert Protocol Exception", e1);
        }

        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoOutput(true);

        if ((PostData == null || PostData.isEmpty()) == false) {

            byte[] btPostData = PostData.getBytes(Charset.forName("UTF-8"));

            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(btPostData.length));

            String signTarget = "POST\n";
            signTarget += sha256Base64(btPostData) + "\n";
            signTarget += date + "\n";
            signTarget += url + "\n";

            String Signature = base64Encode(HMacSha256(base64Decode(getSecretKey()), signTarget.getBytes(Charset.forName("UTF-8"))));

            httpURLConnection.setRequestProperty("x-bc-version".toLowerCase(), APIVERSION);
            httpURLConnection.setRequestProperty("x-bc-auth", Signature);
            httpURLConnection.setRequestProperty("x-bc-encryptionmode", encryptor.getMode());

            DataOutputStream output = null;

            try {
                output = new DataOutputStream(httpURLConnection.getOutputStream());
                output.write(btPostData);
                output.flush();
            } catch (Exception e) {
                throw new BarocertException(-99999999, "Passcert Fail to POST data to Server.", e);
            } finally {
                try {
                    if (output != null)
                        output.close();
                } catch (IOException e1) {
                    throw new BarocertException(-99999999, "Passcert httppost func DataOutputStream close() Exception",
                            e1);
                }
            }
        }

        String Result = parseResponse(httpURLConnection);

        return fromJsonString(Result, clazz);
    }

    private static String sha256Base64(byte[] input) throws BarocertException {
        MessageDigest md;
        byte[] btResult = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            btResult = md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new BarocertException(-99999999, "Passcert sha256 base64 Exception", e);
        }

        return Base64.encode(btResult);
    }

    private static byte[] base64Decode(String input) {
        return Base64.decode(input);
    }

    private static String base64Encode(byte[] input) {
        return Base64.encode(input);
    }

    // SHA256 HMAC 암호화
    private static byte[] HMacSha256(byte[] key, byte[] input) throws BarocertException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA256_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            return mac.doFinal(input);
        } catch (Exception e) {
            throw new BarocertException(-99999999,"Passcert Fail to Calculate HMAC-SHA256, Please check your SecretKey.", e);
        }
    }

    private class ErrorResponse {
        private long code;
        private String message;

        public long getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    private static String fromStream(InputStream input) throws BarocertException {
        InputStreamReader is = null;
        BufferedReader br = null;
        StringBuilder sb = null;

        try {
            is = new InputStreamReader(input, Charset.forName("UTF-8"));
            br = new BufferedReader(is);
            sb = new StringBuilder();

            String read = br.readLine();

            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
        } catch (IOException e) {
            throw new BarocertException(-99999999, "Passcert fromStream func Exception", e);
        } finally {
            try {
                if (br != null)
                    br.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                throw new BarocertException(-99999999, "Passcert fromStream func finally close Exception", e);
            }
        }

        return sb.toString();
    }

    private static String fromGzipStream(InputStream input) throws BarocertException {
        GZIPInputStream zipReader = null;
        InputStreamReader is = null;
        BufferedReader br = null;
        StringBuilder sb = null;

        try {
            zipReader = new GZIPInputStream(input);
            is = new InputStreamReader(zipReader, "UTF-8");
            br = new BufferedReader(is);
            sb = new StringBuilder();

            String read = br.readLine();

            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
        } catch (IOException e) {
            throw new BarocertException(-99999999, "Passcert fromGzipStream func Exception", e);
        } finally {
            try {
                if (br != null)
                    br.close();
                if (is != null)
                    is.close();
                if (zipReader != null)
                    zipReader.close();
            } catch (IOException e) {
                throw new BarocertException(-99999999, "Passcert fromGzipStream func finally close Exception", e);
            }
        }

        return sb.toString();
    }

    private String parseResponse(HttpURLConnection httpURLConnection) throws BarocertException {
        String result = "";
        InputStream input = null;
        BarocertException exception = null;

        try {
            input = httpURLConnection.getInputStream();
            if (null != httpURLConnection.getContentEncoding()
                    && httpURLConnection.getContentEncoding().equals("gzip")) {
                result = fromGzipStream(input);
            } else {
                result = fromStream(input);
            }
        } catch (IOException e) {
            InputStream errorIs = null;
            ErrorResponse error = null;

            try {
                errorIs = httpURLConnection.getErrorStream();
                result = fromStream(errorIs);
                error = fromJsonString(result, ErrorResponse.class);
            } catch (Exception ignored) {
                throw new BarocertException(-99999999, "Passcert parseResponse func InputStream Exception", ignored);
            } finally {
                try {
                    if (errorIs != null)
                        errorIs.close();
                } catch (IOException e1) {
                    throw new BarocertException(-99999999, "Passcert parseResponse func InputStream close() Exception", e1);
                }
            }

            if (error == null) {
                exception = new BarocertException(-99999999, "Passcert Fail to receive data from Server.", e);
            } else {
                exception = new BarocertException(error.getCode(), error.getMessage());
            }
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e2) {
                throw new BarocertException(-99999999, "Passcert parseResponse func InputStream close() Exception",
                        e2);
            }
        }

        if (exception != null)
            throw exception;

        return result;
    }
    

    private void setupEncryptor() throws BarocertException {
        if(encryptor == null) this.encryptor = Encryptor.newInstance(_SECRETKEY);
    }
    
    @Override
    public String encrypt(String plainText) throws BarocertException {
        setupEncryptor();
        return encryptor.enc(plainText);
    }


    // 본인인증 서명요청
    @Override
    public IdentityReceipt requestIdentity(String clientCode, Identity identity) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (identity == null) throw new BarocertException(-99999999, "본인인증 서명요청 정보가 입력되지 않았습니다.");
        if (isNullOrEmpty(identity.getReceiverHP())) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(identity.getReceiverName())) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (isNullOrEmpty(identity.getReceiverBirthday())) throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (isNullOrEmpty(identity.getReqTitle())) throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (isNullOrEmpty(identity.getCallCenterNum())) throw new BarocertException(-99999999, "고객센터 연락처가 입력되지 않았습니다.");
        if (identity.getExpireIn() == null) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullOrEmpty(identity.getToken())) throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");

        String postData = toJsonString(identity);

        return httpPost("/PASS/Identity/" + clientCode, postData, IdentityReceipt.class);
    }

    // 본인인증 상태확인
    @Override
    public IdentityStatus getIdentityStatus(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        return httpGet("/PASS/Identity/" + clientCode + "/" + receiptID, IdentityStatus.class);
    }

    // 본인인증 서명검증
    @Override
    public IdentityResult verifyIdentity(String clientCode, String receiptID, IdentityVerify verify) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");
        if (isNullOrEmpty(verify.getReceiverHP())) throw new BarocertException(-99999999, "검증 수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(verify.getReceiverName())) throw new BarocertException(-99999999, "검증 수신자 성명이 입력되지 않았습니다.");

        String postData = toJsonString(verify);

        return httpPost("/PASS/Identity/" + clientCode + "/" + receiptID, postData, IdentityResult.class);
    }

    // 전자서명 서명요청
    @Override
    public SignReceipt requestSign(String clientCode, Sign sign) throws BarocertException {

        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (sign == null) throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getReceiverHP())) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getReceiverName())) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getReqTitle())) throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getCallCenterNum())) throw new BarocertException(-99999999, "고객센터 연락처가 입력되지 않았습니다.");
        if (sign.getExpireIn() == null) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getToken())) throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getTokenType())) throw new BarocertException(-99999999, "원문 유형이 입력되지 않았습니다.");

        String postData = toJsonString(sign);

        return httpPost("/PASS/Sign/" + clientCode, postData, SignReceipt.class);
    }

    // 전자서명 상태확인
    @Override
    public SignStatus getSignStatus(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        return httpGet("/PASS/Sign/" + clientCode + "/" + receiptID, SignStatus.class);
    }

    // 전자서명 서명검증
    @Override
    public SignResult verifySign(String clientCode, String receiptID, SignVerify verify) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");
        if (isNullOrEmpty(verify.getReceiverHP())) throw new BarocertException(-99999999, "검증 수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(verify.getReceiverName())) throw new BarocertException(-99999999, "검증 수신자 성명이 입력되지 않았습니다.");

        String postData = toJsonString(verify);

        return httpPost("/PASS/Sign/" + clientCode + "/" + receiptID, postData, SignResult.class);
    }

    // 출금동의 서명요청
    @Override
    public CMSReceipt requestCMS(String clientCode, CMS cms) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (cms == null) throw new BarocertException(-99999999, "출금동의 서명요청 정보가 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getReceiverHP()))throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getReceiverName())) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getReqTitle())) throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getCallCenterNum())) throw new BarocertException(-99999999, "고객센터 연락처가 입력되지 않았습니다.");
        if (cms.getExpireIn() == null) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankName())) throw new BarocertException(-99999999, "출금은행명이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankAccountNum())) throw new BarocertException(-99999999, "출금계좌번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankAccountName())) throw new BarocertException(-99999999, "출금계좌 예금주명이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankServiceType())) throw new BarocertException(-99999999, "출금 유형이 입력되지 않았습니다.");

        String postData = toJsonString(cms);

        return httpPost("/PASS/CMS/" + clientCode, postData, CMSReceipt.class);
    }

    // 출금동의 상태확인
    @Override
    public CMSStatus getCMSStatus(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        return httpGet("/PASS/CMS/" + clientCode + "/" + receiptID, CMSStatus.class);
    }

    // 출금동의 서명검증
    @Override
    public CMSResult verifyCMS(String clientCode, String receiptID, CMSVerify verify) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");
        if (isNullOrEmpty(verify.getReceiverHP())) throw new BarocertException(-99999999, "검증 수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(verify.getReceiverName())) throw new BarocertException(-99999999, "검증 수신자 성명이 입력되지 않았습니다.");

        String postData = toJsonString(verify);

        return httpPost("/PASS/CMS/" + clientCode + "/" + receiptID, postData, CMSResult.class);
    }
    
    private boolean isNullOrEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}
    
    public void setIPRestrictOnOff(boolean isIPRestrictOnOff) {
        this.isIPRestrictOnOff = isIPRestrictOnOff;
    }

    public void setUseStaticIP(boolean useStaticIP) {
        this.useStaticIP = useStaticIP;
    }

    public void setUseLocalTimeYN(boolean useLocalTimeYN) {
        this.useLocalTimeYN = useLocalTimeYN;
    }

    public boolean isUseStaticIP() {
        return useStaticIP;
    }

    public boolean isUseLocalTimeYN() {
        return useLocalTimeYN;
    }

    public boolean isIPRestrictOnOff() {
        return isIPRestrictOnOff;
    }

    public void setProxyIP(String proxyIP) {
        this.proxyIP = proxyIP;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getLinkID() {
        return _linkID;
    }

    public void setLinkID(String linkID) {
        this._linkID = linkID;
    }

    public String getSecretKey() {
        return _SECRETKEY;
    }

    public void setSecretKey(String secretKey) {
        this._SECRETKEY = secretKey;
    }

}