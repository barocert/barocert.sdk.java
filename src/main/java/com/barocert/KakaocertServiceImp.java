package com.barocert;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.barocert.kakaocert.cms.CMSRequest;
import com.barocert.kakaocert.cms.CMSResponse;
import com.barocert.kakaocert.cms.CMSStateResult;
import com.barocert.kakaocert.cms.CMSVerifyResult;
import com.barocert.kakaocert.esign.ESMultiRequest;
import com.barocert.kakaocert.esign.ESMultiResponse;
import com.barocert.kakaocert.esign.BulkESVerifyRequest;
import com.barocert.kakaocert.esign.MultiStateResult;
import com.barocert.kakaocert.esign.ESMultiVerifyResult;
import com.barocert.kakaocert.esign.ESRequest;
import com.barocert.kakaocert.esign.ESResponse;
import com.barocert.kakaocert.esign.ESStateResult;
import com.barocert.kakaocert.esign.ESVerifyRequest;
import com.barocert.kakaocert.esign.ESVerifyResult;
import com.barocert.kakaocert.verifyauth.VAVerifyRequest;
import com.barocert.kakaocert.verifyauth.VARequest;
import com.barocert.kakaocert.verifyauth.VAResponse;
import com.barocert.kakaocert.verifyauth.VAStateResult;
import com.barocert.kakaocert.verifyauth.VAVerifyResult;
import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;

import kr.co.linkhub.auth.Base64;
import kr.co.linkhub.auth.LinkhubException;
import kr.co.linkhub.auth.Token;
import kr.co.linkhub.auth.TokenBuilder;

public class KakaocertServiceImp implements KakaocertService {

    private static final String ServiceID = "BAROCERT";
    
    private static final String Auth_Static_URL = "https://static-auth.linkhub.co.kr";
    private static final String ServiceURL_REAL = "https://bc-api.linkhub.kr"; // TODO :: 나중에 바꿔야 함.
    private static final String ServiceURL_Static_REAL = "https://static-barocert.linkhub.co.kr";
    
    public static final int GCM_IV_LENGTH = 12;	
    public static final int GCM_TAG_LENGTH = 16;
    
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private final String APIVersion = "2.0";
    
    private String ServiceURL = null;
    private String AuthURL = null;
    private String ProxyIP = null;
    private Integer ProxyPort = null;

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
        isIPRestrictOnOff = true;
        useStaticIP = false;
        useLocalTimeYN = true;
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
        
        if(useStaticIP) 
        	return ServiceURL_Static_REAL;
        
        return ServiceURL_REAL;
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
            tokenBuilder = TokenBuilder.newInstance(getLinkID(), getSecretKey()).ServiceID(ServiceID).addScope("partner")
            			.useLocalTimeYN(useLocalTimeYN);

            if (AuthURL != null) {
                tokenBuilder.setServiceURL(AuthURL);
            } else {
            	if (useStaticIP)
            		tokenBuilder.setServiceURL(Auth_Static_URL);
            }
            
            if (ProxyIP != null && ProxyPort != null) {
                tokenBuilder.setProxyIP(ProxyIP);
                tokenBuilder.setProxyPort(ProxyPort);
            }

