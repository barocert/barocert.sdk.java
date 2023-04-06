package com.barocert.kakaocert;

import com.barocert.BarocertException;
import com.barocert.kakaocert.cms.RequestCMS;
import com.barocert.kakaocert.cms.ResponseCMS;
import com.barocert.kakaocert.cms.ResponseCMSStatus;
import com.barocert.kakaocert.cms.ResponseVerifyCMS;
import com.barocert.kakaocert.identity.RequestIdentity;
import com.barocert.kakaocert.identity.ResponseIdentity;
import com.barocert.kakaocert.identity.ResponseIdentityStatus;
import com.barocert.kakaocert.identity.ResponseVerifyIdentity;
import com.barocert.kakaocert.sign.RequestMultiSign;
import com.barocert.kakaocert.sign.RequestSign;
import com.barocert.kakaocert.sign.ResponseMultiSign;
import com.barocert.kakaocert.sign.ResponseSign;
import com.barocert.kakaocert.sign.ResponseMultiSignStatus;
import com.barocert.kakaocert.sign.ResponseSignStatus;
import com.barocert.kakaocert.sign.ResponseVerifyMultiSign;
import com.barocert.kakaocert.sign.ResponseVerifySign;

public interface KakaocertService {
	
	/**
	 * 본인인증 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestIdentity
	 * 			본인인증 요청정보
	 * @return ResponseVerify
	 * @throws BarocertException
	 */
	public ResponseIdentity requestIdentity(String clientCode, RequestIdentity requestIdentity) throws BarocertException;
	
	/**
	 * 본인인증 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResponseIdentityStatus
	 * @throws BarocertException
	 */
	public ResponseIdentityStatus getIdentityStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 본인인증 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResponseVerifyIdentity
	 * @throws BarocertException
	 */
	public ResponseVerifyIdentity verifyIdentity(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(단건) 
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestSign
	 * 			전자서명 요청정보
	 * @return ResponseSign
	 * @throws BarocertException
	 */
	public ResponseSign requestSign(String clientCode, RequestSign requestSign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseSignStatus
	 * @throws BarocertException
	 */
	public ResponseSignStatus getSignStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseVerifySign
	 * @throws BarocertException
	 */
	public ResponseVerifySign verifySign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(복수)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestMultiSign	
	 * 			전자서명 요청정보
	 * @return ResponseMultiSign
	 * @throws BarocertException
	 */
	public ResponseMultiSign requestMultiSign(String clientCode, RequestMultiSign requestMultiSign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(복수)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseMultiSignStatus
	 * @throws BarocertException
	 */
	public ResponseMultiSignStatus getMultiSignStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(복수)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseVerifyMultiSign
	 * @throws BarocertException
	 */
	public ResponseVerifyMultiSign verifyMultiSign(String clientCode, String receiptID) throws BarocertException;
	
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
	 * @return ResponseCMSStatus
	 * @throws BarocertException
	 */
	public ResponseCMSStatus getCMSStatus(String clientCode, String receiptID) throws BarocertException;
	
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
	public ResponseVerifyCMS verifyCMS(String clientCode, String receiptID) throws BarocertException;
	
	/**
     * AES256/GCM/NoPadding 암호화
     * 
     * @param plainText
     * @return String
     * @throws BarocertException
     */
	public String encryptGCM(String plainText) throws BarocertException;
}
