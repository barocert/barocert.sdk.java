package com.kakaocert.api.test;

import java.util.ArrayList;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.KakaocertService;
import com.barocert.KakaocertServiceImp;
import com.barocert.kakaocert.esign.ESMultiRequest;
import com.barocert.kakaocert.esign.ESMultiResponse;
import com.barocert.kakaocert.esign.MultiStateResult;
import com.barocert.kakaocert.esign.ESMultiVerifyResult;
import com.barocert.kakaocert.esign.ESRequest;
import com.barocert.kakaocert.esign.ESResponse;
import com.barocert.kakaocert.esign.ESStateResult;
import com.barocert.kakaocert.esign.ESVerifyResult;
import com.barocert.kakaocert.esign.ESMultiTokens;

public class TEST_ESign {

	private final String testLinkID = "BKAKAO"; // TODO :: 나중에 바꿔야 함.
	private final String testSecretKey = "egkxYN99ZObjLa3c0nr9/riG+a0VDkZu87LSGR8c37U="; // TODO :: 나중에 바꿔야 함.
	
	private KakaocertService kakaocertService;
	
	public TEST_ESign() {
		KakaocertServiceImp service = new KakaocertServiceImp();
		service.setLinkID(testLinkID);
		service.setSecretKey(testSecretKey);
		service.setUseStaticIP(false);
		service.setUseLocalTimeYN(true);
		kakaocertService = service;
	}
		
	// 전자서명 요청(단건)
	@Test
	public void requestESign_TEST() throws BarocertException {
		try {
			// 전자서명 요청 정보 Object
			ESRequest request = new ESRequest();
			
			// 수신자 정보(휴대폰번호, 성명, 생년월일)와 Ci 값 중 택일
			request.setReceiverHP(kakaocertService.AES256Encrypt("01087674117"));
			request.setReceiverName(kakaocertService.AES256Encrypt("이승환"));
			request.setReceiverBirthday(kakaocertService.AES256Encrypt("19930112"));
			// request.setCi(kakaocertService.AES256Encrypt(kakaocertService.AES256Encrypt(""));
			
			request.setReqTitle("전자서명단건테스트");
			request.setExpireIn(1000);
			request.setToken(kakaocertService.AES256Encrypt("토큰원문 단건 테스트"));
			request.setTokenType("TEXT"); // TEXT, HASH
			
			// AppToApp 방식 이용 시 입력.
			// request.setReturnURL("https://kakao.barocert.com");
			
			ESResponse result = kakaocertService.requestESign("023030000003", request, false);
			
			System.out.println(result.getReceiptID());
			System.out.println(result.getScheme());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 전자서명 요청(다건)
	@Test
	public void requestMultiESign_TEST() throws BarocertException {
		try {
			ESMultiRequest request = new ESMultiRequest();
			
			// 수신자 정보(휴대폰번호, 성명, 생년월일)와 Ci 값 중 택1
			request.setReceiverHP(kakaocertService.AES256Encrypt("01087674117"));
			request.setReceiverName(kakaocertService.AES256Encrypt("이승환"));
			request.setReceiverBirthday(kakaocertService.AES256Encrypt("19930112"));
			// request.setCi(kakaocertService.AES256Encrypt(""));
			
			request.setReqTitle("인증요청 구분제목 테스트");
			request.setExpireIn(1000);
			
			String num = "1";
			StringBuilder sb = new StringBuilder();
			for(int j=0; j < 2800; j++) {
				sb.append(num);
			}
			
			request.setTokens(new ArrayList<ESMultiTokens>());
			
			// 인증요청 메시지 제목, 토큰원문은 최대 20개.
			for(int i = 0; i < 20; i++) {
				ESMultiTokens token = new ESMultiTokens();
				token.setReqTitle("서명요청 제목 다건 테스트 " + i);
				token.setToken(kakaocertService.AES256Encrypt(sb.toString())); // 원문길이는 2800자 까지.
				request.getTokens().add(token);
			}
			
			request.setTokenType("TEXT"); // TEXT, HASH
			
			// AppToApp 방식 이용 시
			// request.setReturnURL("https://kakao.barocert.com");
			
			ESMultiResponse result = kakaocertService.requestMultiESign("023030000003", request, false);
			
			System.out.println(result.getReceiptID());
			System.out.println(result.getScheme());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 전자서명 상태확인(단건)	
	@Test
	public void getESignState_TEST() throws BarocertException {
		try {
			ESStateResult response = kakaocertService.getESignState("023030000003", "0230323095510000000000000000000000000001");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getClientCode());
			System.out.println(response.getState());
			System.out.println(response.getExpireIn());
			System.out.println(response.getCallCenterName());
			System.out.println(response.getCallCenterNum());
			System.out.println(response.getReqTitle());
			System.out.println(response.getAuthCategory());
			System.out.println(response.getReturnURL());
			System.out.println(response.getTokenType());
			System.out.println(response.getRequestDT());
			System.out.println(response.getViewDT());
			System.out.println(response.getCompleteDT());
			System.out.println(response.getExpireDT());
			System.out.println(response.getVerifyDT());
			System.out.println(response.getScheme());
			System.out.println(response.isAppUseYN());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 전자서명 상태확인(다건)	
	@Test
	public void getMultiESignState_TEST() throws BarocertException {
		try {
			MultiStateResult response = kakaocertService.getMultiESignState("023030000003", "0230323-023030000051-0000000000000000017");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getClientCode());
			System.out.println(response.getState());
			System.out.println(response.getExpireIn());
			System.out.println(response.getCallCenterName());
			System.out.println(response.getCallCenterNum());
			System.out.println(response.getReqTitle());
			System.out.println(response.getAuthCategory());
			System.out.println(response.getReturnURL());
			System.out.println(response.getTokenType());
			System.out.println(response.getRequestDT());
			System.out.println(response.getViewDT());
			System.out.println(response.getCompleteDT());
			System.out.println(response.getExpireDT());
			System.out.println(response.getVerifyDT());
			System.out.println(response.getScheme());
			System.out.println(response.isAppUseYN());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 전자서명 검증(단건)
	@Test
	public void verifyESign_TEST() throws BarocertException {
		try {
			ESVerifyResult response = kakaocertService.verifyESign("023030000003", "0230323095510000000000000000000000000001");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getState());
			System.out.println(response.getSignedData());
			System.out.println(response.getCi());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 전자서명 검증(다건)
	@Test
	public void multiVerifyESign_TEST() throws BarocertException {
		try {
			ESMultiVerifyResult response = kakaocertService.multiVerifyESign("023030000003", "0230323-023030000051-0000000000000000017");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getState());
			
			for(int i = 0; i < response.getMultiSignedData().size(); i++) {
				System.out.println(response.getMultiSignedData().get(i));
			}
			
			System.out.println(response.getCi());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
}
