package com.barocert.kakaocert;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.kakaocert.verifyauth.RequestVerify;
import com.barocert.kakaocert.verifyauth.ResponseVerifyAuth;
import com.barocert.kakaocert.verifyauth.ResponseStateVerify;
import com.barocert.kakaocert.verifyauth.ResponseVerify;

public class TEST_VerifyAuth {
	
    private final String testLinkID = "LINKHUB_BC";
    private final String testSecretKey = "npCAl0sHPpJqlvMbrcBmNagrxkQ74w9Sl0A+M++kMCE=";
	
    private KakaocertService kakaocertService;
	
    public TEST_VerifyAuth() {
        KakaocertServiceImp service = new KakaocertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        service.setUseLocalTimeYN(true);
        kakaocertService = service;
    }
	
    // 본인인증 요청
    @Test
    public void TEST_RequestAuth() throws BarocertException {
        try {
            // 본인인증 요청 Object
            RequestVerify request = new RequestVerify();
			
            // 수신자 정보.
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일.
            request.setReceiverHP(kakaocertService.encryptGCM("01054437896"));
            request.setReceiverName(kakaocertService.encryptGCM("최상혁"));
            request.setReceiverBirthday(kakaocertService.encryptGCM("19880301"));
            // request.setCi(kakaocertService.encryptGCM(""));
			
            // 인증요청 메시지 제목이 최대길이 40자.
            request.setReqTitle("인증요청 메시지 제목란");
            // 인증요청 만료시간: 최대 1000(초)까지 입력 가능
            request.setExpireIn(1000);
			
            request.setToken(kakaocertService.encryptGCM("본인인증요청토큰"));
			
            // AppToApp 인증요청 여부
            // true: AppToApp 인증방식, false: Talk Message 인증방식
            request.setAppUseYN(false);
			
            // AppToApp 방식 이용 시 입력.
            // request.setReturnURL("https://kakao.barocert.com");
			
            ResponseVerify result = kakaocertService.requestVerify("023030000004", request);
			
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("Scheme : " + result.getScheme());
        } catch(BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }
	
    // 본인인증 상태확인
    @Test
    public void TEST_RequestStateAuth() throws BarocertException {
        try {
            ResponseStateVerify result = kakaocertService.requestStateVerify("023030000004", "02303300230300000810000000000004");
			
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
    public void TEST_RequestVerifyAuth() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
            // 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
            ResponseVerifyAuth result = kakaocertService.requestVerifyAuth("023030000004", "02303300230300000810000000000004");
			
            System.out.println("ReceiptID : " + result.getReceiptID());
            System.out.println("State : " + result.getState());	// 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("Token : " + result.getToken());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }
	
}