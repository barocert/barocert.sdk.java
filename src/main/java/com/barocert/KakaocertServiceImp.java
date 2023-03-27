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
import com.barocert.kakaocert.cms.CMSResponse;
import com.barocert.kakaocert.cms.CMSStateResult;
import com.barocert.kakaocert.cms.CMSVerifyResult;
import com.barocert.kakaocert.esign.ESignMultiObject;
import com.barocert.kakaocert.esign.ESignMultiResponse;
import com.barocert.kakaocert.esign.MultiESignStateResult;
import com.barocert.kakaocert.esign.MultiESignVerifyResult;
import com.barocert.kakaocert.esign.ESignObject;
import com.barocert.kakaocert.esign.ESignResponse;
import com.barocert.kakaocert.esign.ESignStateResult;
import com.barocert.kakaocert.esign.ESignVerifyResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthObject;
import com.barocert.kakaocert.verifyauth.VerifyAuthResponse;
import com.barocert.kakaocert.verifyauth.VerifyAuthStateResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthResult;
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
    
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    
    private String AuthURL = null;
    private String ProxyIP = null;
    private Integer ProxyPort = null;

    private boolean useStaticIP;
    private boolean useLocalTimeYN;
    
    private String _linkID;
    private String _secretKey;
	
    private TokenBuilder tokenBuilder;

	private Gson _gsonParser = new Gson();
    private final SecureRandom secureRandom = new SecureRandom();
    private Map<String, Token> tokenTable = new HashMap<String, Token>();

    public KakaocertServiceImp() {
        useStaticIP = false;
        useLocalTimeYN = true;
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
        if (this.tokenBuilder == null) {
            tokenBuilder = TokenBuilder
            		.newInstance(getLinkID(), getSecretKey()).ServiceID(ServiceID).addScope("partner")
            		.useLocalTimeYN(useLocalTimeYN)
            		.addScope("401")
            		.addScope("402")
            		.addScope("403")
            		.addScope("404");

            if (AuthURL != null) {
                tokenBuilder.setServiceURL(AuthURL);
            } else {
        		if (useStaticIP) tokenBuilder.setServiceURL(Auth_Static_URL);
            }
            
            if (ProxyIP != null && ProxyPort != null) {
                tokenBuilder.setProxyIP(ProxyIP);
                tokenBuilder.setProxyPort(ProxyPort);
            }
        }

        return tokenBuilder;
    }

    private String getSessionToken(String ClientCode, String ForwardIP) throws BarocertException {
        Token token = null;
        Date UTCTime = null;

        if (tokenTable.containsKey(ClientCode))
            token = tokenTable.get(ClientCode);

        boolean expired = true;

        if (token != null) {
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat subFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            subFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                Date expiration = format.parse(token.getExpiration());
                UTCTime = subFormat.parse(getTokenbuilder().getTime());
                expired = expiration.before(UTCTime);
            } catch (LinkhubException le) {
                throw new BarocertException(le);
            } catch (Exception e) {
				throw new BarocertException(-99999999, "Kakaocert GetSessionToken Exception : " + e.getMessage());
			}
        }

        if (expired) {
            if (tokenTable.containsKey(ClientCode))
                tokenTable.remove(ClientCode);

            try {
            	token = getTokenbuilder().build("", "*");

                tokenTable.put(ClientCode, token);
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
    protected <T> T httpget(String url, String clientCode, Class<T> clazz) throws BarocertException {
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

        if (clientCode != null && clientCode.isEmpty() == false) {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken(clientCode, null));
        }

        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");

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

        if (clientCode != null && clientCode.isEmpty() == false) {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken(clientCode, null));
        }

        httpURLConnection.setRequestProperty("x-bc-date".toLowerCase(), date);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");

        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e1) {
        	throw new BarocertException(-99999999, "Kakaocert Protocol Exception : ", e1);
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
                    if (output != null) {
                        output.close();
                    }
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
            throw new BarocertException(-99999999, "Fail to Calculate HMAC-SHA256, Please check your SecretKey.", e);
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
            e.printStackTrace();
        }
        
        return base64Encode(byteBuffer.array());
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

            if (null != httpURLConnection.getContentEncoding() && httpURLConnection.getContentEncoding().equals("gzip")) {
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

            } finally {
                try {
                    if (errorIs != null) {
                        errorIs.close();
                    }
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
                if (input != null) {
                    input.close();
                }
            } catch (IOException e2) {
                throw new BarocertException(-99999999, "Kakaocert parseResponse func InputStream close() Exception", e2);
            }
        }

        if (exception != null)
            throw exception;

        return result;
    }

    // 전자서명 요청(단건)
    @Override
    public ESignResponse eSignRequest(String clientCode, ESignObject eSignObject) throws BarocertException {

    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == eSignObject)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        
        String postDate = toJsonString(eSignObject);
        
    	return httppost("/KAKAO/ESign/" + clientCode, clientCode, postDate, ESignResponse.class);
    }
    
    // 전자서명 요청(다건)
    @Override
    public ESignMultiResponse eSignMultiRequest(String clientCode, ESignMultiObject eSignMultiObject) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == eSignMultiObject)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
    	  
        String postDate = toJsonString(eSignMultiObject);

        return httppost("/KAKAO/ESignMulti/" + clientCode, clientCode, postDate, ESignMultiResponse.class);
    }

    // 전자서명 상태확인(단건)
    @Override
    public ESignStateResult getESignState(String clientCode, String receiptID) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESign/" + clientCode + "/" + receiptID, clientCode, ESignStateResult.class);
    }
    
    // 전자서명 상태확인(다건)
    @Override
	public MultiESignStateResult getMultiESignState(String clientCode, String receiptID) throws BarocertException {
    	
    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESignMulti/" + clientCode + "/" + receiptID, clientCode, MultiESignStateResult.class);
	}

    // 전자서명 서명검증(단건)
    @Override
    public ESignVerifyResult eSignVerify(String clientCode, String receiptID) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
		String postDate = toJsonString("");
        
        return httppost("/KAKAO/ESign/" + clientCode + "/" + receiptID, clientCode, postDate, ESignVerifyResult.class);
    }
    
    // 전자서명 서명검증(다건)
    @Override
	public MultiESignVerifyResult multiESignVerify(String clientCode, String receiptID) throws BarocertException {
		
    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

		String postDate = toJsonString("");
        
        return httppost("/KAKAO/ESignMulti/" + clientCode + "/" + receiptID, clientCode, postDate, MultiESignVerifyResult.class);
	}

    
    // 본인인증 요청
    @Override
    public VerifyAuthResponse verifyAuthRequest(String clientCode, VerifyAuthObject verifyAuthObject) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == verifyAuthObject)
            throw new BarocertException(-99999999, "본인인증 요청정보가 입력되지 않았습니다.");
        
        String postDate = toJsonString(verifyAuthObject);
        
        return httppost("/KAKAO/VerifyAuth/" + clientCode, clientCode, postDate, VerifyAuthResponse.class);
    }
    
    // 본인인증 상태확인
    @Override
    public VerifyAuthStateResult getVerifyAuthState(String clientCode, String receiptID) throws BarocertException {

    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        return httpget("/KAKAO/VerifyAuth/" + clientCode + "/" + receiptID, clientCode, VerifyAuthStateResult.class);
    }
    
    // 본인인증 서명검증
    @Override
    public VerifyAuthResult verifyAuth(String clientCode, String receiptID) throws BarocertException {
    	
        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        String postDate = toJsonString("");
        
        return httppost("/KAKAO/VerifyAuth/" + clientCode + "/" + receiptID, clientCode, postDate, VerifyAuthResult.class);
    }
    
    
    // 출금동의 요청
    @Override
    public CMSResponse cMSRequest(String clientCode, CMSObject cMSObject) throws BarocertException {

        if (clientCode == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (cMSObject == null || clientCode.length() == 0)
            throw new BarocertException(-99999999, "출금동의 요청정보가 입력되지 않았습니다.");
        
        String postDate = toJsonString(cMSObject);

        return httppost("/KAKAO/CMS/" + clientCode, clientCode, postDate, CMSResponse.class);
    }
    
    // 출금동의 상태확인
    @Override
    public CMSStateResult getCMSState(String clientCode, String receiptID) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/CMS/" + clientCode + "/" + receiptID, clientCode, CMSStateResult.class);
    }

    // 출금동의 서명검증
    @Override
    public CMSVerifyResult cMSVerify(String clientCode, String receiptID) throws BarocertException {
    	
        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        String postDate = toJsonString("");
        
        return httppost("/KAKAO/CMS/" + clientCode + "/" + receiptID, clientCode, postDate, CMSVerifyResult.class);
    }
    
}
