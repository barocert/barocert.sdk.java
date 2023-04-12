package com.barocert.kakaocert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.kakaocert.sign.MultiSignTokens;
import com.barocert.kakaocert.sign.RequestMultiSign;
import com.barocert.kakaocert.sign.RequestSign;
import com.barocert.kakaocert.sign.ResponseMultiSign;
import com.barocert.kakaocert.sign.ResponseSign;
import com.barocert.kakaocert.sign.ResponseMultiSignStatus;
import com.barocert.kakaocert.sign.ResponseSignStatus;
import com.barocert.kakaocert.sign.ResponseVerifyMultiSign;
import com.barocert.kakaocert.sign.ResponseVerifySign;

public class TEST_Sign {

    private final String testLinkID = "LINKHUB_BC";
    private final String testSecretKey = "npCAl0sHPpJqlvMbrcBmNagrxkQ74w9Sl0A+M++kMCE=";

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
    @Test
    public void TEST_RequestSign() throws BarocertException {
        try {
            // 전자서명 요청 정보 객체
            RequestSign request = new RequestSign();

            // 수신자 정보
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일
            request.setReceiverHP(kakaocertService.encrypt("01054437896"));
            request.setReceiverName(kakaocertService.encrypt("최상혁"));
            request.setReceiverBirthday(kakaocertService.encrypt("19880301"));
            // request.setCi(kakaocertService.encrypt(""));

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
            // eSignRequest.setReturnURL("https://www.kakaocert.com");

            ResponseSign result = kakaocertService.requestSign("023030000004", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(단건)
    @Test
    public void TEST_GetSignStatus() throws BarocertException {
        try {
            ResponseSignStatus result = kakaocertService.getSignStatus("023030000004", "02304120230300000040000000000040");

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

    // 전자서명 서명검증(단건)
    @Test
    public void TEST_VerifySign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
            // 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
            ResponseVerifySign result = kakaocertService.verifySign("023030000004", "02304120230300000040000000000040");

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
    @Test
    public void TEST_RequestMultiSign() throws BarocertException {
        try {
            // 전자서명 요청 정보 객체
            RequestMultiSign request = new RequestMultiSign();

            // 수신자 정보
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일
            request.setReceiverHP(kakaocertService.encrypt("01054437896"));
            request.setReceiverName(kakaocertService.encrypt("최상혁"));
            request.setReceiverBirthday(kakaocertService.encrypt("19880301"));
            // multiSignRequest.setCi(kakaocertService.encrypt(""));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("전자서명복수테스트");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // 개별문서 등록 - 최대 20 건
            // 개별 요청 정보 객체
            MultiSignTokens token = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token.setReqTitle("전자서명복수문서테스트1");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token.setToken(kakaocertService.encrypt("전자서명복수테스트데이터1"));
            request.addToken(token);

            // 개별 요청 정보 객체
            MultiSignTokens token2 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token2.setReqTitle("전자서명복수문서테스트2");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token2.setToken(kakaocertService.encrypt("전자서명복수테스트데이터2"));
            request.addToken(token2);

            // 서명 원문 유형
            // TEXT - 일반 텍스트, HASH - HASH 데이터
            request.setTokenType("TEXT");

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // App to App 방식 이용시, 에러시 호출할 URL
            // request.setReturnURL("https://www.kakaocert.com");

            ResponseMultiSign result = kakaocertService.requestMultiSign("023030000004", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(복수)
    @Test
    public void TEST_GetMultiSignStatus() throws BarocertException {
        try {
            ResponseMultiSignStatus result = kakaocertService.getMultiSignStatus("023030000004", "02304120230300000040000000000041");

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

    // 전자서명 서명검증(복수)
    @Test
    public void TEST_VerifyMultiSign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간(10분)이내에만 요청가능 합니다.
            ResponseVerifyMultiSign result = kakaocertService.verifyMultiSign("023030000004", "02304120230300000040000000000041");

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
