package com.barocert.passcert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.passcert.login.Login;
import com.barocert.passcert.login.LoginReceipt;
import com.barocert.passcert.login.LoginResult;
import com.barocert.passcert.login.LoginStatus;
import com.barocert.passcert.login.LoginVerify;

public class TEST_Login {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";

    private PasscertService passcertService;

    public TEST_Login() {
        PasscertServiceImp service = new PasscertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        service.setUseLocalTimeYN(true);
        passcertService = service;
    }

    /*
     * 패스 이용자에게 간편로그인을 요청합니다.
     * https://developers.barocert.com/reference/pass/java/login/api#RequestLogin
     */
    @Test
    public void TEST_RequestLogin() throws BarocertException {
        try {
            // 간편로그인 요청 정보 객체
            Login request = new Login();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(passcertService.encrypt("01012341234"));
            // 수신자 성명 - 최대 80자
            request.setReceiverName(passcertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(passcertService.encrypt("19700101"));

            // 간편로그인 메시지 제목 - 최대 40자
            request.setReqTitle("간편로그인 요청 메시지 제목");
            // 간편로그인 메시지 - 최대 500자
            request.setReqMessage(passcertService.encrypt("간편로그인 요청 메시지"));
            // 고객센터 연락처 - 최대 12자
            request.setCallCenterNum("1600-9854");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);
            // 서명 원문 - 최대 2,800자 까지 입력가능
            request.setToken(passcertService.encrypt("간편로그인 요청 원문"));
            
            // 사용자 동의 필요 여부
            request.setUserAgreementYN(true);
            // 사용자 정보 포함 여부
            request.setReceiverInfoYN(true);
            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Push 인증방식
            request.setAppUseYN(false);
            // ApptoApp 인증방식에서 사용
            // 통신사 유형('SKT', 'KT', 'LGU'), 대문자 입력(대소문자 구분)
            // request.setTelcoType("SKT");
            // ApptoApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            // request.setDeviceOSType("IOS");

            LoginReceipt result = passcertService.requestLogin("023040000001", request);

            // 접수아이디, 앱스킴, 앱다운로드URL 
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("MarketUrl : " + result.getMarketUrl());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 간편로그인 요청 후 반환받은 접수아이디로 진행 상태를 확인합니다.
     * 상태확인 함수는 간편로그인 요청 함수를 호출한 당일 23시 59분 59초까지만 호출 가능합니다.
     * 간편로그인 요청 함수를 호출한 당일 23시 59분 59초 이후 상태확인 함수를 호출할 경우 오류가 반환됩니다.
     * https://developers.barocert.com/reference/pass/java/login/api#GetLoginStatus
     */
    @Test
    public void TEST_GetLoginStatus() throws BarocertException {
        try {                                                                                               
            LoginStatus result = passcertService.getLoginStatus("023040000001", "02307100230400000010000000000009");

            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn()); // 단위: 초(s)
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("ReqMessage : " + result.getReqMessage());
            System.out.println("RequestDT : " + result.getRequestDT());
            System.out.println("CompleteDT : " + result.getCompleteDT());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("RejectDT : " + result.getRejectDT());
            System.out.println("TokenType : " + result.getTokenType());
            System.out.println("UserAgreementYN : " + result.getUserAgreementYN());
            System.out.println("ReceiverInfoYN : " + result.getReceiverInfoYN());
            System.out.println("TelcoType : " + result.getTelcoType()); // 통신사 유형('SKT', 'KT', 'LGU')
            System.out.println("DeviceOSType : " + result.getDeviceOSType()); // 모바일장비 유형('ANDROID', 'IOS')
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("AppUseYN : " + result.getAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 완료된 전자서명을 검증하고 전자서명값(signedData)을 반환 받습니다.
     * 검증 함수는 간편로그인 요청 함수를 호출한 당일 23시 59분 59초까지만 호출 가능합니다.
     * 간편로그인 요청 함수를 호출한 당일 23시 59분 59초 이후 검증 함수를 호출할 경우 오류가 반환됩니다.
     * https://developers.barocert.com/reference/pass/java/login/api#VerifyLogin
     */
    @Test
    public void TEST_VerifyLogin() throws BarocertException {
        try {
            // 검증 요청 정보 객체
            LoginVerify verify = new LoginVerify();
            // 검증 요청 휴대폰번호 - 11자 (하이픈 제외)
            verify.setReceiverHP(passcertService.encrypt("01012341234")); 
            // 검증 요청 성명 - 최대 80자
            verify.setReceiverName(passcertService.encrypt("홍길동")); 
            
            LoginResult result = passcertService.verifyLogin("023040000001", "02307100230400000010000000000009", verify);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4),미처리(5)
            System.out.println("ReceiverName : " + result.getReceiverName());
            System.out.println("ReceiverYear : " + result.getReceiverYear());
            System.out.println("ReceiverDay : " + result.getReceiverDay());
            System.out.println("ReceiverGender : " + result.getReceiverGender());
            System.out.println("ReceiverForeign : " + result.getReceiverForeign());
            System.out.println("ReceiverTelcoType : " + result.getReceiverTelcoType());
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("CI : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }
    
}