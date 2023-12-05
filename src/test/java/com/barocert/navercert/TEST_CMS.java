package com.barocert.navercert;

import com.barocert.BarocertException;
import com.barocert.navercert.cms.CMS;
import com.barocert.navercert.cms.CMSReceipt;
import com.barocert.navercert.cms.CMSResult;
import com.barocert.navercert.cms.CMSStatus;
import org.junit.Test;

public class TEST_CMS {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";
    private NavercertService navercertService;
    private String clientCode = "023090000021";

    public TEST_CMS() {
        NavercertServiceImp service = new NavercertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(false);
        service.setUseStaticIP(false);
        navercertService = service;
    }

    /*
     * 네이버 이용자에게 자동이체 출금동의를 요청합니다.
     * https://developers.barocert.com/reference/naver/java/cms/api#RequestCMS
     */
    @Test
    public void TEST_RequestCMS() throws BarocertException {
        try {
            // 출금동의 요청 정보 객체
            CMS request = new CMS();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(navercertService.encrypt("01012341234"));
            // 수신자 성명 - 80자
            request.setReceiverName(navercertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(navercertService.encrypt("19700101"));

            // 인증요청 메시지 제목
            request.setReqTitle("출금동의 요청 메시지 제목");
            // 인증요청 메시지
            request.setReqMessage(navercertService.encrypt("출금동의 요청 메시지"));
            // 고객센터 연락처 - 최대 12자
            request.setCallCenterNum("1600-9854");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // 청구기관명
            request.setRequestCorp(navercertService.encrypt("청구기관"));
            // 출금은행명
            request.setBankName(navercertService.encrypt("출금은행"));
            // 출금계좌번호
            request.setBankAccountNum(navercertService.encrypt("123-456-7890"));
            // 출금계좌 예금주명
            request.setBankAccountName(navercertService.encrypt("홍길동"));
            // 출금계좌 예금주 생년월일
            request.setBankAccountBirthday(navercertService.encrypt("19700101"));

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // AppToApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            // request.setDeviceOSType("ANDROID");

            // AppToApp 방식 이용시, 호출할 URL
            // "http", "https"등의 웹프로토콜 사용 불가
            // request.setReturnURL("navercert://cms");

            CMSReceipt result = navercertService.requestCMS(clientCode, request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("MarketUrl : " + result.getMarketUrl());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 자동이체 출금동의 요청 후 반환받은 접수아이디로 인증 진행 상태를 확인합니다.
     * https://developers.barocert.com/reference/naver/java/cms/api#GetCMSStatus
     */
    @Test
    public void TEST_GetCMSStatus() throws BarocertException {
        try {
            CMSStatus result = navercertService.getCMSStatus(clientCode, "02312050230900000210000000000016");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireDT : " + result.getExpireDT());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 완료된 전자서명을 검증하고 전자서명값(signedData)을 반환 받습니다.
     * 네이버 보안정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류가 반환됩니다.
     * 전자서명 만료일시 이후에 검증 API를 호출하면 오류가 반환됩니다.
     * https://developers.barocert.com/reference/naver/java/cms/api#VerifyCMS
     */
    @Test
    public void TEST_VerifyCMS() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간 이내에만 요청가능 합니다.
            CMSResult result = navercertService.verifyCMS(clientCode, "02312050230900000210000000000016");

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