package com.barocert;

import com.barocert.kakaocert.cms.CMSObject;
import com.barocert.kakaocert.cms.ResponseCMS;
import com.barocert.kakaocert.cms.ResponseStateCMS;
import com.barocert.kakaocert.cms.ResponseVerifyCMS;
import com.barocert.kakaocert.esign.ResponseMultiESign;
import com.barocert.kakaocert.esign.MultiESignObject;
import com.barocert.kakaocert.esign.ResponseStateMultiESign;
import com.barocert.kakaocert.esign.ResponseVerifyMultiESign;
import com.barocert.kakaocert.esign.ESignObject;
import com.barocert.kakaocert.esign.ResponseESign;
import com.barocert.kakaocert.esign.ResponseStateESign;
import com.barocert.kakaocert.esign.ResponseVerifyESign;
import com.barocert.kakaocert.verifyauth.AuthObject;
import com.barocert.kakaocert.verifyauth.ResponseAuth;
import com.barocert.kakaocert.verifyauth.ResponseStateAuth;
import com.barocert.kakaocert.verifyauth.ResponseVerifyAuth;

public interface KakaocertService {
	
	/**
	 * 본인인증 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param verifyAuthObject
	 * 			본인인증 요청정보
	 * @return VerifyAuthResponse
	 * @throws BarocertException
	 */
	public ResponseAuth requestAuth(String clientCode, AuthObject authObject) throws BarocertException;
	
	/**
	 * 본인인증 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return VerifyAuthStateResult
	 * @throws BarocertException
	 */
	public ResponseStateAuth requestStateAuth(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 본인인증 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return VerifyAuthResult
	 * @throws BarocertException
	 */
	public ResponseVerifyAuth requestVerifyAuth(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(단건) 
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param eSignObject
	 * 			전자서명 요청정보
	 * @return ESignResponse
	 * @throws BarocertException
	 */
	public ResponseESign requestESign(String clientCode, ESignObject eSignObject) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ESignStateResult
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
	 * @return ESignVerifyResult
	 * @throws BarocertException
	 */
	public ResponseVerifyESign requestVerifyESign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param eSignMultiObject	
	 * 			전자서명 요청정보
	 * @return ESignMultiResponse
	 * @throws BarocertException
	 */
	public ResponseMultiESign requestMultiESign(String clientCode, MultiESignObject multiESignObject) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return MultiESignStateResult
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
	 * @return MultiESignVerifyResult
	 * @throws BarocertException
	 */
	public ResponseVerifyMultiESign requestVerifyMultiESign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 출금동의 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param cMSRequest
	 * 			출금동의 요청정보
	 * @return CMSResponse
	 * @throws BarocertException
	 */
	public ResponseCMS requestCMS(String clientCode, CMSObject cMSObject) throws BarocertException;
	
	/**
	 * 출금동의 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return CMSStateResult
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
	 * @return CMSVerifyResult
	 * @throws BarocertException
	 */
	public ResponseVerifyCMS requestVerifyCMS(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * AES256 암호화
	 * 
	 * @param plainText
	 * @return String
	 * @throws BarocertException
	 */
	public String AES256Encrypt(String plainText) throws BarocertException;

}
