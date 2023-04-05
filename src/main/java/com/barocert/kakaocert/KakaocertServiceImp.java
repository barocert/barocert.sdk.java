package com.barocert.kakaocert;

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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

import com.barocert.BarocertException;
import com.barocert.kakaocert.cms.RequestCMS;
import com.barocert.kakaocert.cms.ResponseCMS;
import com.barocert.kakaocert.cms.ResponseStateCMS;
import com.barocert.kakaocert.cms.ResponseVerifyCMS;
import com.barocert.kakaocert.esign.RequestESign;
import com.barocert.kakaocert.esign.RequestMultiESign;
import com.barocert.kakaocert.esign.ResponseESign;
import com.barocert.kakaocert.esign.ResponseMultiESign;
import com.barocert.kakaocert.esign.ResponseStateESign;
import com.barocert.kakaocert.esign.ResponseStateMultiESign;
import com.barocert.kakaocert.esign.ResponseVerifyESign;
import com.barocert.kakaocert.esign.ResponseVerifyMultiESign;
import com.barocert.kakaocert.verifyauth.RequestVerifyAuth;
import com.barocert.kakaocert.verifyauth.ResponseStateVerify;
import com.barocert.kakaocert.verifyauth.ResponseVerify;
import com.barocert.kakaocert.verifyauth.ResponseVerifyAuth;
import com.google.gson.Gson;

import kr.co.linkhub.auth.Base64;
import kr.co.linkhub.auth.LinkhubException;
import kr.co.linkhub.auth.Token;
import kr.co.linkhub.auth.TokenBuilder;

public class KakaocertServiceImp implements KakaocertService {

    private static final String SERVICE_ID = "BAROCERT";

    private static final String AUTH_STATIC_URL = "https://static-auth.linkhub.co.kr";
    private static final String SERVICEURL_STATIC = "https://static-barocert.linkhub.co.kr";
    private static final String SERVICEURL = "https://barocert.linkhub.co.kr";
    private static final String APIVERSION = "2.0"; // sha256
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    
    private static final int GCM_TAG_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;

    private static final TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");
    private String proxyIP;
    private Integer proxyPort;

    private boolean isIPRestrictOnOff = true; // 인증통큰 발급 IP 제한 - 기본값(true)
    private boolean useStaticIP = false; // API 서비스고정 IP - 기본값(false)
    private boolean useLocalTimeYN = true; // 로컬시스템 시간 사용여부 - 기본값(true)

