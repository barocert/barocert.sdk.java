package com.barocert.passcert;

import com.barocert.BarocertException;
import com.barocert.passcert.cms.CMS;
import com.barocert.passcert.cms.CMSReceipt;
import com.barocert.passcert.cms.CMSResult;
import com.barocert.passcert.cms.CMSStatus;
import com.barocert.passcert.cms.CMSVerify;
import com.barocert.passcert.identity.Identity;
import com.barocert.passcert.identity.IdentityReceipt;
import com.barocert.passcert.identity.IdentityResult;
import com.barocert.passcert.identity.IdentityStatus;
import com.barocert.passcert.identity.IdentityVerify;
import com.barocert.passcert.sign.Sign;
import com.barocert.passcert.sign.SignReceipt;
import com.barocert.passcert.sign.SignResult;
import com.barocert.passcert.sign.SignStatus;
import com.barocert.passcert.sign.SignVerify;

public interface PasscertService {
	
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
	public IdentityReceipt requestIdentity(String clientCode, Identity requestIdentity) throws BarocertException;
	
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
	public IdentityStatus getIdentityStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 본인인증 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @param verify
	 * 			서명검증 요청정보
	 * @return ResponseVerifyIdentity
	 * @throws BarocertException
	 */
	public IdentityResult verifyIdentity(String clientCode, String receiptID, IdentityVerify verify) throws BarocertException;
	
	/**
	 * 전자서명 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param requestSign
	 * 			전자서명 요청정보
	 * @return ResponseSign
	 * @throws BarocertException
	 */
	public SignReceipt requestSign(String clientCode, Sign requestSign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseSignStatus
	 * @throws BarocertException
	 */
	public SignStatus getSignStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @param verify
	 * 			서명검증 요청정보
	 * @return ResponseVerifySign
	 * @throws BarocertException
	 */
	public SignResult verifySign(String clientCode, String receiptID, SignVerify verify) throws BarocertException;
		
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
	public CMSReceipt requestCMS(String clientCode, CMS requestCMS) throws BarocertException;
	
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
	public CMSStatus getCMSStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 출금동의 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @param verify
	 * 			서명검증 요청정보
	 * @return ResponseVerifyCMS
	 * @throws BarocertException
	 */
	public CMSResult verifyCMS(String clientCode, String receiptID, CMSVerify verify) throws BarocertException;
	
	/**
     * AES256/GCM/NoPadding (이상 1.8) or AES/CBC/PKCS5Padding(미만 1.8) 암호화
     * 
     * @param plainText
     * @return String
     * @throws BarocertException
     */
	public String encrypt(String plainText) throws BarocertException;
}
