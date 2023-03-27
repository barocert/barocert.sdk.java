package com.kakaocert.api.test;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.KakaocertService;
import com.barocert.KakaocertServiceImp;
import com.barocert.kakaocert.cms.CMSObject;
import com.barocert.kakaocert.cms.CMSResponse;
import com.barocert.kakaocert.cms.CMSStateResult;
import com.barocert.kakaocert.cms.CMSVerifyResult;

public class TEST_CMS {
	
	private final String testLinkID = "BKAKAO"; // TODO :: 나중에 바꿔야 함.
	private final String testSecretKey = "egkxYN99ZObjLa3c0nr9/riG+a0VDkZu87LSGR8c37U="; // TODO :: 나중에 바꿔야 함.
	
	private KakaocertService kakaocertService;
	
	public TEST_CMS() {
		KakaocertServiceImp service = new KakaocertServiceImp();
		service.setServiceURL("https://bc-api.linkhub.kr");
		service.setLinkID(testLinkID);
		service.setSecretKey(testSecretKey);
		service.setUseStaticIP(false);
		service.setUseLocalTimeYN(true);
		kakaocertService = service;
	}
	
	// 출금동의 요청
	@Test
	public void cMSRequest_TEST() {
		try {
			// 출금동의 요청 Object
			CMSObject request = new CMSObject();
			
			// 수신자 정보(휴대폰번호, 성명, 생년월일)와 Ci 값 중 택일
			request.setReceiverHP(kakaocertService.AES256Encrypt("01087674117"));
			request.setReceiverName(kakaocertService.AES256Encrypt("이승환"));
			request.setReceiverBirthday(kakaocertService.AES256Encrypt("19930112"));
			// request.setCi(kakaocertService.AES256Encrypt(""));
			
			request.setReqTitle("인증요청 메시지 제공란");
			request.setExpireIn(1000); // 요청 마감시간(단위:초)
			
			request.setRequestCorp(kakaocertService.AES256Encrypt("청구 기관명란"));
			request.setBankName(kakaocertService.AES256Encrypt("출금은행명란"));
			request.setBankAccountNum(kakaocertService.AES256Encrypt("9-4324-5117-58"));
			request.setBankAccountName(kakaocertService.AES256Encrypt("예금주명 입력란"));
			request.setBankAccountBirthday(kakaocertService.AES256Encrypt("19930112"));
			request.setBankServiceType(kakaocertService.AES256Encrypt("CMS")); // CMS, FIRM, GIRO
			
			// AppToApp 인증요청 여부
	        // true: AppToApp 인증방식, false: Talk Message 인증방식
			request.setAppUseYN(false);
			
			// AppToApp 방식 이용 시 입력.
			// request.setReturnURL("https://kakao.barocert.com");
			
			CMSResponse result = kakaocertService.cMSRequest("023030000003", request);
			
			System.out.println("ReceiptID : " + result.getReceiptID());
			System.out.println("Scheme : " + result.getScheme());
		} catch(BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
	// 출금동의 상태확인
	@Test
	public void getCMSState_TEST() throws BarocertException {
		try {
			CMSStateResult result = kakaocertService.getCMSState("023030000003", "02303270230300000030000000000001");
			
			System.out.println("ReceiptID : " + result.getReceiptID());
			System.out.println("ClientCode : " + result.getClientCode());
			System.out.println("State : " + result.getState());	// 대기(0),완료(1),만료(2),거절(3),실패(4)
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
	
	// 출금동의 검증
	@Test
	public void cMSVerify_TEST() throws BarocertException {
		try {
			// 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
			// 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
			CMSVerifyResult result = kakaocertService.cMSVerify("023030000003", "02303270230300000030000000000001");
			
			System.out.println("ReceiptID : " + result.getReceiptID());
			System.out.println("State : " + result.getState());	// 대기(0),완료(1),만료(2),거절(3),실패(4)
			System.out.println("SignedData : " + result.getSignedData());
			System.out.println("Ci : " + result.getCi());
		} catch (BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
}
