package com.barocert.passcert;

import com.barocert.crypto.Filez;
import com.barocert.crypto.HASH;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.passcert.sign.Sign;
import com.barocert.passcert.sign.SignReceipt;
import com.barocert.passcert.sign.SignResult;
import com.barocert.passcert.sign.SignStatus;
import com.barocert.passcert.sign.SignVerify;


public class TEST_Sign {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";

    private PasscertService passcertService;

    public TEST_Sign() {
        PasscertServiceImp service = new PasscertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        passcertService = service;
    }

    /*
     * 패스 이용자에게 문서의 전자서명을 요청합니다.
     * https://developers.barocert.com/reference/pass/java/sign/api#RequestSign
     */
    @Test
    public void TEST_RequestSign() throws BarocertException, FileNotFoundException {
        try {
            // 전자서명 요청 정보 객체
            Sign request = new Sign();

            // 수신자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(passcertService.encrypt("01012341234"));
            // 수신자 성명 - 최대 80자
            request.setReceiverName(passcertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(passcertService.encrypt("19700101"));

            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("전자서명 요청 메시지 제목");
            // 인증요청 메시지 - 최대 500자
            request.setReqMessage(passcertService.encrypt("전자서명 요청 메시지"));
            // 고객센터 연락처 - 최대 12자
            request.setCallCenterNum("1600-9854");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);
            // 서명 원문 유형
            // 'TEXT' - 일반 텍스트, 'HASH' - HASH 데이터, 'URL' - URL 데이터, 'PDF' - PDF 데이터
            // 원본데이터(originalTypeCode, originalURL, originalFormatCode) 입력시 'TEXT', 'PDF' 사용 불가
            request.setTokenType("URL");
            // 서명 원문 - 원문 2,800자 까지 입력가능
            request.setToken(passcertService.encrypt("전자서명 요청 원문"));

            // 서명 원문 유형
            // request.setTokenType("PDF");
            // 서명 원문 유형이 PDF인 경우, 원문은 SHA-256, Base64 URL Safe No Padding을 사용
            // byte[] target = Filez.fileToBytesFrom("barocert.pdf");
            // request.setToken(passcertService.encrypt(passcertService.sha256_base64url_file(target)));

            // 사용자 동의 필요 여부
            request.setUserAgreementYN(true);
            // 사용자 정보 포함 여부
            request.setReceiverInfoYN(true);
            
            // 원본유형코드
            // 'AG' - 동의서, 'AP' - 신청서, 'CT' - 계약서, 'GD' - 안내서, 'NT' - 통지서, 'TR' - 약관
            request.setOriginalTypeCode("TR");
            // 원본조회URL
            request.setOriginalURL("https://www.passcert.co.kr");
            // 원본형태코드
            // ('TEXT', 'HTML', 'DOWNLOAD_IMAGE', 'DOWNLOAD_DOCUMENT')
            request.setOriginalFormatCode("HTML");

            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Push 인증방식
            request.setAppUseYN(false);
            // ApptoApp 인증방식에서 사용
            // 통신사 유형('SKT', 'KT', 'LGU'), 대문자 입력(대소문자 구분)
            // request.setTelcoType("SKT");
            // ApptoApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            // request.setDeviceOSType("IOS");

            SignReceipt result = passcertService.requestSign("023070000014", request);

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
     * 전자서명 요청 후 반환받은 접수아이디로 인증 진행 상태를 확인합니다.
     * 상태확인 함수는 전자서명 요청 함수를 호출한 당일 23시 59분 59초까지만 호출 가능합니다.
     * 전자서명 요청 함수를 호출한 당일 23시 59분 59초 이후 상태확인 함수를 호출할 경우 오류가 반환됩니다.
     * https://developers.barocert.com/reference/pass/java/login/api#GetLoginStatus
     */
    @Test
    public void TEST_GetSignStatus() throws BarocertException {
        try {
            SignStatus result = passcertService.getSignStatus("023070000014", "02307100230700000140000000000005");

            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("RequestDT : " + result.getRequestDT());
            System.out.println("CompleteDT : " + result.getCompleteDT());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("RejectDT : " + result.getRejectDT());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    /*
     * 완료된 전자서명을 검증하고 전자서명값(signedData)을 반환 받습니다.
     * 검증 함수는 전자서명 요청 함수를 호출한 당일 23시 59분 59초까지만 호출 가능합니다.
     * 전자서명 요청 함수를 호출한 당일 23시 59분 59초 이후 검증 함수를 호출할 경우 오류가 반환됩니다.
     * https://developers.barocert.com/reference/pass/java/sign/api#VerifySign
     */
    @Test
    public void TEST_VerifySign() throws BarocertException {
        try {
            // 검증 요청 정보 객체
            SignVerify verify = new SignVerify();
            // 검증 요청자 휴대폰번호 - 11자 (하이픈 제외)
            verify.setReceiverHP(passcertService.encrypt("01012341234")); 
            // 검증 요청자 성명 - 최대 80자
            verify.setReceiverName(passcertService.encrypt("홍길동"));

            SignResult result = passcertService.verifySign("023070000014", "02307100230700000140000000000005", verify);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4),미처리(5)
            System.out.println("ReceiverName : " + result.getReceiverName());
            System.out.println("ReceiverYear : " + result.getReceiverYear());
            System.out.println("ReceiverDay : " + result.getReceiverDay());
            System.out.println("ReceiverHP : " + result.getReceiverHP());
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
