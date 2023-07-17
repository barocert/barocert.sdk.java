package com.barocert.passcert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.passcert.cms.CMS;
import com.barocert.passcert.cms.CMSReceipt;
import com.barocert.passcert.cms.CMSResult;
import com.barocert.passcert.cms.CMSStatus;
import com.barocert.passcert.cms.CMSVerify;

public class TEST_CMS {

    private static final String DEC_ALGORITHM = "RSA";

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";
    private final String testDecretKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCyKXf5N/gMXQmAag28kJMrT3IDSdbwQbXl0eFoeYI/uq8eKbh7ogV2eSDURrmyS5mGDY8yKcXwyEVT4Lbx9yyOcoT0leXT3JVhiYGK5Ds2+I4YZznEESvawMCz8QtZuX5IXEHK9kqWUeSqMQn9Hi9epLXtXN/J1o4DtTIxewdCKhT/yxbW/1d+MJTJkukC75HgeG/4ADl/yow1J81J+GRnvbIS7OTTvIf9uAP1Eu1HL94fP2JJ/Qi0+xfEXEe6/aRTMsGl9P/4XSY8i4RaZAuZqoej/bU4Dm7bhnobz6bo9iPoKPnkd+EE2pLKDEDAqEmCQYXN3TqYpOmF16npvxTTAgMBAAECggEBAK7NSx4lkOUof4MUCwhA3XR4DUg2sYGnJz4m778ewPGgS+MPUidTb4KvE8vS3K2XyTiioyW3oLM+++5xI25CcuAMcnC7hfSZj6NUU9qfVY34zwoYda/unRWTWz7xuI4/Fi0O/6qQfdwA25c0aDWF4To95xXNsvCI52ux415y9Esv9MlMQa/io8KDyuOkTPhZP+sgnwFupvNfunT8CjwpAtDpzznb4zfcEoU3z7jMDRvbTsDgdMmzr46pX+2kmeKtNXX9dnziZXjoLm+gS1Iukt5I7SyLhO2KjdJOBGJ56YYLwq3T3d18smYSe00BXxOqI7RJgwK+G+Ou5sYpgUIu3CkCgYEA5E7GOIwW5zxe0lfz82ZvwMyKJHgF5tdgx9IW7EUBWXbeDlGqVGTnXMb2lftEApmL3cZ+UzxKwCITpyiJfGFBnhV7JYbo9QbFMEPcdfIlZW9d/0SKxzBlHDfHviwLnV2qYjbSMZSH1MkqRh64l1eGeaf8UDVzaToA3Yux6Gu3Fg8CgYEAx8WcKS0E7AW8WcIpUHQXqlzINJ2GNBbrMOz3phVyTp5xWY2gW9dfMrep+ukZicMwirwvh1G6AI0ahnc/0s1n9IkaXQ1xOrvW/K4+NQ0yhZxkJNG1c3hYdCWIA0JZItRRwOXq4xvbO/AOjgEHb5kyeNsnQMgf3fuy8Fzdt4pVOP0CgYEAierkZ6iI9WtMxLiJEBJjlA57rQgsWITnXA6X9mbBJ/BcuD2xLYY/FZbDw1qkfQWQroqIKXQUm/h58tLUKyT9ZKgJWmQjOlG6sSttdHqxCJO8LsaTJz0e92ri6Qjmg0vf77C6TWUyoOJc/Tr1u8cN31QRYcrIS1rUxwDqmkLnuRUCgYEAqQ1Fh+ar6psz93UCBy4mtKkNVtESt1PJtT5il25Aq90SqKjb0bxguAeKVWUakmTV2CFFyypSz5KYpr+VB+uAlAPNhn8QmZZJaMx+oeBIJ57fc/TuqwdlOuxju/ZSfdHUWPt3mLaDAKa1el/OjhbDCljST9TKesw7cYH0shPi6HUCgYAp1hXRnIi5Fu8dKPsk6pQTNV1WtUhEX/aqCtv/qZ3jYTPdA7HJNi9rakizVHQBs6o0JMv/NMMrXeGWw+RCYVNN6DOe5esSKn//uubnHpntlEAz6AKtwUhSU7F58zAQO1L/jsqvpKCFw1p5Z90Hc6UGDW0YMibVMc2nzCf3xxeaDw==";

    private PasscertService passcertService;

