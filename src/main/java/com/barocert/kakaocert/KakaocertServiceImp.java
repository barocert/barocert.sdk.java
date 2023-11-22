package com.barocert.kakaocert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barocert.BarocertException;
import com.barocert.ServiceImpBase;
import com.barocert.kakaocert.cms.CMS;
import com.barocert.kakaocert.cms.CMSReceipt;
import com.barocert.kakaocert.cms.CMSStatus;
import com.barocert.kakaocert.cms.CMSResult;
import com.barocert.kakaocert.identity.Identity;
import com.barocert.kakaocert.identity.IdentityReceipt;
import com.barocert.kakaocert.identity.IdentityStatus;
import com.barocert.kakaocert.login.LoginResult;
import com.barocert.kakaocert.identity.IdentityResult;
import com.barocert.kakaocert.sign.MultiSignTokens;
import com.barocert.kakaocert.sign.MultiSign;
import com.barocert.kakaocert.sign.Sign;
import com.barocert.kakaocert.sign.MultiSignReceipt;
import com.barocert.kakaocert.sign.MultiSignStatus;
import com.barocert.kakaocert.sign.SignReceipt;
import com.barocert.kakaocert.sign.SignStatus;
import com.barocert.kakaocert.sign.MultiSignResult;
import com.barocert.kakaocert.sign.SignResult;
import kr.co.linkhub.auth.Token;

public class KakaocertServiceImp extends ServiceImpBase implements KakaocertService {

    private static final Map<String, Token> tokenTable = new HashMap<String, Token>();

    @Override
    protected Token findToken(String key) {
        if (tokenTable.containsKey(key)) return tokenTable.get(key);
        return null;
    }

    @Override
    protected boolean removeToken(String key) {
        if (tokenTable.containsKey(key)){
            tokenTable.remove(key);
            return true;
        }
        return false;
    }

    @Override
    protected Token putToken(String key, Token token) {
        tokenTable.put(key, token);
        return token;
    }

    @Override
    protected List<String> getScopes() {
        return Arrays.asList("401", "402", "403", "404", "405");
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
        if (identity.getExpireIn() == null) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullOrEmpty(identity.getToken())) throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");

        String postData = toJsonString(identity);

        return httpPost("/KAKAO/Identity/" + clientCode, postData, IdentityReceipt.class);
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

