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
        service.setUseStaticIP(false);
        kakaocertService = service;
    }

    /*
     * 카카오톡 이용자에게 단건(1건) 문서의 전자서명을 요청합니다.
     * https://developers.barocert.com/reference/kakao/java/sign/api-single#RequestSign
     */
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

            // 서명 요청 제목 - 최대 40자
            request.setSignTitle("전자서명(단건) 서명 요청 제목");
            // 커스텀 메시지 - 최대 500자
            request.setExtraMessage(kakaocertService.encrypt("전자서명(단건) 커스텀 메시지"));
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);
            // 서명 원문 - 원문 2,800자 까지 입력가능
            request.setToken(kakaocertService.encrypt("전자서명(단건) 요청 원문"));
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

    /*
     * 전자서명(단건) 요청 후 반환받은 접수아이디로 인증 진행 상태를 확인합니다.
     * https://developers.barocert.com/reference/kakao/java/sign/api-single#GetSignStatus
     */
    @Test
    public void TEST_GetSignStatus() throws BarocertException {
        try {
            SignStatus result = kakaocertService.getSignStatus("023040000001", "02311220230400000010000000000009");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("RequestDT : " + result.getRequestDT());
            System.out.println("ViewDT : " + result.getViewDT());
            System.out.println("CompleteDT : " + result.getCompleteDT());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("VerifyDT : " + result.getVerifyDT());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 완료된 전자서명을 검증하고 전자서명값(signedData)을 반환 받습니다.
     * 카카오 보안정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류가 반환됩니다.
     * 전자서명 완료일시로부터 10분 이내에 검증 API를 호출하지 않으면 오류가 반환됩니다.
     * https://developers.barocert.com/reference/kakao/java/sign/api-single#VerifySign
     */
    @Test
    public void TEST_VerifySign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간(10분)이내에만 요청가능 합니다.
            SignResult result = kakaocertService.verifySign("023040000001", "02311220230400000010000000000009");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("Ci : " + result.getCi());
            System.out.println("ReceiverName : " + result.getReceiverName());
            System.out.println("ReceiverYear : " + result.getReceiverYear());
            System.out.println("ReceiverDay : " + result.getReceiverDay());
            System.out.println("ReceiverHP : " + result.getReceiverHP());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 카카오톡 이용자에게 복수(최대 20건) 문서의 전자서명을 요청합니다.
     * https://developers.barocert.com/reference/kakao/java/sign/api-multi#RequestMultiSign
     */
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
            request.setReqTitle("전자서명(복수) 요청 메시지 제목");
            // 커스텀 메시지 - 최대 500자
            request.setExtraMessage(kakaocertService.encrypt("전자서명(복수) 커스텀 메시지"));
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);

            // 개별문서 등록 - 최대 20 건
            // 개별 요청 정보 객체
            MultiSignTokens token = new MultiSignTokens();
            // 서명 요청 제목 - 최대 40자
            token.setSignTitle("전자서명(복수) 서명 요청 제목 1");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token.setToken(kakaocertService.encrypt("전자서명(복수) 요청 메시지 원문 1"));
            request.addToken(token);

            MultiSignTokens token2 = new MultiSignTokens();
            // 서명 요청 제목 - 최대 40자
            token2.setSignTitle("전자서명(복수) 서명 요청 제목 2");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token2.setToken(kakaocertService.encrypt("전자서명(복수) 요청 메시지 원문 2"));
            request.addToken(token2);
            
            MultiSignTokens token3 = new MultiSignTokens();
            // 서명 요청 제목 - 최대 40자
            token3.setSignTitle("전자서명(복수) 서명 요청 제목 3");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token3.setToken(kakaocertService.encrypt("전자서명(복수) 요청 메시지 원문 3"));
            request.addToken(token3);
            
            MultiSignTokens token4 = new MultiSignTokens();
            // 서명 요청 제목 - 최대 40자
            token4.setSignTitle("전자서명(복수) 서명 요청 제목 4");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token4.setToken(kakaocertService.encrypt("전자서명(복수) 요청 메시지 원문 4"));
            request.addToken(token4);
            
            MultiSignTokens token5 = new MultiSignTokens();
            // 서명 요청 제목 - 최대 40자
            token5.setSignTitle("전자서명(복수) 서명 요청 제목 5");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            token5.setToken(kakaocertService.encrypt("전자서명(복수) 요청 메시지 원문 5"));
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

    /*
     * 전자서명(복수) 요청 후 반환받은 접수아이디로 인증 진행 상태를 확인합니다.
     * https://developers.barocert.com/reference/kakao/java/sign/api-multi#GetMultiSignStatus
     */
    @Test
    public void TEST_GetMultiSignStatus() throws BarocertException {
        try {
            MultiSignStatus result = kakaocertService.getMultiSignStatus("023040000001", "02311220230400000010000000000010");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("RequestDT : " + result.getRequestDT());
            System.out.println("ViewDT : " + result.getViewDT());
            System.out.println("CompleteDT : " + result.getCompleteDT());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("VerifyDT : " + result.getVerifyDT());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 완료된 전자서명을 검증하고 전자서명값(signedData)을 반환 받습니다.
     * 카카오 보안정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류가 반환됩니다.
     * 전자서명 완료일시로부터 10분 이후에 검증 API를 호출하면 오류가 반환됩니다.
     * https://developers.barocert.com/reference/kakao/java/sign/api-multi#VerifyMultiSign
     */
    @Test
    public void TEST_VerifyMultiSign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간(10분)이내에만 요청가능 합니다.
            MultiSignResult result = kakaocertService.verifyMultiSign("023040000001", "02311220230400000010000000000010");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)

            for (int i = 0; i < result.getMultiSignedData().size(); i++)
                System.out.println("MultiSignedData " + i + " : " + result.getMultiSignedData().get(i));

            System.out.println("Ci : " + result.getCi());
            System.out.println("ReceiverName : " + result.getReceiverName());
            System.out.println("ReceiverYear : " + result.getReceiverYear());
            System.out.println("ReceiverDay : " + result.getReceiverDay());
            System.out.println("ReceiverHP : " + result.getReceiverHP());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}
