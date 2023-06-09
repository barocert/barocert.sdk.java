package com.barocert.kakaocert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.kakaocert.identity.Identity;
import com.barocert.kakaocert.identity.IdentityReceipt;
import com.barocert.kakaocert.identity.IdentityStatus;
import com.barocert.kakaocert.identity.IdentityResult;

public class TEST_Identity {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";

    private KakaocertService kakaocertService;

    public TEST_Identity() {
        KakaocertServiceImp service = new KakaocertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        service.setUseLocalTimeYN(true);
        kakaocertService = service;
    }

    // 본인인증 요청
    // https://developers.barocert.com/reference/kakao/java/identity/api#RequestIdentity
    @Test
    public void TEST_RequestIdentity() throws BarocertException {
        try {
            // 본인인증 요청 정보 객체
            Identity request = new Identity();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(kakaocertService.encrypt("01012341234"));
            // 수신자 성명 - 80자
            request.setReceiverName(kakaocertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(kakaocertService.encrypt("19700101"));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("인증요청 메시지 제목란");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);
            // 서명 원문 - 최대 2,800자 까지 입력가능
            request.setToken(kakaocertService.encrypt("본인인증요청토큰"));

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // App to App 방식 이용시, 호출할 URL
            // verifyAuthRequest.setReturnURL("https://www.kakaocert.com");

            IdentityReceipt result = kakaocertService.requestIdentity("023040000001", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 본인인증 상태확인
    // https://developers.barocert.com/reference/kakao/java/identity/api#GetIdentityStatus
    @Test
    public void TEST_GetIdentityStatus() throws BarocertException {
        try {
            IdentityStatus result = kakaocertService.getIdentityStatus("023040000001", "02307100230400000010000000000002");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("AuthCategory : " + result.getAuthCategory());
            System.out.println("ReturnURL : " + result.getReturnURL());
            System.out.println("tokenType : " + result.getTokenType());
            System.out.println("RequestDT : " + result.getRequestDT());
            System.out.println("ViewDT : " + result.getViewDT());
            System.out.println("CompleteDT : " + result.getCompleteDT());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("VerifyDT : " + result.getVerifyDT());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("AppUseYN : " + result.getAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 본인인증 서명검증
    // https://developers.barocert.com/reference/kakao/java/identity/api#VerifyIdentity
    @Test
    public void TEST_VerifyIdentity() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간(10분)이내에만 요청가능 합니다.
            IdentityResult result = kakaocertService.verifyIdentity("023040000001", "02307100230400000010000000000002");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("ci : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}