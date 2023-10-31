package com.barocert.passcert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barocert.BarocertException;
import com.barocert.ServiceImpBase;
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
import com.barocert.passcert.login.Login;
import com.barocert.passcert.login.LoginReceipt;
import com.barocert.passcert.login.LoginResult;
import com.barocert.passcert.login.LoginStatus;
import com.barocert.passcert.login.LoginVerify;
import com.barocert.passcert.sign.Sign;
import com.barocert.passcert.sign.SignReceipt;
import com.barocert.passcert.sign.SignResult;
import com.barocert.passcert.sign.SignStatus;
import com.barocert.passcert.sign.SignVerify;
import kr.co.linkhub.auth.Token;

public class PasscertServiceImp extends ServiceImpBase implements PasscertService {
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
        return Arrays.asList("441", "442", "443", "444");
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
    public IdentityResult verifyIdentity(String clientCode, String receiptID, IdentityVerify identityVerify) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");
        if (identityVerify == null) throw new BarocertException(-99999999, "본인인증 검증 요청 정보가 입력되지 않았습니다.");
        if (identityVerify.getReceiverHP() == null) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (identityVerify.getReceiverName() == null) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");

        String postData = toJsonString(identityVerify);

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
    public SignResult verifySign(String clientCode, String receiptID, SignVerify signVerify) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");
        if (signVerify == null) throw new BarocertException(-99999999, "전자서명 검증 요청 정보가 입력되지 않았습니다.");
        if (signVerify.getReceiverHP() == null) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (signVerify.getReceiverName() == null) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");

        String postData = toJsonString(signVerify);

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
    public CMSResult verifyCMS(String clientCode, String receiptID, CMSVerify cmsVerify) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");
        if (cmsVerify == null) throw new BarocertException(-99999999, "출금동의 검증 요청 정보가 입력되지 않았습니다.");
        if (cmsVerify.getReceiverHP() == null) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (cmsVerify.getReceiverName() == null) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");

        String postData = toJsonString(cmsVerify);

        return httpPost("/PASS/CMS/" + clientCode + "/" + receiptID, postData, CMSResult.class);
    }

     // 간편로그인 요청
    @Override
    public LoginReceipt requestLogin(String clientCode, Login login) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (login == null) throw new BarocertException(-99999999, "간편로그인 서명요청 정보가 입력되지 않았습니다.");
        if (isNullOrEmpty(login.getReceiverHP())) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (isNullOrEmpty(login.getReceiverName())) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");
        if (isNullOrEmpty(login.getReqTitle())) throw new BarocertException(-99999999, "인증요청 메시지 제목이 입력되지 않았습니다.");
        if (isNullOrEmpty(login.getCallCenterNum())) throw new BarocertException(-99999999, "고객센터 연락처가 입력되지 않았습니다.");
        if (login.getExpireIn() == null) throw new BarocertException(-99999999, "만료시간이 입력되지 않았습니다.");
        if (isNullOrEmpty(login.getToken())) throw new BarocertException(-99999999, "토큰 원문이 입력되지 않았습니다.");

        String postData = toJsonString(login);

        return httpPost("/PASS/Login/" + clientCode, postData, LoginReceipt.class);
    }

    // 간편로그인 상태확인
    @Override
    public LoginStatus getLoginStatus(String clientCode, String receiptID) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");

        return httpGet("/PASS/Login/" + clientCode + "/" + receiptID, LoginStatus.class);
    }

    // 간편로그인 검증
    @Override
    public LoginResult verifyLogin(String clientCode, String receiptID, LoginVerify loginVerify) throws BarocertException {

        // 필수 값 체크.
        if (isNullOrEmpty(clientCode)) throw new BarocertException(-99999999, "이용기관코드가 입력되지 않았습니다.");
        if (false == clientCode.matches("^\\d+$")) throw new BarocertException(-99999999, "이용기관코드는 숫자만 입력할 수 있습니다.");
        if (clientCode.length() != 12) throw new BarocertException(-99999999, "이용기관코드는 12자 입니다.");
        if (isNullOrEmpty(receiptID)) throw new BarocertException(-99999999, "접수아이디가 입력되지 않았습니다.");
        if (false == receiptID.matches("^\\d+$")) throw new BarocertException(-99999999, "접수아이디는 숫자만 입력할 수 있습니다.");
        if (receiptID.length() != 32) throw new BarocertException(-99999999, "접수아이디는 32자 입니다.");
        if (loginVerify == null) throw new BarocertException(-99999999, "간편로그인 검증 정보가 입력되지 않았습니다.");
        if (loginVerify.getReceiverHP() == null) throw new BarocertException(-99999999, "수신자 휴대폰번호가 입력되지 않았습니다.");
        if (loginVerify.getReceiverName() == null) throw new BarocertException(-99999999, "수신자 성명이 입력되지 않았습니다.");

        String postData = toJsonString(loginVerify);

        return httpPost("/PASS/Login/" + clientCode + "/" + receiptID, postData, LoginResult.class);
    }
    
    private boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}