    private String _linkID;
    private String _SECRETKEY;
    private TokenBuilder tokenBuilder;

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
                    .useLocalTimeYN(useLocalTimeYN) // 로컬시스템 시간 사용여부.
                    .ServiceID(SERVICE_ID)          // 서비스아이디.
                    .addScope("partner")            // partner
                    .addScope("401")                // 전자서명(단건)
                    .addScope("402")                // 전자서명(복수)
                    .addScope("403")                // 본인인증
                    .addScope("404");               // 출금동의

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
                throw new BarocertException(-99999999, "Kakaocert SessionToken Exception", le);
            } catch (Exception e) {
                throw new BarocertException(-99999999, "Kakaocert Parse Exception", e);
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
                throw new BarocertException(-99999999, "Kakaocert GetSessionToken Exception", le);
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
    protected <T> T httpget(String url, String clientCode, Class<T> clazz) throws BarocertException {
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
            throw new BarocertException(-99999999, "Kakaocert API 서버 접속 실패", e);
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
    protected <T> T httppost(String url, String clientCode, String PostData, Class<T> clazz) throws BarocertException {
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
            throw new BarocertException(-99999999, "Kakaocert API 서버 접속 실패", e);
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
            throw new BarocertException(-99999999, "Kakaocert Protocol Exception", e1);
        }

        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoOutput(true);

        if ((PostData == null || PostData.isEmpty()) == false) {

            byte[] btPostData = PostData.getBytes(Charset.forName("UTF-8"));

            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(btPostData.length));

            String signTarget = "POST\n";
            signTarget += url + "\n";
            signTarget += sha256Base64(btPostData) + "\n";
            signTarget += date + "\n";

            String Signature = base64Encode(
                    HMacSha256(base64Decode(getSecretKey()), signTarget.getBytes(Charset.forName("UTF-8"))));

            httpURLConnection.setRequestProperty("x-bc-version".toLowerCase(), APIVERSION);
            httpURLConnection.setRequestProperty("x-bc-auth", Signature);
            httpURLConnection.setRequestProperty("x-bc-encryptionmode", "GCM");

            DataOutputStream output = null;

            try {
                output = new DataOutputStream(httpURLConnection.getOutputStream());
                output.write(btPostData);
                output.flush();
            } catch (Exception e) {
                throw new BarocertException(-99999999, "Kakaocert Fail to POST data to Server.", e);
            } finally {
                try {
                    if (output != null)
                        output.close();
                } catch (IOException e1) {
                    throw new BarocertException(-99999999, "Kakaocert httppost func DataOutputStream close() Exception",
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
            throw new BarocertException(-99999999, "Kakaocert sha256 base64 Exception", e);
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
            throw new BarocertException(-99999999,
                    "Kakaocert Fail to Calculate HMAC-SHA256, Please check your SecretKey.", e);
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
            throw new BarocertException(-99999999, "Kakaocert fromStream func Exception", e);
        } finally {
            try {
                if (br != null)
                    br.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                throw new BarocertException(-99999999, "Kakaocert fromStream func finally close Exception", e);
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
            throw new BarocertException(-99999999, "Kakaocert fromGzipStream func Exception", e);
        } finally {
            try {
                if (br != null)
                    br.close();
                if (is != null)
                    is.close();
                if (zipReader != null)
                    zipReader.close();
            } catch (IOException e) {
                throw new BarocertException(-99999999, "Kakaocert fromGzipStream func finally close Exception", e);
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
                throw new BarocertException(-99999999, "Kakaocert parseResponse func InputStream Exception", ignored);
            } finally {
                try {
                    if (errorIs != null)
                        errorIs.close();
                } catch (IOException e1) {
                    throw new BarocertException(-99999999, "Kakaocert parseResponse func InputStream close() Exception",
                            e1);
                }
            }

            if (error == null) {
                exception = new BarocertException(-99999999, "Kakaocert Fail to receive data from Server.", e);
            } else {
                exception = new BarocertException(error.getCode(), error.getMessage());
            }
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e2) {
                throw new BarocertException(-99999999, "Kakaocert parseResponse func InputStream close() Exception",
                        e2);
            }
        }

        if (exception != null)
            throw exception;

        return result;
    }
    
    
    public String encryptGCM(String plainText)  throws BarocertException {
        ByteBuffer byteBuffer = null;

        if (plainText == null || plainText.length() == 0)
            throw new BarocertException(-99999999, "KaKaoCert. There is nothing to encrypt.");

        try
        {

            byte[] keyBytes = base64Decode(_SECRETKEY);
            byte[] iv = generateGCMiV();

            GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            AEADParameters parameters = new AEADParameters(new KeyParameter(keyBytes), GCM_TAG_LENGTH * 8, iv, null);
            cipher.init(true, parameters);

            byte[] plaintextBytes = plainText.getBytes(Charset.forName("UTF-8"));
            byte[] ciphertextBytes = new byte[cipher.getOutputSize(plaintextBytes.length)];
            int ciphertextLength = cipher.processBytes(plaintextBytes, 0, plaintextBytes.length, ciphertextBytes, 0);

            try {
                cipher.doFinal(ciphertextBytes, ciphertextLength);
            } catch (Exception e) {
                e.printStackTrace();
            }
            byteBuffer = ByteBuffer.allocate(iv.length + ciphertextBytes.length);
            byteBuffer.put(iv); // 벡터 추가.
            byteBuffer.put(ciphertextBytes); // 암호문 추가.

        }catch(Exception e){
            throw new BarocertException(-99999999, "KaKaoCert AES256 Encrypt Exception", e);
        }

        return base64Encode(byteBuffer.array());
    }
    
    private byte[] generateGCMiV() {
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] iv = new byte[GCM_IV_LENGTH];
        random.nextBytes(iv);
        return iv;
    }

    // 본인인증 서명요청
    @Override
    public ResponseVerify requestVerifyAuth(String clientCode, RequestVerifyAuth requestVerifyAuth) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (requestVerifyAuth == null)
            throw new BarocertException(-99999999, "본인인증 서명요청 정보가 입력되지 않았습니다.");
        if (requestVerifyAuth.getReceiverHP() == null || requestVerifyAuth.getReceiverHP().length() == 0)
            throw new BarocertException(-99999999, "수신자 휴대폰 번호가 입력되지 않았습니다.");
        if (requestVerifyAuth.getReceiverName() == null || requestVerifyAuth.getReceiverName().length() == 0)
            throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (requestVerifyAuth.getReceiverBirthday() == null || requestVerifyAuth.getReceiverBirthday().length() == 0)
            throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (requestVerifyAuth.getReqTitle() == null || requestVerifyAuth.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (requestVerifyAuth.getExpireIn() == null)
            throw new BarocertException(-99999999, "유효 만료일시가 입력되지 않았습니다.");
        if (requestVerifyAuth.getToken() == null || requestVerifyAuth.getToken().length() == 0)
            throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");

        String postDate = toJsonString(requestVerifyAuth);

        return httppost("/KAKAO/VerifyAuth/" + clientCode, clientCode, postDate, ResponseVerify.class);
    }

    // 본인인증 상태확인
    @Override
    public ResponseStateVerify stateVerifyAuth(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/VerifyAuth/" + clientCode + "/" + receiptID, clientCode, ResponseStateVerify.class);
    }

    // 본인인증 서명검증
    @Override
    public ResponseVerifyAuth verifyVerifyAuth(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        String postDate = toJsonString("");

        return httppost("/KAKAO/VerifyAuth/" + clientCode + "/" + receiptID, clientCode, postDate,
                ResponseVerifyAuth.class);
    }

    // 전자서명 서명요청(단건)
    @Override
    public ResponseESign requestESign(String clientCode, RequestESign requestESign)
            throws BarocertException {

        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (requestESign == null)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        if (requestESign.getReceiverHP() == null || requestESign.getReceiverHP().length() == 0)
            throw new BarocertException(-99999999, "수신자 휴대폰 번호가 입력되지 않았습니다.");
        if (requestESign.getReceiverName() == null || requestESign.getReceiverName().length() == 0)
            throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (requestESign.getReceiverBirthday() == null || requestESign.getReceiverBirthday().length() == 0)
            throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (requestESign.getReqTitle() == null || requestESign.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (requestESign.getToken() == null || requestESign.getToken().length() == 0)
            throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");
        if (requestESign.getTokenType() == null || requestESign.getTokenType().length() == 0)
            throw new BarocertException(-99999999, "서명대상 유형코드가 입력되지 않았습니다.");

        String postData = toJsonString(requestESign);

        return httppost("/KAKAO/ESign/" + clientCode, clientCode, postData, ResponseESign.class);
    }

    // 전자서명 상태확인(단건)
    @Override
    public ResponseStateESign stateESign(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESign/" + clientCode + "/" + receiptID, clientCode, ResponseStateESign.class);
    }

    // 전자서명 서명검증(단건)
    @Override
    public ResponseVerifyESign verifyESign(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        String postDate = toJsonString("");

        return httppost("/KAKAO/ESign/" + clientCode + "/" + receiptID, clientCode, postDate,
                ResponseVerifyESign.class);
    }

    // 전자서명 서명요청(복수)
    @Override
    public ResponseMultiESign requestMultiESign(String clientCode, RequestMultiESign requestMultiESign)
            throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (requestMultiESign == null)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        if (requestMultiESign.getReceiverHP() == null || requestMultiESign.getReceiverHP().length() == 0)
            throw new BarocertException(-99999999, "수신자 휴대폰 번호가 입력되지 않았습니다.");
        if (requestMultiESign.getReceiverName() == null || requestMultiESign.getReceiverName().length() == 0)
            throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (requestMultiESign.getReceiverBirthday() == null || requestMultiESign.getReceiverBirthday().length() == 0)
            throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (requestMultiESign.getReqTitle() == null || requestMultiESign.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (requestMultiESign.getExpireIn() == null)
            throw new BarocertException(-99999999, "유효 만료일시가 입력되지 않았습니다.");
        if (requestMultiESign.getTokens() == null || requestMultiESign.getTokens().isEmpty()
                || requestMultiESign.getTokens().size() == 0)
            throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");
        if (requestMultiESign.getTokenType() == null || requestMultiESign.getTokenType().length() == 0)
            throw new BarocertException(-99999999, "서명대상 유형코드가 입력되지 않았습니다.");

        String postDate = toJsonString(requestMultiESign);

        return httppost("/KAKAO/ESignMulti/" + clientCode, clientCode, postDate, ResponseMultiESign.class);
    }

    // 전자서명 상태확인(복수)
    @Override
    public ResponseStateMultiESign stateMultiESign(String clientCode, String receiptID)
            throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESignMulti/" + clientCode + "/" + receiptID, clientCode, ResponseStateMultiESign.class);
    }

    // 전자서명 서명검증(복수)
    @Override
    public ResponseVerifyMultiESign verifyMultiESign(String clientCode, String receiptID)
            throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        String postDate = toJsonString("");

        return httppost("/KAKAO/ESignMulti/" + clientCode + "/" + receiptID, clientCode, postDate,
                ResponseVerifyMultiESign.class);
    }

    // 출금동의 서명요청
    @Override
    public ResponseCMS requestCMS(String clientCode, RequestCMS requestCMS) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (requestCMS == null)
            throw new BarocertException(-99999999, "출금동의 서명요청 정보가 입력되지 않았습니다.");
        if (requestCMS.getReceiverHP() == null || requestCMS.getReceiverHP().length() == 0)
            throw new BarocertException(-99999999, "수신자 휴대폰 번호가 입력되지 않았습니다.");
        if (requestCMS.getReceiverName() == null || requestCMS.getReceiverName().length() == 0)
            throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (requestCMS.getBankAccountBirthday() == null || requestCMS.getBankAccountBirthday().length() == 0)
            throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (requestCMS.getReqTitle() == null || requestCMS.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (requestCMS.getExpireIn() == null)
            throw new BarocertException(-99999999, "유효 만료일시가 입력되지 않았습니다.");
        if (requestCMS.getRequestCorp() == null || requestCMS.getRequestCorp().length() == 0)
            throw new BarocertException(-99999999, "청구기관명이 입력되지 않았습니다.");
        if (requestCMS.getBankName() == null || requestCMS.getBankName().length() == 0)
            throw new BarocertException(-99999999, "은행명이 입력되지 않았습니다.");
        if (requestCMS.getBankAccountNum() == null || requestCMS.getBankAccountNum().length() == 0)
            throw new BarocertException(-99999999, "예금주 생년월일이 입력되지 않았습니다.");
        if (requestCMS.getBankAccountName() == null || requestCMS.getBankAccountName().length() == 0)
            throw new BarocertException(-99999999, "계좌번호가 입력되지 않았습니다.");
        if (requestCMS.getBankAccountBirthday() == null || requestCMS.getBankAccountBirthday().length() == 0)
            throw new BarocertException(-99999999, "예금주명이 입력되지 않았습니다.");
        if (requestCMS.getBankServiceType() == null || requestCMS.getBankServiceType().length() == 0)
            throw new BarocertException(-99999999, "출금동의 서비스 유형코드가 입력되지 않았습니다.");

        String postDate = toJsonString(requestCMS);

        return httppost("/KAKAO/CMS/" + clientCode, clientCode, postDate, ResponseCMS.class);
    }

    // 출금동의 상태확인
    @Override
    public ResponseStateCMS stateCMS(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/CMS/" + clientCode + "/" + receiptID, clientCode, ResponseStateCMS.class);
    }

    // 출금동의 서명검증
    @Override
    public ResponseVerifyCMS verifyCMS(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        String postDate = toJsonString("");

        return httppost("/KAKAO/CMS/" + clientCode + "/" + receiptID, clientCode, postDate, ResponseVerifyCMS.class);
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