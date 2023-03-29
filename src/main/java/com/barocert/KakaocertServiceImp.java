package com.barocert;

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

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.barocert.kakaocert.cms.CMSObject;
import com.barocert.kakaocert.cms.ResponseCMS;
import com.barocert.kakaocert.cms.ResponseStateCMS;
import com.barocert.kakaocert.cms.ResponseVerifyCMS;
import com.barocert.kakaocert.esign.MultiESignObject;
import com.barocert.kakaocert.esign.ResponseMultiESign;
import com.barocert.kakaocert.esign.ResponseStateMultiESign;
import com.barocert.kakaocert.esign.ResponseVerifyMultiESign;
import com.barocert.kakaocert.esign.ESignObject;
import com.barocert.kakaocert.esign.ResponseESign;
import com.barocert.kakaocert.esign.ResponseStateESign;
import com.barocert.kakaocert.esign.ResponseVerifyESign;
import com.barocert.kakaocert.verifyauth.AuthObject;
import com.barocert.kakaocert.verifyauth.ResponseAuth;
import com.barocert.kakaocert.verifyauth.ResponseStateAuth;
import com.barocert.kakaocert.verifyauth.ResponseVerifyAuth;
import com.google.gson.Gson;

import kr.co.linkhub.auth.Base64;
import kr.co.linkhub.auth.LinkhubException;
import kr.co.linkhub.auth.Token;
import kr.co.linkhub.auth.TokenBuilder;

public class KakaocertServiceImp implements KakaocertService {

    private static final String ServiceID = "BAROCERT";
    
    private static final String Auth_Static_URL = "https://static-auth.linkhub.co.kr";
    private static final String ServiceURL_Static = "https://static-barocert.linkhub.co.kr";
    private String ServiceURL = "https://barocert.linkhub.co.kr";
    
    private static final String APIVersion = "2.0";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    
    private String AuthURL = null;
    private String ProxyIP = null;
    private Integer ProxyPort = null;
    private String ForwardIP = null;

    private boolean isIPRestrictOnOff;
    private boolean useStaticIP;
    private boolean useLocalTimeYN;
    
    private String _linkID;
    private String _secretKey;
	
    private TokenBuilder tokenBuilder;

    private Gson _gsonParser = new Gson();
    private final SecureRandom secureRandom = new SecureRandom();
    private Map<String, Token> tokenTable = new HashMap<String, Token>();

    public KakaocertServiceImp() {
    	isIPRestrictOnOff = true; // 인증통큰 발급 IP 제한 - 기본값(true)
        useStaticIP = false; // API 서비스고정 IP - 기본값(false)
        useLocalTimeYN = true; // 로컬시스템 시간 사용여부 - 기본값(true)
    }
    
