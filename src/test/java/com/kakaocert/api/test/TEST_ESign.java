package com.kakaocert.api.test;

import java.util.ArrayList;

import org.junit.Test;

import com.barocert.BarocertException;
import com.barocert.KakaocertService;
import com.barocert.KakaocertServiceImp;
import com.barocert.kakaocert.esign.BulkRequestESign;
import com.barocert.kakaocert.esign.BulkResultESign;
import com.barocert.kakaocert.esign.BulkVerifyResult;
import com.barocert.kakaocert.esign.RequestESign;
import com.barocert.kakaocert.esign.ResponseESign;
import com.barocert.kakaocert.esign.ResultESign;
import com.barocert.kakaocert.esign.ResultVerifyEsign;
import com.barocert.kakaocert.esign.Tokens;

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
			RequestESign request = new RequestESign();
			
			// 수신자 정보(휴대폰번호, 성명, 생년월일)와 Ci 값 중 택일
			request.setReceiverHP("01087674117");
			request.setReceiverName("이승환");
			request.setReceiverBirthday("19930112");
			// request.setCi("");
			
			request.setReqTitle("전자서명단건테스트");
			request.setExpireIn(1000);
			request.setToken("전자서명단건테스트데이터");
			request.setTokenType("TEXT"); // TEXT, HASH
			
			// App to App 방식 이용시, 에러시 호출할 URL
			// request.setReturnURL("https://kakao.barocert.com");
			
			ResponseESign response = kakaocertService.requestESign("023030000003", request, false);
			System.out.println(response.getReceiptId());
			System.out.println(response.getScheme());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 전자서명 요청(다건)
	@Test
	public void bulkRequestESign_TEST() throws BarocertException {
		try {
			BulkRequestESign request = new BulkRequestESign();
			
			// 수신자 정보(휴대폰번호, 성명, 생년월일)와 Ci 값 중 택일
			request.setReceiverHP("01087674117");
			request.setReceiverName("이승환");
			request.setReceiverBirthday("19930112");
			// request.setCi("");
			
			request.setReqTitle("전자서명다건테스트");
			request.setExpireIn(1000);
			
			request.setTokens(new ArrayList<Tokens>());
			
			Tokens token = new Tokens();
			token.setReqTitle("전자서명다건문서테스트1");
			token.setToken("전자서명다건테스트데이터1");
			request.getTokens().add(token);
			
			token = new Tokens();
			token.setReqTitle("전자서명다건문서테스트2");
			token.setToken("전자서명다건테스트데이터2");
			request.getTokens().add(token);
			
			request.setTokenType("TEXT"); // TEXT, HASH
			
			// App to App 방식 이용시, 에러시 호출할 URL
			// request.setReturnURL("https://kakao.barocert.com");
			
			ResponseESign response = kakaocertService.bulkRequestESign("023020000003", request, false);
			
			System.out.println(response.getReceiptId());
			System.out.println(response.getScheme());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
	// 전자서명 상태확인(단건)	
	@Test
	public void getESignState_TEST() throws BarocertException {
		try {
			ResultESign response = kakaocertService.getESignState("023020000003", "0230316215859000000000000000000000000001");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getRequestID());
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
	public void getBulkESignState_TEST() throws BarocertException {
		try {
			BulkResultESign response = kakaocertService.getBulkESignState("023020000003", "0230316220145000000000000000000000000001");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getRequestID());
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
			ResultVerifyEsign response = kakaocertService.verifyESign("023020000003", "0230316215859000000000000000000000000001");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getRequestID());
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
	public void bulkVerifyESign_TEST() throws BarocertException {
		try {
			BulkVerifyResult response = kakaocertService.bulkVerifyESign("023020000003", "0230316220145000000000000000000000000001");
			
			System.out.println(response.getReceiptID());
			System.out.println(response.getRequestID());
			System.out.println(response.getState());
			
			for(int i = 0; i < response.getBulkSignedData().size(); i++) {
				System.out.println(response.getBulkSignedData().get(i));
			}
			
			System.out.println(response.getCi());
		} catch(BarocertException ke) {
			System.out.println(ke.getCode());
			System.out.println(ke.getMessage());
		}
	}
	
}
