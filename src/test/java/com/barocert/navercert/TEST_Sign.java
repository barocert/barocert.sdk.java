package com.barocert.navercert;

import com.barocert.BarocertException;
import com.barocert.navercert.sign.*;
import org.junit.Test;

public class TEST_Sign {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";
    private NavercertService navercertService;
    private String clientCode = "023060000088";

    public TEST_Sign() {
        NavercertServiceImp service = new NavercertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(false);
        service.setUseStaticIP(true);
        service.setUseLocalTimeYN(true);
        navercertService = service;
    }

    // 전자서명 서명요청(단건)
    // https://developers.barocert.com/reference/naver/java/sign/api-single#RequestSign
    @Test
    public void TEST_RequestSign() throws BarocertException {
        try {
            // 전자서명 요청 정보 객체
            Sign request = new Sign();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(navercertService.encrypt("01012341234"));
            // 수신자 성명 - 80자
            request.setReceiverName(navercertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(navercertService.encrypt("19700101"));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("전자서명단건테스트");

            // 고객센터 연락처 - 최대 12자
            request.setCallCenterNum("1600-9854");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);
            // 인증요청 메시지 - 최대 500자
            request.setReqMessage(navercertService.encrypt("전자서명 인증요청 메시지"));
            // 서명 원문 - 원문 2,800자 까지 입력가능
            request.setToken(navercertService.encrypt("전자서명 단건 테스트 데이터"));
            // 서명 원문 유형
            // TEXT - 일반 텍스트, HASH - HASH 데이터
            request.setTokenType(navercertService.encrypt("TEXT"));


            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);

            // ApptoApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            //request.setDeviceOSType("ANDROID");

            // App to App 방식 이용시, 호출할 URL
            // request.setReturnURL("navercert://sign");

            SignReceipt result = navercertService.requestSign(clientCode, request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("MarketUrl : " + result.getMarketUrl());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(단건)
    // https://developers.barocert.com/reference/naver/java/sign/api-single#GetSignStatus
    @Test
    public void TEST_GetSignStatus() throws BarocertException {
        try {

            SignStatus result = navercertService.getSignStatus(clientCode, "02308230230600000880000000000030");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("ReturnURL : " + result.getReturnURL());
            System.out.println("tokenType : " + result.getTokenType());
            System.out.println("deviceOSType : " + result.getDeviceOSType());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("AppUseYN : " + result.getAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 서명검증(단건)
    // https://developers.barocert.com/reference/naver/java/sign/api-single#VerifySign
    @Test
    public void TEST_VerifySign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간 이내에만 요청가능 합니다.
            SignResult result = navercertService.verifySign(clientCode, "02308230230600000880000000000024");

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

    // 전자서명 서명요청(복수)
    // https://developers.barocert.com/reference/naver/java/sign/api-multi#RequestMultiSign
    @Test
    public void TEST_RequestMultiSign() throws BarocertException {
        try {
            // 전자서명 요청 정보 객체
            MultiSign request = new MultiSign();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(navercertService.encrypt("01012341234"));
            // 수신자 성명 - 80자
            request.setReceiverName(navercertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(navercertService.encrypt("19700101"));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("전자서명(복수)");
            // 고객센터 연락처 - 최대 12자
            request.setCallCenterNum("1600-9854");
            // 인증요청 메시지 - 최대 500자
            request.setReqMessage(navercertService.encrypt("전자서명 인증요청 메시지"));
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // 개별문서 등록 - 최대 20 건
            // 개별 요청 정보 객체
            MultiSignTokens token = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token.setTokenType(navercertService.encrypt("TEXT"));
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token.setToken(navercertService.encrypt("본 계약서는 네이버써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token);

            MultiSignTokens token2 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token2.setTokenType(navercertService.encrypt("TEXT"));
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token2.setToken(navercertService.encrypt("본 계약서는 네이버써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token2);
            
            MultiSignTokens token3 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token3.setTokenType(navercertService.encrypt("TEXT"));
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token3.setToken(navercertService.encrypt("본 계약서는 네이버써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token3);
            
            MultiSignTokens token4 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token4.setTokenType(navercertService.encrypt("TEXT"));
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token4.setToken(navercertService.encrypt("본 계약서는 네이버써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token4);
            
            MultiSignTokens token5 = new MultiSignTokens();
            // 인증요청 메시지 제목 - 최대 40자
            token5.setTokenType(navercertService.encrypt("TEXT"));
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token5.setToken(navercertService.encrypt("본 계약서는 네이버써트의 서비스 이용을 신청하며 이하 내용에 동의 합니다."));
            request.addToken(token5);

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);


            // ApptoApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            // request.setDeviceOSType("ANDROID");

            // App to App 방식 이용시, 에러시 호출할 URL
            // request.setReturnURL("navercert://Sign");

            MultiSignReceipt result = navercertService.requestMultiSign(clientCode, request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("MarketUrl : " + result.getMarketUrl());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(복수)
    // https://developers.barocert.com/reference/naver/java/sign/api-multi#GetMultiSignStatus
    @Test
    public void TEST_GetMultiSignStatus() throws BarocertException {
        try {
            MultiSignStatus result = navercertService.getMultiSignStatus(clientCode, "02308250230600000880000000000003");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn());
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("ReturnURL : " + result.getReturnURL());
            System.out.println("tokenType : " + result.getTokenType());
            System.out.println("deviceOSType : " + result.getDeviceOSType());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("AppUseYN : " + result.getAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 서명검증(복수)
    // https://developers.barocert.com/reference/naver/java/sign/api-multi#VerifyMultiSign
    @Test
    public void TEST_VerifyMultiSign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간 이내에만 요청가능 합니다.
            MultiSignResult result = navercertService.verifyMultiSign(clientCode, "02308250230600000880000000000003");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)

            for (int i = 0; i < result.getMultiSignedData().size(); i++)
                System.out.println("MultiSignedData " + i + " : " + result.getMultiSignedData().get(i));

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
