package com.barocert.kakaocert;

import java.util.ArrayList;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.kakaocert.esign.MultiESignTokens;
import com.barocert.kakaocert.esign.RequestESign;
import com.barocert.kakaocert.esign.RequestMultiESign;
import com.barocert.kakaocert.esign.ResponseESign;
import com.barocert.kakaocert.esign.ResponseMultiESign;
import com.barocert.kakaocert.esign.ResponseStateESign;
import com.barocert.kakaocert.esign.ResponseStateMultiESign;
import com.barocert.kakaocert.esign.ResponseVerifyESign;
import com.barocert.kakaocert.esign.ResponseVerifyMultiESign;

public class TEST_ESign {

    private final String testLinkID = "LINKHUB_BC";
    private final String testSecretKey = "npCAl0sHPpJqlvMbrcBmNagrxkQ74w9Sl0A+M++kMCE=";

    private KakaocertService kakaocertService;

    public TEST_ESign() {
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
    public void TEST_RequestESign() throws BarocertException {
        try {
            // 전자서명 요청(단건) Object
            RequestESign request = new RequestESign();

            // 수신자 정보.
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일.
            request.setReceiverHP(kakaocertService.encryptGCM("01054437896"));
            request.setReceiverName(kakaocertService.encryptGCM("최상혁"));
            request.setReceiverBirthday(kakaocertService.encryptGCM("19880301"));
            // request.setCi(kakaocertService.AES256encryptGCMrypt(kakaocertService.encryptGCM(""));

            request.setReqTitle("인증요청 메시지 제공란"); // 인증요청 메시지 제목이 최대길이 40자.
            request.setExpireIn(1000); // 인증요청 만료시간 : 최대 1000(초)까지 입력 가능.

            request.setToken(kakaocertService.encryptGCM("토큰원문 단건 테스트")); // 원문 2800자 까지 입력가능.
            request.setTokenType("TEXT"); // TEXT, HASH

            // AppToApp 인증요청 여부.
            // true: AppToApp 인증방식, false: Talk Message 인증방식.
            request.setAppUseYN(false);

            // AppToApp 방식 이용 시 입력.
            // request.setReturnURL("https://kakao.barocert.com");

            ResponseESign result = kakaocertService.requestESign("023030000004", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(단건)
    @Test
    public void TEST_RequestStateESign() throws BarocertException {
        try {
            ResponseStateESign result = kakaocertService.stateESign("023030000004", "02304040230300000040000000000004");

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
            System.out.println("isAppUseYN : " + result.isAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 서명검증(단건)
    @Test
    public void TEST_RequestVerifyESign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
            // 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
            ResponseVerifyESign result = kakaocertService.verifyESign("023030000004", "02304040230300000040000000000004");

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("Ci : " + result.getCi());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 서명요청(다건)
    @Test
    public void TEST_RequestMultiESign() throws BarocertException {
        try {
            // 전자서명 요청(다건) Object
            RequestMultiESign request = new RequestMultiESign();

            // 수신자 정보.
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일.
            request.setReceiverHP(kakaocertService.encryptGCM("01087674117"));
            request.setReceiverName(kakaocertService.encryptGCM("이승환"));
            request.setReceiverBirthday(kakaocertService.encryptGCM("19930112"));
            // request.setCi(kakaocertService.AES256encryptGCMrypt(""));

            request.setReqTitle("인증요청 구분제목 테스트"); // 인증요청 메시지 제목이 최대길이 40자.
            request.setExpireIn(1000); // 인증요청 만료시간: 최대 1000(초)까지 입력 가능.

            request.setTokens(new ArrayList<MultiESignTokens>());

            // 최대길이 2800자 테스트.
            String num = "💼";

            System.out.println(num.length());

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 1400; j++) { // 원문 2800자 까지 입력가능.
                sb.append(num);
            }

            // 최대 20건 다건 테스트.
            for (int i = 0; i < 20; i++) { // 토큰원문은 최대 20개 까지 입력가능.
                MultiESignTokens token = new MultiESignTokens();
                token.setReqTitle("서명요청 제목 다건 테스트 " + i); // 인증요청 메시지 제목.
                token.setToken(kakaocertService.encryptGCM(sb.toString())); // 원문 2800자 까지 입력가능.
                request.getTokens().add(token);
            }

            request.setTokenType("TEXT"); // TEXT, HASH

            // AppToApp 인증요청 여부.
            // true: AppToApp 인증방식, false: Talk Message 인증방식.
            request.setAppUseYN(false);

            // AppToApp 방식 이용 시
            // request.setReturnURL("https://kakao.barocert.com");

            ResponseMultiESign result = kakaocertService.requestMultiESign("023030000081", request);

            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 상태확인(다건)
    @Test
    public void TEST_RequestStateMultiESign() throws BarocertException {
        try {
            ResponseStateMultiESign result = kakaocertService.stateMultiESign("023030000081", "02303300230300000810000000000010");

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
            System.out.println("isAppUseYN : " + result.isAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

    // 전자서명 서명검증(다건)
    @Test
    public void TEST_RequestVerifyMultiESign() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
            // 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
            ResponseVerifyMultiESign result = kakaocertService.verifyMultiESign("023030000081", "02303300230300000810000000000010");

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

    @Test
    public void testName() throws Exception {
        String property = System.getProperty("java.version");
        System.out.println(property);
    }

}
