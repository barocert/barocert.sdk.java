package com.barocert.kakaocert;

import java.util.ArrayList;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.KakaocertService;
import com.barocert.KakaocertServiceImp;
import com.barocert.kakaocert.esign.ESignMultiObject;
import com.barocert.kakaocert.esign.ESignMultiResponse;
import com.barocert.kakaocert.esign.MultiESignStateResult;
import com.barocert.kakaocert.esign.MultiESignVerifyResult;
import com.barocert.kakaocert.esign.ESignObject;
import com.barocert.kakaocert.esign.ESignResponse;
import com.barocert.kakaocert.esign.ESignStateResult;
import com.barocert.kakaocert.esign.ESignVerifyResult;
import com.barocert.kakaocert.esign.MultiESignTokens;

public class TEST_ESign {

	private final String testLinkID = "BKAKAO"; // TODO :: 나중에 바꿔야 함.
	private final String testSecretKey = "egkxYN99ZObjLa3c0nr9/riG+a0VDkZu87LSGR8c37U="; // TODO :: 나중에 바꿔야 함.
	
	private KakaocertService kakaocertService;
	
	public TEST_ESign() {
		KakaocertServiceImp service = new KakaocertServiceImp();
		service.setServiceURL("https://bc-api.linkhub.kr");
		service.setLinkID(testLinkID);
		service.setSecretKey(testSecretKey);
		service.setUseStaticIP(false);
		service.setUseLocalTimeYN(true);
		kakaocertService = service;
	}
		
	// 전자서명 요청(단건)
	@Test
	public void eSignRequest_TEST() throws BarocertException {
		try {
			// 전자서명 요청(단건) Object
			ESignObject request = new ESignObject();
			
            // 수신자 정보
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일
			request.setReceiverHP(kakaocertService.AES256Encrypt("01087674117"));
			request.setReceiverName(kakaocertService.AES256Encrypt("이승환"));
			request.setReceiverBirthday(kakaocertService.AES256Encrypt("19930112"));
			// request.setCi(kakaocertService.AES256Encrypt(kakaocertService.AES256Encrypt(""));
			
			// 인증요청 메시지 제목이 최대길이 40자.
			request.setReqTitle("전자서명단건테스트");
			// 인증요청 만료시간: 최대 1000(초)까지 입력 가능
			request.setExpireIn(1000);
			request.setToken(kakaocertService.AES256Encrypt("토큰원문 단건 테스트"));
			request.setTokenType("TEXT"); // TEXT, HASH
			
			// AppToApp 인증요청 여부
	        // true: AppToApp 인증방식, false: Talk Message 인증방식
			request.setAppUseYN(false);
			
			// AppToApp 방식 이용 시 입력.
			// request.setReturnURL("https://kakao.barocert.com");
			
			ESignResponse result = kakaocertService.eSignRequest("023030000003", request);
			
			System.out.println("ReceiptID : " + result.getReceiptID());
			System.out.println("Scheme : " + result.getScheme());
		} catch(BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
	// 전자서명 요청(다건)
	@Test
	public void eSignMultiRequest_TEST() throws BarocertException {
		try {
			// 전자서명 요청(다건) Object
			ESignMultiObject request = new ESignMultiObject();
			
			// 수신자 정보
            // 휴대폰번호,성명,생년월일 또는 Ci(연계정보)값 중 택 일
			request.setReceiverHP(kakaocertService.AES256Encrypt("01087674117"));
			request.setReceiverName(kakaocertService.AES256Encrypt("이승환"));
			request.setReceiverBirthday(kakaocertService.AES256Encrypt("19930112"));
			// request.setCi(kakaocertService.AES256Encrypt(""));
			
			// 인증요청 메시지 제목이 최대길이 40자.
			request.setReqTitle("인증요청 구분제목 테스트");
			// 인증요청 만료시간: 최대 1000(초)까지 입력 가능
			request.setExpireIn(1000);
			
			request.setTokens(new ArrayList<MultiESignTokens>());
			
			// 원문길이 2800자 까지 입력가능.
			String num = "1";
			StringBuilder sb = new StringBuilder();
			for(int j=0; j < 2800; j++) {
				sb.append(num); // 최대길이 2800자 테스트
			}
			
			// 인증요청 메시지 제목, 토큰원문은 최대 20개.
			for(int i = 0; i < 20; i++) {
				MultiESignTokens token = new MultiESignTokens();
				token.setReqTitle("서명요청 제목 다건 테스트 " + i);
				token.setToken(kakaocertService.AES256Encrypt(sb.toString())); // 원문길이 2800자 까지 입력가능.
				request.getTokens().add(token);
			}
			
			// AppToApp 인증요청 여부
	        // true: AppToApp 인증방식, false: Talk Message 인증방식
			request.setAppUseYN(false);
			
			request.setTokenType("TEXT"); // TEXT, HASH
			
			// AppToApp 방식 이용 시
			// request.setReturnURL("https://kakao.barocert.com");
			
			ESignMultiResponse result = kakaocertService.eSignMultiRequest("023030000003", request);
			
			System.out.println("ReceiptID : " + result.getReceiptID());
			System.out.println("Scheme : " + result.getScheme());
		} catch(BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
	// 전자서명 상태확인(단건)	
	@Test
	public void getESignState_TEST() throws BarocertException {
		try {
			ESignStateResult result = kakaocertService.getESignState("023030000003", "02303270230300000030000000000033");
			
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
		} catch(BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
	// 전자서명 상태확인(다건)	
	@Test
	public void getMultiESignState_TEST() throws BarocertException {
		try {
			MultiESignStateResult result = kakaocertService.getMultiESignState("023030000003", "02303270230300000030000000000034");
			
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
		} catch(BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
	// 전자서명 검증(단건)
	@Test
	public void eSignVerify_TEST() throws BarocertException {
		try {
			// 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
			// 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
			ESignVerifyResult result = kakaocertService.eSignVerify("023030000003", "02303270230300000030000000000033");
			
			System.out.println("ReceiptID : " + result.getReceiptID());
			System.out.println("State : " + result.getState());	// 대기(0),완료(1),만료(2),거절(3),실패(4)
			System.out.println("SignedData : " + result.getSignedData());
			System.out.println("Ci : " + result.getCi());
		} catch(BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
	// 전자서명 검증(다건)
	@Test
	public void multiESignVerify_TEST() throws BarocertException {
		try {
			// 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며,
			// 사용자가 서명을 완료하고, 10분(유효시간) 까지 검증하기 API 요청가능 합니다.
			MultiESignVerifyResult result = kakaocertService.multiESignVerify("023030000003", "02303270230300000030000000000034");
			
			System.out.println("ReceiptID : " + result.getReceiptID());
			System.out.println("State : " + result.getState());	// 대기(0),완료(1),만료(2),거절(3),실패(4)
			
			for(int i = 0; i < result.getMultiSignedData().size(); i++)
				System.out.println("MultiSignedData " + i + " : " + result.getMultiSignedData().get(i));
			
			System.out.println("Ci : " + result.getCi());
		} catch(BarocertException be) {
			System.out.println("Code : " + be.getCode());
			System.out.println("Message : " + be.getMessage());
		}
	}
	
}
