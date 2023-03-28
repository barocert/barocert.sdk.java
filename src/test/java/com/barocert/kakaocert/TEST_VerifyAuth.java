package com.barocert.kakaocert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.KakaocertService;
import com.barocert.KakaocertServiceImp;
import com.barocert.kakaocert.verifyauth.VerifyAuthObject;
import com.barocert.kakaocert.verifyauth.VerifyAuthResponse;
import com.barocert.kakaocert.verifyauth.VerifyAuthResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthStateResult;

public class TEST_VerifyAuth {
	
    private final String testLinkID = "BKAKAO"; // TODO :: 나중에 바꿔야 함.
    private final String testSecretKey = "egkxYN99ZObjLa3c0nr9/riG+a0VDkZu87LSGR8c37U="; // TODO :: 나중에 바꿔야 함.
	
    private KakaocertService kakaocertService;
	
    public TEST_VerifyAuth() {
        KakaocertServiceImp service = new KakaocertServiceImp();
        service.setServiceURL("https://bc-api.linkhub.kr");
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        service.setUseLocalTimeYN(true);
        kakaocertService = service;
    }
	
    // 본인인증 요청
    @Test
    public void verifyAuthRequest_TEST() throws BarocertException {
        try {
            // 본인인증 요청 Object
            VerifyAuthObject request = new VerifyAuthObject();
			
            // 수신자 정보
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일
            request.setReceiverHP(kakaocertService.AES256Encrypt("01087674117"));
            request.setReceiverName(kakaocertService.AES256Encrypt("이승환"));
            request.setReceiverBirthday(kakaocertService.AES256Encrypt("19930112"));
            // request.setCi(kakaocertService.AES256Encrypt(""));
			
            // 인증요청 메시지 제목이 최대길이 40자.
            request.setReqTitle("인증요청 메시지 제목란");
            // 인증요청 만료시간: 최대 1000(초)까지 입력 가능
            request.setExpireIn(1000);
			
            request.setToken(kakaocertService.AES256Encrypt("본인인증요청토큰"));
			
            // AppToApp 인증요청 여부
            // true: AppToApp 인증방식, false: Talk Message 인증방식
            request.setAppUseYN(false);
			
            // AppToApp 방식 이용 시 입력.
            // request.setReturnURL("https://kakao.barocert.com");
			
            VerifyAuthResponse result = kakaocertService.verifyAuthRequest("023030000003", request);
			
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch(BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }
	
    // 본인인증 상태확인
    @Test
    public void getVerifyAuthState_TEST() throws BarocertException {
        try {
            VerifyAuthStateResult result = kakaocertService.getVerifyAuthState("023030000003", "02303270230300000030000000000029");
			
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("ClientCode : " + result.getClientCode());
            System.out.println("State : " + result.getState());	// 대기(0),완료(1),만료(2),거절(3),실패(4)
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
            System.out.println("isAppUseYN : " + result.isAppUseYN());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }
	
    // 본인인증 서명검증
    @Test
    public void verifyAuth_TEST() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
            // 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
            VerifyAuthResult result = kakaocertService.verifyAuth("023030000003", "02303270230300000030000000000029");
			
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState());	// 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("Token : " + result.getToken());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }
	
}