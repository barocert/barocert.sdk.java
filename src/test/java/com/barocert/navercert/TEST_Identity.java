package com.barocert.navercert;

import com.barocert.BarocertException;
import com.barocert.navercert.identity.Identity;
import com.barocert.navercert.identity.IdentityReceipt;
import com.barocert.navercert.identity.IdentityResult;
import com.barocert.navercert.identity.IdentityStatus;
import org.junit.Test;

public class TEST_Identity {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";
    private NavercertService navercertService;
    private String clientCode = "023060000088";

    public TEST_Identity() {
        NavercertServiceImp service = new NavercertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(false);
        service.setUseStaticIP(false);
        service.setUseLocalTimeYN(true);
        navercertService = service;
    }

    // 본인인증 요청
    // https://developers.barocert.com/reference/naver/java/identity/api#RequestIdentity
    @Test
    public void TEST_RequestIdentity() throws BarocertException {
        try {
            // 본인인증 요청 정보 객체
            Identity request = new Identity();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(navercertService.encrypt("01054437896"));
            // 수신자 성명 - 80자
            request.setReceiverName(navercertService.encrypt("최상혁"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(navercertService.encrypt("19880301"));

            // 고객센터 연락처 - 최대 12자
            request.setCallCenterNum("1600-9854");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // AppToApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            // request.setDeviceOSType("ANDROID");

            // AppToApp 방식 이용시, 호출할 URL
            // request.setReturnURL("navercert://Identity");

            IdentityReceipt result = navercertService.requestIdentity(clientCode, request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("MarketUrl : " + result.getMarketUrl());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 본인인증 상태확인
    // https://developers.barocert.com/reference/naver/java/identity/api#GetIdentityStatus
    @Test
    public void TEST_GetIdentityStatus() throws BarocertException {
        try {
            IdentityStatus result = navercertService.getIdentityStatus(clientCode, "02309050230600000880000000000010");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReturnURL : " + result.getReturnURL());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("AppUseYN : " + result.getAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 본인인증 서명검증
    // https://developers.barocert.com/reference/naver/java/identity/api#VerifyIdentity
    @Test
    public void TEST_VerifyIdentity() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간 이내에만 요청가능 합니다.
            IdentityResult result = navercertService.verifyIdentity(clientCode, "02309050230600000880000000000010");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("Ci : " + result.getCi());
            System.out.println("ReceiverName : " + result.getReceiverName());
            System.out.println("ReceiverDay : " + result.getReceiverDay());
            System.out.println("ReceiverYear : " + result.getReceiverYear());
            System.out.println("ReceiverHP : " + result.getReceiverHP());
            System.out.println("ReceiverGender : " + result.getReceiverGender());
            System.out.println("ReceiverEmail : " + result.getReceiverEmail());
            System.out.println("ReceiverForeign : " + result.getReceiverForeign());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}