        return httpGet("/KAKAO/Identity/" + clientCode + "/" + receiptID, IdentityStatus.class);
    }

    // 본인인증 서명검증
    @Override
    public IdentityResult verifyIdentity(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        String postData = toJsonString("");

        return httpPost("/KAKAO/Identity/" + clientCode + "/" + receiptID, postData, IdentityResult.class);
    }

    // 전자서명 서명요청(단건)
    @Override
    public SignReceipt requestSign(String clientCode, Sign sign)
            throws BarocertException {

        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (sign == null) throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getReceiverHP())) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getReceiverName())) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getReceiverBirthday())) throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getSignTitle())) throw new BarocertException(-99999999, "서명 요청 제목이 입력되지 않았습니다.");
        if (sign.getExpireIn() == null) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getToken())) throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");
        if (isNullOrEmpty(sign.getTokenType())) throw new BarocertException(-99999999, "원문 유형이 입력되지 않았습니다.");

        String postData = toJsonString(sign);

        return httpPost("/KAKAO/Sign/" + clientCode, postData, SignReceipt.class);
    }

    // 전자서명 상태확인(단건)
    @Override
    public SignStatus getSignStatus(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        return httpGet("/KAKAO/Sign/" + clientCode + "/" + receiptID, SignStatus.class);
    }

    // 전자서명 서명검증(단건)
    @Override
    public SignResult verifySign(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        String postData = toJsonString("");

        return httpPost("/KAKAO/Sign/" + clientCode + "/" + receiptID, postData, SignResult.class);
    }

    // 전자서명 서명요청(복수)
    @Override
    public MultiSignReceipt requestMultiSign(String clientCode, MultiSign multiSign)
            throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (multiSign == null) throw new BarocertException(-99999999, "전자서명 요청정보가 입력되지 않았습니다.");
        if (isNullOrEmpty(multiSign.getReceiverHP())) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(multiSign.getReceiverName()))throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (isNullOrEmpty(multiSign.getReceiverBirthday())) throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (isNullOrEmpty(multiSign.getReqTitle())) throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (multiSign.getExpireIn() == null ) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullorEmptyTitle(multiSign.getTokens())) throw new BarocertException(-99999999, "서명 요청 제목이 입력되지 않았습니다.");
        if (isNullorEmptyToken(multiSign.getTokens())) throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");
        if (isNullOrEmpty(multiSign.getTokenType())) throw new BarocertException(-99999999, "원문 유형이 입력되지 않았습니다.");

        String postData = toJsonString(multiSign);

        return httpPost("/KAKAO/MultiSign/" + clientCode, postData, MultiSignReceipt.class);
    }

    // 전자서명 상태확인(복수)
    @Override
    public MultiSignStatus getMultiSignStatus(String clientCode, String receiptID)
            throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        return httpGet("/KAKAO/MultiSign/" + clientCode + "/" + receiptID, MultiSignStatus.class);
    }

    // 전자서명 서명검증(복수)
    @Override
    public MultiSignResult verifyMultiSign(String clientCode, String receiptID)
            throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        String postData = toJsonString("");

        return httpPost("/KAKAO/MultiSign/" + clientCode + "/" + receiptID, postData, MultiSignResult.class);
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
        if (isNullOrEmpty(cms.getBankAccountBirthday())) throw new BarocertException(-99999999, "생년월일이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getReqTitle())) throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (cms.getExpireIn() == null) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getRequestCorp())) throw new BarocertException(-99999999, "청구기관명이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankName())) throw new BarocertException(-99999999, "은행명이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankAccountNum())) throw new BarocertException(-99999999, "계좌번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankAccountName())) throw new BarocertException(-99999999, "예금주명이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankAccountBirthday()))throw new BarocertException(-99999999, "예금주 생년월일이 입력되지 않았습니다.");
        if (isNullOrEmpty(cms.getBankServiceType())) throw new BarocertException(-99999999, "출금 유형이 입력되지 않았습니다.");

        String postData = toJsonString(cms);

        return httpPost("/KAKAO/CMS/" + clientCode, postData, CMSReceipt.class);
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

        return httpGet("/KAKAO/CMS/" + clientCode + "/" + receiptID, CMSStatus.class);
    }

    // 출금동의 서명검증
    @Override
    public CMSResult verifyCMS(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        String postData = toJsonString("");

        return httpPost("/KAKAO/CMS/" + clientCode + "/" + receiptID, postData, CMSResult.class);
    }
    
    // 간편로그인 서명검증
    @Override
    public LoginResult verifyLogin(String clientCode, String txID) throws BarocertException {
        
        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(txID)) throw new BarocertException(-99999999, "txID가 입력되지 않았습니다.");

        String postData = toJsonString("");

        return httpPost("/KAKAO/Login/" + clientCode + "/" + txID, postData, LoginResult.class);
    }

    private boolean isNullOrEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}

    private boolean isNullorEmptyTitle(List<MultiSignTokens> list){
        if(list == null) return true;
        if(list.isEmpty()) return true;
        for(MultiSignTokens multiSignToken : list) {
            if(multiSignToken == null) return true;
            if(isNullOrEmpty(multiSignToken.getSignTitle())) return true;
        }
        return false;
    }
    
    private boolean isNullorEmptyToken(List<MultiSignTokens> list){
        if(list == null) return true;
        if(list.isEmpty()) return true;
        for(MultiSignTokens multiSignToken : list) {
            if(multiSignToken == null) return true;
            if(isNullOrEmpty(multiSignToken.getToken())) return true;
        }
        return false;
    }

    
    
}