    public boolean isIPRestrictOnOff() {
        return isIPRestrictOnOff;
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

    public String getServiceURL() {
        if (ServiceURL != null)
            return ServiceURL;
        
        if (useStaticIP) 
        	return ServiceURL_Static;
        
        return ServiceURL;
    }

    public void setServiceURL(String serviceURL) {
        ServiceURL = serviceURL;
    }

    public void setProxyIP(String proxyIP) {
       this.ProxyIP = proxyIP;
    }

    public void setProxyPort(Integer proxyPort) {
       this.ProxyPort = proxyPort;
    }

    public String getAuthURL() {
        return AuthURL;
    }

    public void setAuthURL(String authURL) {
        AuthURL = authURL;
    }

    public String getLinkID() {
        return _linkID;
    }

    public void setLinkID(String linkID) {
        this._linkID = linkID;
    }

    public String getSecretKey() {
        return _secretKey;
    }

    public void setSecretKey(String secretKey) {
        this._secretKey = secretKey;
    }

    private TokenBuilder getTokenbuilder() {
        if (this.tokenBuilder == null) { // token 이 없다면.
            tokenBuilder = TokenBuilder
            		.newInstance(getLinkID(), getSecretKey()) // LinkID, SecretKey 
            		.useLocalTimeYN(useLocalTimeYN) // 로컬시스템 시간 사용여부.
            		.ServiceID(ServiceID) // 서비스아이디.
            		.addScope("partner")  // partner
            		.addScope("401")  // ESign
            		.addScope("402")  // ESign
            		.addScope("403")  // VerifyAuth
            		.addScope("404"); // CMS

            if (AuthURL != null) { // AuthURL(프록시URL) 이 있다면,
                tokenBuilder.setServiceURL(AuthURL); // ServiceURL 은 AuthURL(프록시URL) 로 설정.
            } else {
            	// AuthURL 이 null 이고, useStaticIP 가 true 이면, ServiceURL 이 Auth_Static_URL 적용.
                if (useStaticIP) tokenBuilder.setServiceURL(Auth_Static_URL);
            }
            
            if (ProxyIP != null && ProxyPort != null) { // ProxyIP 와 ProxyPort 가 없다면,
                tokenBuilder.setProxyIP(ProxyIP);     // 입력한 ProxyIP 셋팅.
                tokenBuilder.setProxyPort(ProxyPort); // 입력한 ProxyPort 셋팅.
            }
        }

        return tokenBuilder;
    }

    private String getSessionToken(String ClientCode, String ForwardIP) throws BarocertException {
        Token token = null;
        Date UTCTime = null;
        
        if (tokenTable.containsKey(ClientCode)) // tokenTable에 Key가 있다면,
            token = tokenTable.get(ClientCode);
        
        boolean expired = true;
        
        if (token != null) { // 토큰이 있다면, 토큰 유효성체크.
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat subFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            subFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

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
            if (tokenTable.containsKey(ClientCode))
                tokenTable.remove(ClientCode);
            
            try {
            	if(isIPRestrictOnOff) { // 인증토큰 발급 IP 제한. 기본값(true)
                    token = getTokenbuilder().build(null, ForwardIP);
            	} else {
            		token = getTokenbuilder().build(null, "*");
            	}

                tokenTable.put(ClientCode, token);
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
            
            if (ProxyIP != null && ProxyPort != null) { // 프록시 정보가 있다면,
            	// 프록시 객체를 통한 연결설정.
                Proxy prx = new Proxy(Type.HTTP, new InetSocketAddress(ProxyIP, ProxyPort)); // 프록시서버 및 포트 설정
                httpURLConnection = (HttpURLConnection) uri.openConnection(prx); // URL Connection
            } else {
                httpURLConnection = (HttpURLConnection) uri.openConnection();
            }
        } catch (Exception e) {
            throw new BarocertException(-99999999, "Kakaocert API 서버 접속 실패", e);
        }
        
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken(clientCode, null));
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        httpURLConnection.setRequestProperty("x-bc-version".toLowerCase(), APIVersion);

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
            
            if (ProxyIP != null && ProxyPort != null) {
                Proxy prx = new Proxy(Type.HTTP, new InetSocketAddress(ProxyIP, ProxyPort));
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
        
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken(clientCode, null));
        httpURLConnection.setRequestProperty("x-bc-date".toLowerCase(), date);
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

            String Signature = base64Encode(HMacSha256(base64Decode(getSecretKey()), signTarget.getBytes(Charset.forName("UTF-8"))));

            httpURLConnection.setRequestProperty("x-bc-version".toLowerCase(), APIVersion);
            httpURLConnection.setRequestProperty("x-bc-auth", " " + Signature);
            httpURLConnection.setRequestProperty("x-bc-encryptionmode", "CBC");

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
                    throw new BarocertException(-99999999, "Kakaocert httppost func DataOutputStream close() Exception", e1);
                }
            }
        }

        String Result = parseResponse(httpURLConnection);

        return fromJsonString(Result, clazz);
    }


    private static String sha256Base64(byte[] input) {
        MessageDigest md;
        byte[] btResult = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            btResult = md.digest(input);
        } catch (NoSuchAlgorithmException e) {
        }

        return Base64.encode(btResult);
    }

    private static byte[] base64Decode(String input) {
        return Base64.decode(input);
    }

    private static String base64Encode(byte[] input) {
        return Base64.encode(input);
    }
    
    private static byte[] HMacSha256(byte[] key, byte[] input) throws BarocertException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA256_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            return mac.doFinal(input);
        } catch (Exception e) {
            throw new BarocertException(-99999999, "Kakaocert Fail to Calculate HMAC-SHA256, Please check your SecretKey.", e);
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
    
    protected byte[] GenerateRandomKeyByte()  {
        byte[] iv = new byte[16]; // CBC IV LENGTH
        secureRandom.nextBytes(iv);
		
        return iv;
    }
    
    @Override
    public String AES256Encrypt(String plainText) throws BarocertException {
        ByteBuffer byteBuffer = null;
        
        if (plainText == null || plainText.length() == 0)
        	throw new BarocertException(-99999999, "KaKaoCert. There is nothing to encrypt.");
		
        try {
            byte[] iv = GenerateRandomKeyByte();
		
            SecretKeySpec keySpec = new SecretKeySpec(base64Decode(_secretKey), "AES");
		    
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec,new IvParameterSpec(iv));
		    
            byte[] encryptedData = cipher.doFinal(plainText.getBytes(Charset.forName("UTF-8")));
		
            byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
            byteBuffer.put(iv);
            byteBuffer.put(encryptedData);
        } catch (Exception e) {
            throw new BarocertException(-99999999, "KaKaoCert AES256 Encrypt Exception", e);
        }
		
        return base64Encode(byteBuffer.array());
    }

    private static String fromStream(InputStream input) throws BarocertException {
        InputStreamReader is = null;
        BufferedReader br = null;
        StringBuilder sb = null;
		
        try {
            is = new InputStreamReader(input, Charset.forName("UTF-8")); // UTF-8 디코딩.
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
            zipReader = new GZIPInputStream(input); // GZIP 포멧으로 압출풀기.
            is = new InputStreamReader(zipReader, "UTF-8"); // UTF-8 디코딩.
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

            if (null != httpURLConnection.getContentEncoding() && httpURLConnection.getContentEncoding().equals("gzip")) {
                result = fromGzipStream(input); // GZiP 압축해제.
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

            } finally {
                try {
                    if (errorIs != null)
                        errorIs.close();
                } catch (IOException e1) {
                    throw new BarocertException(-99999999, "Kakaocert parseResponse func InputStream close() Exception", e1);
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
                throw new BarocertException(-99999999, "Kakaocert parseResponse func InputStream close() Exception", e2);
            }
        }

        if (exception != null)
            throw exception;

        return result;
    }

    // 본인인증 서명요청
    @Override
    public ResponseAuth requestAuth(String clientCode, AuthObject authObject) throws BarocertException {
    	
    	String Target = "KakaoCert requestAuth";

    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (authObject == null)
            throw new BarocertException(-99999999, Target + " : 본인인증 서명요청 정보가 입력되지 않았습니다.");
    	if(authObject.getReceiverHP() == null || authObject.getReceiverHP().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 수신자 휴대폰 번호가 입력되지 않았습니다.");
    	if(authObject.getReceiverName() == null || authObject.getReceiverName().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 수신자 성명이 입력되지 않았습니다.");
    	if(authObject.getReceiverBirthday() == null || authObject.getReceiverBirthday().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 생년월일이 입력되지 않았습니다.");
        if (authObject.getReqTitle() == null || authObject.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, Target + " : 인증요청 메시지 제목이 입력되지 않았습니다.");
        if (authObject.getExpireIn() == null)
            throw new BarocertException(-99999999, Target + " : 유효 만료일시가 입력되지 않았습니다.");
        if (authObject.getToken() == null || authObject.getToken().length() == 0)
            throw new BarocertException(-99999999, Target + " : 토큰 원문이 입력되지 않았습니다.");
        
        String postDate = toJsonString(authObject);
        
        return httppost("/KAKAO/VerifyAuth/" + clientCode, clientCode, postDate, ResponseAuth.class);
    }
    
    // 본인인증 상태확인
    @Override
    public ResponseStateAuth requestStateAuth(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestStateAuth";

    	// 필수 값 체크.
    	if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");
        
        return httpget("/KAKAO/VerifyAuth/" + clientCode + "/" + receiptID, clientCode, ResponseStateAuth.class);
    }
    
    // 본인인증 서명검증
    @Override
    public ResponseVerifyAuth requestVerifyAuth(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestVerifyAuth";
    	
    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");
        
        String postDate = toJsonString("");
        
        return httppost("/KAKAO/VerifyAuth/" + clientCode + "/" + receiptID, clientCode, postDate, ResponseVerifyAuth.class);
    }

    // 전자서명 서명요청(단건)
    @Override
    public ResponseESign requestESign(String clientCode, ESignObject eSignObject) throws BarocertException {
    	
    	String Target = "KakaoCert requestESign";

    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (eSignObject == null)
            throw new BarocertException(-99999999, Target + " : 전자서명 요청정보가 입력되지 않았습니다.");
    	if (eSignObject.getReceiverHP() == null || eSignObject.getReceiverHP().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 수신자 휴대폰 번호가 입력되지 않았습니다.");
    	if (eSignObject.getReceiverName() == null || eSignObject.getReceiverName().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 수신자 성명이 입력되지 않았습니다.");
    	if (eSignObject.getReceiverBirthday() == null || eSignObject.getReceiverBirthday().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 생년월일이 입력되지 않았습니다.");
        if (eSignObject.getReqTitle() == null || eSignObject.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, Target + " : 인증요청 메시지 제목이 입력되지 않았습니다.");
        if (eSignObject.getExpireIn() == null)
            throw new BarocertException(-99999999, Target + " : 유효 만료일시가 입력되지 않았습니다.");
        if (eSignObject.getToken() == null || eSignObject.getToken().length() == 0)
            throw new BarocertException(-99999999, Target + " : 토큰 원문이 입력되지 않았습니다.");
        if (eSignObject.getTokenType() == null || eSignObject.getTokenType().length() == 0)
            throw new BarocertException(-99999999, Target + " : 서명대상 유형코드가 입력되지 않았습니다.");
        
        String postDate = toJsonString(eSignObject);
        
    	return httppost("/KAKAO/ESign/" + clientCode, clientCode, postDate, ResponseESign.class);
    }
    
    // 전자서명 상태확인(단건)
    @Override
    public ResponseStateESign requestStateESign(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestStateESign";

    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESign/" + clientCode + "/" + receiptID, clientCode, ResponseStateESign.class);
    }
    
    // 전자서명 서명검증(단건)
    @Override
    public ResponseVerifyESign requestVerifyESign(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestVerifyESign";

    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");
        
		String postDate = toJsonString("");
        
        return httppost("/KAKAO/ESign/" + clientCode + "/" + receiptID, clientCode, postDate, ResponseVerifyESign.class);
    }
    
    // 전자서명 서명요청(다건)
    @Override
    public ResponseMultiESign requestMultiESign(String clientCode, MultiESignObject multiESignObject) throws BarocertException {
    	
    	String Target = "KakaoCert requestMultiESign";

    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (multiESignObject == null)
            throw new BarocertException(-99999999, Target + " : 전자서명 요청정보가 입력되지 않았습니다.");
    	if (multiESignObject.getReceiverHP() == null || multiESignObject.getReceiverHP().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 수신자 휴대폰 번호가 입력되지 않았습니다.");
    	if (multiESignObject.getReceiverName() == null || multiESignObject.getReceiverName().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 수신자 성명이 입력되지 않았습니다.");
    	if (multiESignObject.getReceiverBirthday() == null || multiESignObject.getReceiverBirthday().length() == 0)
    		throw new BarocertException(-99999999, Target + " : 생년월일이 입력되지 않았습니다.");
        if (multiESignObject.getReqTitle() == null || multiESignObject.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, Target + " : 인증요청 메시지 제목이 입력되지 않았습니다.");
        if (multiESignObject.getExpireIn() == null)
            throw new BarocertException(-99999999, Target + " : 유효 만료일시가 입력되지 않았습니다.");
        if (multiESignObject.getTokens() == null || multiESignObject.getTokens().isEmpty() || multiESignObject.getTokens().size() == 0)
            throw new BarocertException(-99999999, Target + " : 토큰 원문이 입력되지 않았습니다.");
        if (multiESignObject.getTokenType() == null || multiESignObject.getTokenType().length() == 0)
            throw new BarocertException(-99999999, Target + " : 서명대상 유형코드가 입력되지 않았습니다.");

        String postDate = toJsonString(multiESignObject);

        return httppost("/KAKAO/ESignMulti/" + clientCode, clientCode, postDate, ResponseMultiESign.class);
    }
    
    // 전자서명 상태확인(다건)
    @Override
    public ResponseStateMultiESign requestStateMultiESign(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestStateMultiESign";
    	
    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");
		
        return httpget("/KAKAO/ESignMulti/" + clientCode + "/" + receiptID, clientCode, ResponseStateMultiESign.class);
    }
    
    // 전자서명 서명검증(다건)
    @Override
    public ResponseVerifyMultiESign requestVerifyMultiESign(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestVerifyMultiESign";
		
    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");
		
        String postDate = toJsonString("");
		
        return httppost("/KAKAO/ESignMulti/" + clientCode + "/" + receiptID, clientCode, postDate, ResponseVerifyMultiESign.class);
    }
    
    // 출금동의 서명요청
    @Override
    public ResponseCMS requestCMS(String clientCode, CMSObject cMSObject) throws BarocertException {
    	
    	String Target = "KakaoCert requestCMS";
    	
    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (cMSObject == null)
            throw new BarocertException(-99999999, Target + " : 출금동의 서명요청 정보가 입력되지 않았습니다.");
    	if (cMSObject.getReceiverHP() == null || cMSObject.getReceiverHP().length() == 0)
            throw new BarocertException(-99999999, Target + " : 수신자 휴대폰 번호가 입력되지 않았습니다.");
        if (cMSObject.getReceiverName() == null || cMSObject.getReceiverName().length() == 0)
            throw new BarocertException(-99999999, Target + " : 수신자 성명이 입력되지 않았습니다.");
        if (cMSObject.getBankAccountBirthday() == null || cMSObject.getBankAccountBirthday().length() == 0)
            throw new BarocertException(-99999999, Target + " : 생년월일이 입력되지 않았습니다.");
        if (cMSObject.getReqTitle() == null || cMSObject.getReqTitle().length() == 0)
            throw new BarocertException(-99999999, Target + " : 인증요청 메시지 제목이 입력되지 않았습니다.");
        if (cMSObject.getExpireIn() == null)
            throw new BarocertException(-99999999, Target + " : 유효 만료일시가 입력되지 않았습니다.");
        if (cMSObject.getRequestCorp() == null || cMSObject.getRequestCorp().length() == 0)
            throw new BarocertException(-99999999, Target + " : 청구기관명이 입력되지 않았습니다.");
        if (cMSObject.getBankName() == null || cMSObject.getBankName().length() == 0)
            throw new BarocertException(-99999999, Target + " : 은행명이 입력되지 않았습니다.");
        if (cMSObject.getBankAccountNum() == null || cMSObject.getBankAccountNum().length() == 0)
            throw new BarocertException(-99999999, Target + " : 예금주 생년월일이 입력되지 않았습니다.");
        if (cMSObject.getBankAccountName() == null || cMSObject.getBankAccountName().length() == 0)
            throw new BarocertException(-99999999, Target + " : 계좌번호가 입력되지 않았습니다.");
        if (cMSObject.getBankAccountBirthday() == null || cMSObject.getBankAccountBirthday().length() == 0)
            throw new BarocertException(-99999999, Target + " : 예금주명이 입력되지 않았습니다.");
        if (cMSObject.getBankServiceType() == null || cMSObject.getBankServiceType().length() == 0)
            throw new BarocertException(-99999999, Target + " : 출금동의 서비스 유형코드가 입력되지 않았습니다.");
        
        String postDate = toJsonString(cMSObject);

        return httppost("/KAKAO/CMS/" + clientCode, clientCode, postDate, ResponseCMS.class);
    }
    
    // 출금동의 상태확인
    @Override
    public ResponseStateCMS requestStateCMS(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestStateCMS";

    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/CMS/" + clientCode + "/" + receiptID, clientCode, ResponseStateCMS.class);
    }

    // 출금동의 서명검증
    @Override
    public ResponseVerifyCMS requestVerifyCMS(String clientCode, String receiptID) throws BarocertException {
    	
    	String Target = "KakaoCert requestVerifyCMS";
    	
    	// 필수 값 체크.
        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, Target + " : 이용기관코드가 입력되지 않았습니다.");
        if (receiptID == null || receiptID.length() == 0)
            throw new BarocertException(-99999999, Target + " : 접수아이디가 입력되지 않았습니다.");
        
        String postDate = toJsonString("");
        
        return httppost("/KAKAO/CMS/" + clientCode + "/" + receiptID, clientCode, postDate, ResponseVerifyCMS.class);
    }
    
}