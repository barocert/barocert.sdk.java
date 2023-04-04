package com.barocert.kakaocert;

import com.barocert.BarocertException;
import com.barocert.kakaocert.cms.RequestCMS;
import com.barocert.kakaocert.cms.ResponseCMS;
import com.barocert.kakaocert.cms.ResponseStateCMS;
import com.barocert.kakaocert.cms.ResponseVerifyCMS;
import com.barocert.kakaocert.esign.ResponseMultiESign;
import com.barocert.kakaocert.esign.RequestMultiESign;
import com.barocert.kakaocert.esign.ResponseStateMultiESign;
import com.barocert.kakaocert.esign.ResponseVerifyMultiESign;
import com.barocert.kakaocert.esign.RequestESign;
import com.barocert.kakaocert.esign.ResponseESign;
import com.barocert.kakaocert.esign.ResponseStateESign;
import com.barocert.kakaocert.esign.ResponseVerifyESign;
import com.barocert.kakaocert.verifyauth.RequestVerify;
import com.barocert.kakaocert.verifyauth.ResponseStateVerify;
import com.barocert.kakaocert.verifyauth.ResponseVerify;
import com.barocert.kakaocert.verifyauth.ResponseVerifyAuth;

public interface KakaocertService {
	
	/**
	 * 본인인증 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestVerify
	 * 			본인인증 요청정보
	 * @return ResponseVerify
	 * @throws BarocertException
	 */
	public ResponseVerify requestVerify(String clientCode, RequestVerify requestVerify) throws BarocertException;
	
	/**
	 * 본인인증 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResponseStateVerify
	 * @throws BarocertException
	 */
	public ResponseStateVerify requestStateVerify(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 본인인증 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResponseVerifyAuth
	 * @throws BarocertException
	 */
	public ResponseVerifyAuth requestVerifyAuth(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(단건) 
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestESign
	 * 			전자서명 요청정보
	 * @return ResponseESign
	 * @throws BarocertException
	 */
	public ResponseESign requestESign(String clientCode, RequestESign requestESign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseStateESign
	 * @throws BarocertException
	 */
	public ResponseStateESign requestStateESign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseVerifyESign
	 * @throws BarocertException
	 */
	public ResponseVerifyESign requestVerifyESign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestMultiESign	
	 * 			전자서명 요청정보
	 * @return ResponseMultiESign
	 * @throws BarocertException
	 */
	public ResponseMultiESign requestMultiESign(String clientCode, RequestMultiESign requestMultiESign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseStateMultiESign
	 * @throws BarocertException
	 */
	public ResponseStateMultiESign requestStateMultiESign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseVerifyMultiESign
	 * @throws BarocertException
	 */
	public ResponseVerifyMultiESign requestVerifyMultiESign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 출금동의 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestCMS
	 * 			출금동의 요청정보
	 * @return ResponseCMS
	 * @throws BarocertException
	 */
	public ResponseCMS requestCMS(String clientCode, RequestCMS requestCMS) throws BarocertException;
	
	/**
	 * 출금동의 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return ResponseStateCMS
	 * @throws BarocertException
	 */
	public ResponseStateCMS requestStateCMS(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 출금동의 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return ResponseVerifyCMS
	 * @throws BarocertException
	 */
	public ResponseVerifyCMS requestVerifyCMS(String clientCode, String receiptID) throws BarocertException;

	public String encryptGCM(String plainText) throws BarocertException;
}