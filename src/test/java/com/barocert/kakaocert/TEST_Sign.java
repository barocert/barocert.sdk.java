package com.barocert.kakaocert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.kakaocert.sign.MultiSignTokens;
import com.barocert.kakaocert.sign.MultiSign;
import com.barocert.kakaocert.sign.Sign;
import com.barocert.kakaocert.sign.MultiSignReceipt;
import com.barocert.kakaocert.sign.SignReceipt;
import com.barocert.kakaocert.sign.MultiSignStatus;
import com.barocert.kakaocert.sign.SignStatus;
import com.barocert.kakaocert.sign.MultiSignResult;
import com.barocert.kakaocert.sign.SignResult;

public class TEST_Sign {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";

    private KakaocertService kakaocertService;

    public TEST_Sign() {
        KakaocertServiceImp service = new KakaocertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(true);
        service.setUseLocalTimeYN(true);
        kakaocertService = service;
    }

    // 전자서명 서명요청(단건)
    // https://developers.barocert.com/reference/kakao/java/sign/api-single#RequestSign
    @Test
    public void TEST_RequestSign() throws BarocertException {
        try {
            // 전자서명 요청 정보 객체
            Sign request = new Sign();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(kakaocertService.encrypt("01012341234"));
            // 수신자 성명 - 80자
            request.setReceiverName(kakaocertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(kakaocertService.encrypt("19700101"));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("전자서명단건테스트");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);
            // 서명 원문 - 원문 2,800자 까지 입력가능
            request.setToken(kakaocertService.encrypt("전자서명단건테스트데이터"));
            // 서명 원문 유형
            // TEXT - 일반 텍스트, HASH - HASH 데이터
            request.setTokenType("TEXT");

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // App to App 방식 이용시, 호출할 URL
            // request.setReturnURL("https://www.kakaocert.com");

            SignReceipt result = kakaocertService.requestSign("023040000001", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(단건)
    // https://developers.barocert.com/reference/kakao/java/sign/api-single#GetSignStatus
    @Test
    public void TEST_GetSignStatus() throws BarocertException {
        try {
            SignStatus result = kakaocertService.getSignStatus("023040000001", "02307100230400000010000000000003");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("AuthCategory : " + result.getAuthCategory());
            System.out.println("ReturnURL : " + result.getReturnURL());
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

    // 전자서명 서명검증(단건)
    // https://developers.barocert.com/reference/kakao/java/sign/api-single#VerifySign
    @Test
    public void TEST_VerifySign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
            // 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
            SignResult result = kakaocertService.verifySign("023040000001", "02307100230400000010000000000003");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("Ci : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 서명요청(복수)
    // https://developers.barocert.com/reference/kakao/java/sign/api-multi#RequestMultiSign
    @Test
    public void TEST_RequestMultiSign() throws BarocertException {
        try {
            // 전자서명 요청 정보 객체
            MultiSign request = new MultiSign();


            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(kakaocertService.encrypt("01012341234"));
            // 수신자 성명 - 80자
            request.setReceiverName(kakaocertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(kakaocertService.encrypt("19700101"));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("전자서명(복수)");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // 개별문서 등록 - 최대 20 건
            // 개별 요청 정보 객체
            MultiSignTokens token = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token.setReqTitle("계약1");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token.setToken(kakaocertService.encrypt("본 계약서는 카카오써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token);

            MultiSignTokens token2 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token2.setReqTitle("계약2");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token2.setToken(kakaocertService.encrypt("본 계약서는 카카오써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token2);
            
            MultiSignTokens token3 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token3.setReqTitle("계약3");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token3.setToken(kakaocertService.encrypt("본 계약서는 카카오써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token3);
            
            MultiSignTokens token4 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token4.setReqTitle("계약4");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token4.setToken(kakaocertService.encrypt("본 계약서는 카카오써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token4);
            
            MultiSignTokens token5 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token5.setReqTitle("계약5");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token5.setToken(kakaocertService.encrypt("본 계약서는 카카오써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token5);
            
            // 서명 원문 유형
            // TEXT - 일반 텍스트, HASH - HASH 데이터
            request.setTokenType("TEXT");

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // App to App 방식 이용시, 에러시 호출할 URL
            // request.setReturnURL("https://www.kakaocert.com");

            MultiSignReceipt result = kakaocertService.requestMultiSign("023040000001", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(복수)
    // https://developers.barocert.com/reference/kakao/java/sign/api-multi#GetMultiSignStatus
    @Test
    public void TEST_GetMultiSignStatus() throws BarocertException {
        try {
            MultiSignStatus result = kakaocertService.getMultiSignStatus("023040000001", "02307100230400000010000000000004");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("AuthCategory : " + result.getAuthCategory());
            System.out.println("ReturnURL : " + result.getReturnURL());
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

    // 전자서명 서명검증(복수)
    // https://developers.barocert.com/reference/kakao/java/sign/api-multi#VerifyMultiSign
    @Test
    public void TEST_VerifyMultiSign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간(10분)이내에만 요청가능 합니다.
            MultiSignResult result = kakaocertService.verifyMultiSign("023040000001", "02307100230400000010000000000004");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)

            for (int i = 0; i < result.getMultiSignedData().size(); i++)
                System.out.println("MultiSignedData " + i + " : " + result.getMultiSignedData().get(i));

            System.out.println("Ci : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}
