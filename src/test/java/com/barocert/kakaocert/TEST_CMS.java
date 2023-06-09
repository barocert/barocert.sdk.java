package com.barocert.kakaocert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.kakaocert.cms.CMS;
import com.barocert.kakaocert.cms.CMSReceipt;
import com.barocert.kakaocert.cms.CMSStatus;
import com.barocert.kakaocert.cms.CMSResult;

public class TEST_CMS {

    private final String testLinkID = "LINKHUB_BC";
    private final String testSecretKey = "npCAl0sHPpJqlvMbrcBmNagrxkQ74w9Sl0A+M++kMCE=";

    private KakaocertService kakaocertService;

    public TEST_CMS() {
        KakaocertServiceImp service = new KakaocertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        service.setUseLocalTimeYN(true);
        kakaocertService = service;
    }

    // 출금동의 요청
    @Test
    public void TEST_RequestCMS() {
        try {
            // 출금동의 요청 객체
            CMS request = new CMS();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(kakaocertService.encrypt("01054437896"));
            // 수신자 성명 - 80자
            request.setReceiverName(kakaocertService.encrypt("최상혁"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(kakaocertService.encrypt("19880301"));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("자동이체 출금동의");

            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // 청구기관명 - 최대 100자
            request.setRequestCorp(kakaocertService.encrypt("주식회사 링크허브"));
            // 출금은행명 - 최대 100자
            request.setBankName(kakaocertService.encrypt("국민은행"));
            // 출금계좌번호 - 최대 32자
            request.setBankAccountNum(kakaocertService.encrypt("9-****-5117-58"));
            // 출금계좌 예금주명 - 최대 100자
            request.setBankAccountName(kakaocertService.encrypt("홍길동"));
            // 출금계좌 예금주 생년월일 - 8자
            request.setBankAccountBirthday(kakaocertService.encrypt("19930101"));
            // 출금유형
            // CMS - 출금동의용, FIRM - 펌뱅킹, GIRO - 지로용
            request.setBankServiceType(kakaocertService.encrypt("CMS")); // CMS, FIRM, GIRO

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // App to App 방식 이용시, 에러시 호출할 URL
            // request.setReturnURL("https://www.kakaocert.com");

            CMSReceipt result = kakaocertService.requestCMS("023030000004", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
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
            CMSStatus result = kakaocertService.getCMSStatus("023030000004", "02304140230300000040000000000002");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("AuthCategory : " + result.getAuthCategory());
            System.out.println("ReturnURL : " + result.getReturnURL());
            System.out.println("TokenType : " + result.getTokenType());
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

    // 출금동의 검증
    @Test
    public void TEST_VerifyCMS() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간(10분)이내에만 요청가능 합니다.
            CMSResult result = kakaocertService.verifyCMS("023030000004", "02304140230300000040000000000002");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("Ci : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}
