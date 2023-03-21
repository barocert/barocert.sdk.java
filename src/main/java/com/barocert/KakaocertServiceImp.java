package com.barocert;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
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

import com.barocert.kakaocert.cms.RequestCMS;
import com.barocert.kakaocert.cms.ResultCMS;
import com.barocert.kakaocert.cms.ResultCMSState;
import com.barocert.kakaocert.cms.VerifyCMSResult;
import com.barocert.kakaocert.esign.BulkRequestESign;
import com.barocert.kakaocert.esign.BulkResultESignState;
import com.barocert.kakaocert.esign.BulkVerifyESignResult;
import com.barocert.kakaocert.esign.RequestESign;
import com.barocert.kakaocert.esign.RequestVerify;
import com.barocert.kakaocert.esign.ResultESign;
import com.barocert.kakaocert.esign.ResultESignState;
import com.barocert.kakaocert.esign.VerifyEsignResult;
import com.barocert.kakaocert.verifyauth.RequestVerifyAuth;
import com.barocert.kakaocert.verifyauth.ReqVerifyAuthResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthStateResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthResult;
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
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private final String APIVersion = "2.0";
    private String ServiceURL = null;
    private String AuthURL = null;

    private TokenBuilder tokenBuilder;

    private boolean isIPRestrictOnOff;
    private boolean useStaticIP;
    private boolean useLocalTimeYN;
    private String _linkID;
    private String _secretKey;
    private Gson _gsonParser = new Gson();
    
	public static final int GCM_IV_LENGTH = 12;	
	public static final int GCM_TAG_LENGTH = 16;
	
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

    protected class ReceiptResponse {
        public String receiptId;
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
     * @param url
     * @param CorpNum
     * @param PostData
     * @param UserID
     * @param clazz
     * @return returned object
     * @throws BarocertException
     */
    protected <T> T httppost(String url, String CorpNum, String PostData, String UserID, Class<T> clazz) throws BarocertException {
        return httppost(url, CorpNum, PostData, UserID, null, clazz);
    }

    /**
     * 
     * @param url
     * @param CorpNum
     * @param PostData
     * @param UserID
     * @param Action
     * @param clazz
     * @return returned object
     * @throws BarocertException
     */
    protected <T> T httppost(String url, String CorpNum, String PostData, String UserID, String Action, Class<T> clazz) throws BarocertException {
        return httppost(url, CorpNum, PostData, UserID, Action, clazz, null);
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

    /**
     * 
     * @param url
     * @param CorpNum
     * @param PostData
     * @param UserID
     * @param Action
     * @param clazz
     * @param ContentType
     * @return
     * @throws BarocertException
     */
    protected <T> T httppost(String url, String CorpNum, String PostData, String UserID, String Action, Class<T> clazz, String ContentType)
            throws BarocertException {
        HttpURLConnection httpURLConnection;
        try {
            URL uri = new URL(getServiceURL() + url);
            httpURLConnection = (HttpURLConnection) uri.openConnection();
        } catch (Exception e) {
            throw new BarocertException(-99999999, "Kakaocert API 서버 접속 실패", e);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = format.format(new Date());

        if (CorpNum != null && CorpNum.isEmpty() == false) {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken(CorpNum, null));
        }

        httpURLConnection.setRequestProperty("x-lh-date".toLowerCase(), date);

        httpURLConnection.setRequestProperty("x-lh-version".toLowerCase(), APIVersion);

        if (ContentType != null && ContentType.isEmpty() == false) {
            httpURLConnection.setRequestProperty("Content-Type", ContentType);
        } else {
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        }

        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");

        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e1) {
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
                throw new BarocertException(-99999999, "Fail to POST data to Server.", e);
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

    /**
     * 
     * @param url
     * @param CorpNum
     * @param UserID
     * @param clazz
     * @return returned object
     * @throws BarocertException
     */
    protected <T> T httpget(String url, String CorpNum, String UserID, Class<T> clazz) throws BarocertException {
        HttpURLConnection httpURLConnection;
        try {
            URL uri = new URL(getServiceURL() + url);
            httpURLConnection = (HttpURLConnection) uri.openConnection();
        } catch (Exception e) {
            throw new BarocertException(-99999999, "Kakaocert API 서버 접속 실패", e);
        }

        if (CorpNum != null && CorpNum.isEmpty() == false) {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getSessionToken(CorpNum, null));
        }

        httpURLConnection.setRequestProperty("x-pb-version".toLowerCase(), APIVersion);

        if (UserID != null && UserID.isEmpty() == false) {
            httpURLConnection.setRequestProperty("x-pb-userid", UserID);
        }

        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");

        String Result = parseResponse(httpURLConnection);

        return fromJsonString(Result, clazz);
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

    protected class UnitCostResponse {

        public float unitCost;

    }

    protected class UploadFile {
        public UploadFile() {
        }

        public String fieldName;
        public String fileName;
        public InputStream fileData;
    }

    protected class URLResponse {
        public String url;
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
            throw new BarocertException(-99999999, "plainText empty");
    	
		try {
			SecretKeySpec secureKey = new SecretKeySpec(BaseEncoding.base64().decode(_secretKey), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secureKey, new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv));
			cipherData = cipher.doFinal(plainText.getBytes(Charsets.UTF_8));
			
			byteBuffer = ByteBuffer.allocate(iv.length + cipherData.length);
			byteBuffer.put(iv);
			byteBuffer.put(cipherData);
		} catch (Exception e) {
			throw new BarocertException(-99999999, "Encrypt Error");
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
    public ResultESign requestESign(String ClientCode, RequestESign esignRequest, boolean appUseYN) throws BarocertException {

    	ResultESign response = null;
    	
    	if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == esignRequest)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        
        esignRequest.setClientCode(ClientCode);
        esignRequest.setAppUseYN(appUseYN);
        
        String PostData = toJsonString(esignRequest);
        
		response = httppost("/KAKAO/ESign/Request", ClientCode, PostData, null, ResultESign.class);
    	return response;
    }
    
    // 전자서명 요청(다건)
    @Override
    public ResultESign bulkRequestESign(String ClientCode, BulkRequestESign esignRequest, boolean appUseYN) throws BarocertException {

        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == esignRequest)
            throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
    	  
        esignRequest.setClientCode(ClientCode);
        esignRequest.setAppUseYN(appUseYN);

        String PostData = toJsonString(esignRequest);

        ResultESign response = httppost("/KAKAO/ESign/BulkRequest", ClientCode, PostData, null, ResultESign.class);
        return response;
    }

    // 전자서명 상태확인(단건)
    @Override
    public ResultESignState getESignState(String ClientCode, String receiptID) throws BarocertException {

        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESign/Status/" + ClientCode + "/" + receiptID, ClientCode, null, ResultESignState.class);
    }
    
    // 전자서명 상태확인(다건)
    @Override
	public BulkResultESignState getBulkESignState(String ClientCode, String receiptID) throws BarocertException {
    	
    	if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/ESign/BulkStatus/" + ClientCode + "/" + receiptID, ClientCode, null, BulkResultESignState.class);
	}

    // 본인인증 서명검증(단건)
    @Override
    public VerifyEsignResult verifyESign(String ClientCode, String receiptID) throws BarocertException {

        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        RequestVerify request = new RequestVerify();
		request.setClientCode(ClientCode);
		request.setReceiptID(receiptID);
        
        String PostData = toJsonString(request);
        
        VerifyEsignResult response = httppost("/KAKAO/ESign/Verify", ClientCode, PostData, null, VerifyEsignResult.class);

        return response;
    }
    
    // 본인인증 서명검증(다건)
    @Override
	public BulkVerifyESignResult bulkVerifyESign(String ClientCode, String receiptID) throws BarocertException {
		
    	if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        RequestVerify request = new RequestVerify();
		request.setClientCode(ClientCode);
		request.setReceiptID(receiptID);
        
        String PostData = toJsonString(request);
        
        BulkVerifyESignResult response = httppost("/KAKAO/ESign/BulkVerify", ClientCode, PostData, null, BulkVerifyESignResult.class);

        return response;
	}

    
    // 본인인증 요청
    @Override
    public ReqVerifyAuthResult requestVerifyAuth(String ClientCode, RequestVerifyAuth verifyAuthRequest, boolean appUseYN) throws BarocertException {

        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == verifyAuthRequest)
            throw new BarocertException(-99999999, "본인인증 요청정보가 입력되지 않았습니다.");
        
        verifyAuthRequest.setClientCode(ClientCode);
        verifyAuthRequest.setAppUseYN(appUseYN);
        
        String PostData = toJsonString(verifyAuthRequest);

        ReqVerifyAuthResult response = httppost("/KAKAO/VerifyAuth/Request", ClientCode, PostData, null, ReqVerifyAuthResult.class);

        return response;
    }
    
    // 본인인증 상태확인
    @Override
    public VerifyAuthStateResult getVerifyAuthState(String ClientCode, String receiptID) throws BarocertException {

    	if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        return httpget("/KAKAO/VerifyAuth/Status/" + ClientCode + "/" + receiptID, ClientCode, null, VerifyAuthStateResult.class);
    }
    
    // 본인인증 서명검증
    @Override
    public VerifyAuthResult verifyAuth(String ClientCode, String receiptID) throws BarocertException {
    	
        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        RequestVerify request = new RequestVerify();
        request.setClientCode(ClientCode);
		request.setReceiptID(receiptID);
		
		String PostData = toJsonString(request);
		
		VerifyAuthResult response = httppost("/KAKAO/VerifyAuth/Verify", ClientCode, PostData, null, VerifyAuthResult.class);

        return response;
    }
    
    
    // 출금동의 요청
    @Override
    public ResultCMS requestCMS(String ClientCode, RequestCMS cmsRequest, boolean appUseYN) throws BarocertException {

        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == cmsRequest)
            throw new BarocertException(-99999999, "자동이체 출금동의 요청정보가 입력되지 않았습니다.");
        
        cmsRequest.setClientCode(ClientCode);
        cmsRequest.setAppUseYN(appUseYN);

        String PostData = toJsonString(cmsRequest);

        ResultCMS response = httppost("/KAKAO/CMS/Request", ClientCode, PostData, null, ResultCMS.class);

        return response;
    }
    
    // 출금동의 상태확인
    @Override
    public ResultCMSState getCMSState(String ClientCode, String receiptID) throws BarocertException {

        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");

        return httpget("/KAKAO/CMS/Status/" + ClientCode + "/" + receiptID, ClientCode, null, ResultCMSState.class);
    }

    // 출금동의 서명검증
    @Override
    public VerifyCMSResult verifyCMS(String ClientCode, String receiptID) throws BarocertException {
    	
        if (null == ClientCode || ClientCode.length() == 0)
            throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (null == receiptID || receiptID.length() == 0)
            throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        
        RequestVerify request = new RequestVerify();
		request.setClientCode(ClientCode);
		request.setReceiptID(receiptID);
        
        String PostData = toJsonString(request);
        
        VerifyCMSResult response = httppost("/KAKAO/CMS/Verify", ClientCode, PostData, null, VerifyCMSResult.class);

        return response;
    }
    
}