            tokenBuilder.addScope("401");
            tokenBuilder.addScope("402");
            tokenBuilder.addScope("403");
            tokenBuilder.addScope("404");
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
            } catch (ParseException e) {
            	StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				
				throw new BarocertException(-99999999, "GetSessionToken Parse Exception : " + errors.toString());
			}
        }

        if (expired) {
            if (tokenTable.containsKey(ClientCode))
                tokenTable.remove(ClientCode);

            try {

                if (isIPRestrictOnOff) {
                    token = getTokenbuilder().build("", ForwardIP);
                } else {
                    token = getTokenbuilder().build("", "*");
                }

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

        httpURLConnection.setRequestProperty("x-bc-version".toLowerCase(), APIVersion);
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

        httpURLConnection.setRequestProperty("x-lh-date".toLowerCase(), date);
        httpURLConnection.setRequestProperty("x-lh-version".toLowerCase(), APIVersion);
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
            signTarget += sha256Base64(btPostData) + "\n";
            signTarget += date + "\n";
            signTarget += APIVersion + "\n";

            String Signature = base64Encode(HMacSha256(base64Decode(getSecretKey()), signTarget.getBytes(Charset.forName("UTF-8"))));

            httpURLConnection.setRequestProperty("x-bc-auth", getLinkID() + " " + Signature);
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
    	byte[] iv = new byte[GCM_IV_LENGTH];
		secureRandom.nextBytes(iv);
		
		return iv;
	}
    
    @Override
    public String AES256Encrypt(String plainText) throws BarocertException {
    	byte[] cipherData = null;
    	ByteBuffer byteBuffer = null;
    	byte[] iv = GenerateRandomKeyByte();
    	
    	if (null == plainText || plainText.length() == 0)
            throw new BarocertException(-99999999, "AES256Encrypt plainText empty");
    	
		try {
			SecretKeySpec secureKey = new SecretKeySpec(base64Decode(_secretKey), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secureKey, new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv));
			cipherData = cipher.doFinal(plainText.getBytes(Charsets.UTF_8));
			
			byteBuffer = ByteBuffer.allocate(iv.length + cipherData.length);
			byteBuffer.put(iv);
			byteBuffer.put(cipherData);
		} catch (Exception e) {
			throw new BarocertException(-99999999, "AES256Encrypt Encrypt Error : ", e);
		}
		
		return BaseEncoding.base64().encode(byteBuffer.array());
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
                exception = new BarocertException(-99999999, "Fail to receive data from Server.", e);
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
    public ESResponse requestESign(String clientCode, ESRequest eSRequest, boolean isAppUseYN) throws BarocertException {

    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == eSRequest)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        
        eSRequest.setClientCode(clientCode);
        eSRequest.setAppUseYN(isAppUseYN);
        
    	return httppost("/KAKAO/ESign/Request", clientCode, toJsonString(eSRequest), ESResponse.class);
    }
    
    // 전자서명 요청(다건)
    @Override
    public ESMultiResponse requestMultiESign(String clientCode, ESMultiRequest bulkESRequest, boolean isAppUseYN) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == bulkESRequest)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
    	  
        bulkESRequest.setClientCode(clientCode);
        bulkESRequest.setAppUseYN(isAppUseYN);

        return httppost("/KAKAO/ESign/MultiRequest", clientCode, toJsonString(bulkESRequest), ESMultiResponse.class);
    }

    // 전자서명 상태확인(단건)
    @Override
    public ESStateResult getESignState(String clientCode, String receiptID) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESign/Status/" + clientCode + "/" + receiptID, clientCode, ESStateResult.class);
    }
    
    // 전자서명 상태확인(다건)
    @Override
	public MultiStateResult getMultiESignState(String clientCode, String receiptID) throws BarocertException {
    	
    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESign/MultiStatus/" + clientCode + "/" + receiptID, clientCode, MultiStateResult.class);
	}

    // 본인인증 서명검증(단건)
    @Override
    public ESVerifyResult verifyESign(String clientCode, String receiptID) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        ESVerifyRequest request = new ESVerifyRequest();
		request.setClientCode(clientCode);
		request.setReceiptID(receiptID);
        
        return httppost("/KAKAO/ESign/Verify", clientCode, toJsonString(request), ESVerifyResult.class);
    }
    
    // 본인인증 서명검증(다건)
    @Override
	public ESMultiVerifyResult multiVerifyESign(String clientCode, String receiptID) throws BarocertException {
		
    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        BulkESVerifyRequest request = new BulkESVerifyRequest();
		request.setClientCode(clientCode);
		request.setReceiptID(receiptID);
        
        return httppost("/KAKAO/ESign/MultiVerify", clientCode, toJsonString(request), ESMultiVerifyResult.class);
	}

    
    // 본인인증 요청
    @Override
    public VAResponse requestVerifyAuth(String clientCode, VARequest vARequest, boolean appUseYN) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == vARequest)
            throw new BarocertException(-99999999, "본인인증 요청정보가 입력되지 않았습니다.");
        
        vARequest.setClientCode(clientCode);
        vARequest.setAppUseYN(appUseYN);
        
        return httppost("/KAKAO/VerifyAuth/Request", clientCode, toJsonString(vARequest), VAResponse.class);
    }
    
    // 본인인증 상태확인
    @Override
    public VAStateResult getVerifyAuthState(String clientCode, String receiptID) throws BarocertException {

    	if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        return httpget("/KAKAO/VerifyAuth/Status/" + clientCode + "/" + receiptID, clientCode, VAStateResult.class);
    }
    
    // 본인인증 서명검증
    @Override
    public VAVerifyResult verifyAuth(String clientCode, String receiptID) throws BarocertException {
    	
        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        VAVerifyRequest request = new VAVerifyRequest();
        request.setClientCode(clientCode);
		request.setReceiptID(receiptID);
		
        return httppost("/KAKAO/VerifyAuth/Verify", clientCode, toJsonString(request), VAVerifyResult.class);
    }
    
    
    // 출금동의 요청
    @Override
    public CMSResponse requestCMS(String clientCode, CMSRequest cMSRequest, boolean appUseYN) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == cMSRequest)
            throw new BarocertException(-99999999, "자동이체 출금동의 요청정보가 입력되지 않았습니다.");
        
        cMSRequest.setClientCode(clientCode);
        cMSRequest.setAppUseYN(appUseYN);

        return httppost("/KAKAO/CMS/Request", clientCode, toJsonString(cMSRequest), CMSResponse.class);
    }
    
    // 출금동의 상태확인
    @Override
    public CMSStateResult getCMSState(String clientCode, String receiptID) throws BarocertException {

        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/CMS/Status/" + clientCode + "/" + receiptID, clientCode, CMSStateResult.class);
    }

    // 출금동의 서명검증
    @Override
    public CMSVerifyResult verifyCMS(String clientCode, String receiptID) throws BarocertException {
    	
        if (null == clientCode || clientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        VAVerifyRequest request = new VAVerifyRequest();
		request.setClientCode(clientCode);
		request.setReceiptID(receiptID);

        return httppost("/KAKAO/CMS/Verify", clientCode, toJsonString(request), CMSVerifyResult.class);
    }

}
