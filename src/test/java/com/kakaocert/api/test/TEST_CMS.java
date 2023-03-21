package com.kakaocert.api.test;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.KakaocertService;
import com.barocert.KakaocertServiceImp;
import com.barocert.kakaocert.cms.RequestCMS;
import com.barocert.kakaocert.cms.ResultCMS;
import com.barocert.kakaocert.cms.ResultCMSState;
import com.barocert.kakaocert.cms.VerifyCMSResult;

public class TEST_CMS {
	
	private final String testLinkID = "BKAKAO"; // TODO :: 나중에 바꿔야 함.
	private final String testSecretKey = "egkxYN99ZObjLa3c0nr9/riG+a0VDkZu87LSGR8c37U="; // TODO :: 나중에 바꿔야 함.
	
	private KakaocertService kakaocertService;
	
	public TEST_CMS() {
		KakaocertServiceImp service = new KakaocertServiceImp();
		service.setLinkID(testLinkID);
		service.setSecretKey(testSecretKey);
		service.setUseStaticIP(false);
		service.setUseLocalTimeYN(true);
		kakaocertService = service;
	}
	
	// 출금동의 요청
	@Test
	public void request_TEST() {
		try {
			RequestCMS request = new RequestCMS();
			
			// 수신자 정보(휴대폰번호, 성명, 생년월일)와 Ci 값 중 택일
			request.setReceiverHP(kakaocertService.AES256Encrypt("01087674117"));
			request.setReceiverName(kakaocertService.AES256Encrypt("이승환"));
			request.setReceiverBirthday(kakaocertService.AES256Encrypt("19930112"));
			// request.setCi(kakaocertService.AES256Encrypt(""));
			
			request.setReqTitle("인증요청 메시지 제공란");
			request.setExpireIn(1000);
			
			request.setRequestCorp(kakaocertService.AES256Encrypt("청구 기관명란"));
			request.setBankName(kakaocertService.AES256Encrypt("출금은행명란"));
			request.setBankAccountNum(kakaocertService.AES256Encrypt("9-4324-5117-58"));
			request.setBankAccountName(kakaocertService.AES256Encrypt("예금주명 입력란"));
			request.setBankAccountBirthday(kakaocertService.AES256Encrypt("19930112"));
			request.setBankServiceType(kakaocertService.AES256Encrypt("CMS")); // CMS, FIRM, GIRO
			
			// App to App 방식 이용시, 에러시 호출할 URL
			// request.setReturnURL("https://kakao.barocert.com");
			
			ResultCMS result = kakaocertService.requestCMS("023030000003", request, false);
			
			System.out.println(result.getReceiptID());
			System.out.println(result.getScheme());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 출금동의 상태확인
	@Test
	public void getResult_TEST() throws BarocertException {
		try {
			ResultCMSState result = kakaocertService.getCMSState("023020000003", "0230317094926000000000000000000000000001");
			
			System.out.println(result.getReceiptID());
			System.out.println(result.getClientCode());
			System.out.println(result.getState());
			System.out.println(result.getExpireIn());
			System.out.println(result.getCallCenterName());
			System.out.println(result.getCallCenterNum());
			System.out.println(result.getReqTitle());
			System.out.println(result.getAuthCategory());
			System.out.println(result.getReturnURL());
			System.out.println(result.getTokenType());
			System.out.println(result.getRequestDT());
			System.out.println(result.getViewDT());
			System.out.println(result.getCompleteDT());
			System.out.println(result.getExpireDT());
			System.out.println(result.getVerifyDT());
			System.out.println(result.getScheme());
			System.out.println(result.isAppUseYN());
		} catch (BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 출금동의 검증
	@Test
	public void verifyCMS_TEST() throws BarocertException {
		try {
			VerifyCMSResult result = kakaocertService.verifyCMS("023020000003", "0230317094926000000000000000000000000001");
			
			System.out.println(result.getReceiptID());
			System.out.println(result.getState());
			System.out.println(result.getSignedData());
			System.out.println(result.getCi());
		} catch (BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
}
