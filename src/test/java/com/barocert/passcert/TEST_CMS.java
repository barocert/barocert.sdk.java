package com.barocert.passcert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.passcert.cms.CMS;
import com.barocert.passcert.cms.CMSReceipt;
import com.barocert.passcert.cms.CMSResult;
import com.barocert.passcert.cms.CMSStatus;
import com.barocert.passcert.cms.CMSVerify;

public class TEST_CMS {

    private final String testLinkID = "TESTER_WS";
    private final String testSecretKey = "a0NVsYKdOI11fBREgTrKRVRaO8PZJi5XroiIKG+WQZY=";

    private PasscertService passcertService;

    public TEST_CMS() {
        PasscertServiceImp service = new PasscertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        service.setUseLocalTimeYN(true);
        passcertService = service;
    }

    // 출금동의 요청
    @Test
    public void TEST_RequestCMS() {
        try {
            // 출금동의 요청 객체
            CMS request = new CMS();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(passcertService.encrypt("01012341234"));
            // 수신자 성명 - 80자
            request.setReceiverName(passcertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(passcertService.encrypt("19700101"));
            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("패스써트 출금동의 인증요청 타이틀");
            // 인증요청 메시지
            request.setReqMessage(passcertService.encrypt("패스써트 출금동의 인증요청 내용"));

            // 고객센터 연락처
            request.setCallCenterNum("1600-9854");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // 사용자 동의 필요 여부
            request.setUserAgreementYN(true);
            // 사용자 정보 포함 여부
            request.setReceiverInfoYN(true);

            // 출금은행명 - 최대 100자
            request.setBankName(passcertService.encrypt("국민은행"));
            // 출금계좌번호 - 최대 32자
            request.setBankAccountNum(passcertService.encrypt("9-****-5117-58"));
            // 출금계좌 예금주명 - 최대 100자
            request.setBankAccountName(passcertService.encrypt("홍길동"));
            // 출금액
            request.setBankWithdraw(passcertService.encrypt("1,000,000원"));
            // 출금유형 
            // CMS - 출금동의, OPEN_BANK - 오픈뱅킹
            request.setBankServiceType(passcertService.encrypt("OPEN_BANK"));

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);
            // ApptoApp 인증방식에서 사용
            // 통신사 유형('SKT', 'KT', 'LG'), 대문자 입력(대소문자 구분)
            request.setTelcoType("SKT");
            // ApptoApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            request.setDeviceOSType("IOS");
            
            request.setUseTssYN(false);

            CMSReceipt result = passcertService.requestCMS("023060000044", request);

            System.out.println("ReceiptID : " + result.getReceiptId());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 출금동의 상태확인
    @Test
    public void TEST_GetCMSStatus() throws BarocertException {
        try {
            CMSStatus result = passcertService.getCMSStatus("023060000044", "02306290230600000440000000000019");

            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("ReqMessage : " + result.getReqMessage());
            System.out.println("RequestDT : " + result.getRequestDT());
            System.out.println("ViewDT : " + result.getViewDT());
            System.out.println("CompleteDT : " + result.getCompleteDT());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("RejectDT : " + result.getRejectDT());
            System.out.println("TokenType : " + result.getTokenType());
            System.out.println("UserAgreementYN : " + result.getUserAgreementYN());
            System.out.println("ReceiverInfoYN : " + result.getReceiverInfoYN());
            System.out.println("TelcoType : " + result.getTelcoType());
            System.out.println("DeviceOSType : " + result.getDeviceOSType());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("AppUseYN : " + result.getAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 출금동의 검증
    @Test
    public void TEST_VerifyCMS() throws BarocertException {
        try {
             // 검증 요청 정보 객체
            CMSVerify verify = new CMSVerify(); 
            // 검증 요청자 휴대폰번호 - 11자 (하이픈 제외)
            verify.setReceiverHP(passcertService.encrypt("01012341234")); 
            // 검증 요청자 성명 - 80자
            verify.setReceiverName(passcertService.encrypt("홍길동"));

            CMSResult result = passcertService.verifyCMS("023060000044", "02306290230600000440000000000012", verify);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ReceiverName : " + result.getReceiverName());
            System.out.println("ReceiverBirthday : " + result.getReceiverBirthday());
            System.out.println("ReceiverHP : " + result.getReceiverHP());
            System.out.println("ReceiverGender : " + result.getReceiverGender());
            System.out.println("TelcoType : " + result.getTelcoType());
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("CI : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}