    public TEST_CMS() {
        PasscertServiceImp service = new PasscertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setDecryptKey(testDecretKey);
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
            // 수신자 성명 - 최대 80자
            request.setReceiverName(passcertService.encrypt("홍길동"));
            // 수신자 생년월일 - 8자 (yyyyMMdd)
            request.setReceiverBirthday(passcertService.encrypt("19700101"));
            // 인증요청 메시지 제목 - 최대 40자
            request.setReqTitle("패스써트 출금동의 인증요청 타이틀");
            // 인증요청 메시지 - 최대 500자
            request.setReqMessage(passcertService.encrypt("패스써트 출금동의 인증요청 내용"));

            // 고객센터 연락처 - 최대 12자
            request.setCallCenterNum("1600-9854");
            // 인증요청 만료시간 - 최대 1,000(초)까지 입력 가능
            request.setExpireIn(1000);
            // 사용자 동의 필요 여부
            request.setUserAgreementYN(true);
            // 사용자 정보 포함 여부
            request.setReceiverInfoYN(true);

            // 출금은행명 - 최대 100자
            request.setBankName(passcertService.encrypt("국민은행"));
            // 출금계좌번호 - 최대 31자
            request.setBankAccountNum(passcertService.encrypt("9-****-5117-58"));
            // 출금계좌 예금주명 - 최대 100자
            request.setBankAccountName(passcertService.encrypt("홍길동"));
            // 출금유형 
            // CMS - 출금동의, OPEN_BANK - 오픈뱅킹
            request.setBankServiceType(passcertService.encrypt("CMS"));
            // 출금액
            request.setBankWithdraw(passcertService.encrypt("1,000,000원"));
            
            // AppToApp 인증요청 여부
            // true - AppToApp 인증방식, false - Talk Message 인증방식
            request.setAppUseYN(false);
            // ApptoApp 인증방식에서 사용
            // 통신사 유형('SKT', 'KT', 'LGU'), 대문자 입력(대소문자 구분)
            // request.setTelcoType("SKT");
            // ApptoApp 인증방식에서 사용
            // 모바일장비 유형('ANDROID', 'IOS'), 대문자 입력(대소문자 구분)
            // request.setDeviceOSType("IOS");
            
            CMSReceipt result = passcertService.requestCMS("023040000001", request);

            // 접수아이디, 앱스킴, 앱다운로드URL 
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
            System.out.println("MarketUrl : " + result.getMarketUrl());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 출금동의 상태확인
    @Test
    public void TEST_GetCMSStatus() throws BarocertException {
        try {
            CMSStatus result = passcertService.getCMSStatus("023040000001", "02307100230400000010000000000006");

            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ExpireIn : " + result.getExpireIn()); // 단위: 초(s)
            System.out.println("CallCenterName : " + result.getCallCenterName());
            System.out.println("CallCenterNum : " + result.getCallCenterNum());
            System.out.println("ReqTitle : " + result.getReqTitle());
            System.out.println("ReqMessage : " + result.getReqMessage());
            System.out.println("RequestDT : " + result.getRequestDT());
            System.out.println("CompleteDT : " + result.getCompleteDT());
            System.out.println("ExpireDT : " + result.getExpireDT());
            System.out.println("RejectDT : " + result.getRejectDT());
            System.out.println("TokenType : " + result.getTokenType());
            System.out.println("UserAgreementYN : " + result.getUserAgreementYN());
            System.out.println("ReceiverInfoYN : " + result.getReceiverInfoYN());
            System.out.println("TelcoType : " + result.getTelcoType()); // 통신사 유형('SKT', 'KT', 'LGU')
            System.out.println("DeviceOSType : " + result.getDeviceOSType()); // 모바일장비 유형('ANDROID'
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
            CMSVerify request = new CMSVerify(); 
            // 검증 요청자 휴대폰번호 - 11자 (하이픈 제외)
            request.setReceiverHP(passcertService.encrypt("01012341234")); 
            // 검증 요청자 성명 - 최대 80자
            request.setReceiverName(passcertService.encrypt("홍길동"));

            CMSResult result = passcertService.verifyCMS("023040000001", "02307100230400000010000000000006", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("ReceiverName : " + passcertService.decrypt(result.getReceiverName(), DEC_ALGORITHM));
            System.out.println("ReceiverBirthday : " + passcertService.decrypt(result.getReceiverBirthday(), DEC_ALGORITHM));
            System.out.println("ReceiverHP : " + passcertService.decrypt(result.getReceiverHP(), DEC_ALGORITHM));
            System.out.println("ReceiverGender : " + passcertService.decrypt(result.getReceiverGender(), DEC_ALGORITHM));
            System.out.println("ReceiverTelcoType : " + passcertService.decrypt(result.getReceiverTelcoType(), DEC_ALGORITHM));
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("CI : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}
