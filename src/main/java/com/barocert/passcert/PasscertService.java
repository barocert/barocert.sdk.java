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
import com.barocert.passcert.login.Login;
import com.barocert.passcert.login.LoginReceipt;
import com.barocert.passcert.login.LoginResult;
import com.barocert.passcert.login.LoginStatus;
import com.barocert.passcert.login.LoginVerify;
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
	 * @param identity
	 * 			본인인증 요청정보
	 * @return IdentityReceipt
	 * 			본인인증 요청 응답정보
	 * @throws BarocertException
	 */
	public IdentityReceipt requestIdentity(String clientCode, Identity identity) throws BarocertException;
	
	/**
	 * 본인인증 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return IdentityStatus
	 * 			본인인증 상태확인 응답정보
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
	 * @param identityVerify
	 * 			본인인증 서명검증 요청정보
	 * @return IdentityResult
	 * 			본인인증 서명검증 응답정보
	 * @throws BarocertException
	 */
	public IdentityResult verifyIdentity(String clientCode, String receiptID, IdentityVerify identityVerify) throws BarocertException;
	
	/**
	 * 전자서명 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param sign
	 * 			전자서명 요청정보
	 * @return SignReceipt
	 * 			전자서명 요청 응답정보
	 * @throws BarocertException
	 */
	public SignReceipt requestSign(String clientCode, Sign sign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return SignStatus
	 * 			전자서명 상태확인 응답정보
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
	 * @param signVerify
	 * 			전자서명 서명검증 요청정보
	 * @return SignResult
	 * 			전자서명 서명검증 응답정보
	 * @throws BarocertException
	 */
	public SignResult verifySign(String clientCode, String receiptID, SignVerify signVerify) throws BarocertException;
		
	/**
	 * 출금동의 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param cms
	 * 			출금동의 요청정보
	 * @return CMSReceipt
	 * 			출금동의 요청 응답정보
	 * @throws BarocertException
	 */
	public CMSReceipt requestCMS(String clientCode, CMS cms) throws BarocertException;
	
	/**
	 * 출금동의 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return CMSStatus
	 * 			출금동의 상태확인 응답정보
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
	 * @param cmsVerify
	 * 			출금동의 서명검증 요청정보
	 * @return CMSResult
	 * 			출금동의 서명검증 응답정보
	 * @throws BarocertException
	 */
	public CMSResult verifyCMS(String clientCode, String receiptID, CMSVerify cmsVerify) throws BarocertException;

	/**
	 * 간편로그인 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param login
	 * 			간편로그인 요청정보
	 * @return LoginReceipt
	 * 			간편로그인 요청 응답정보
	 * @throws BarocertException
	 */
	public LoginReceipt requestLogin(String clientCode, Login login) throws BarocertException;
	
	/**
	 * 간편로그인 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			간편로그인 접수아이디
	 * @return LoginStatus
	 * 			간편로그인 상태확인 응답정보
	 * @throws BarocertException
	 */
	public LoginStatus getLoginStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 간편로그인 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			간편로그인 접수아이디
	 * @param loginVerify
	 * 			간편로그인 서명검증 요청정보
	 * @return LoginResult
	 * 			간편로그인 서명검증 응답정보
	 * @throws BarocertException
	 */
	public LoginResult verifyLogin(String clientCode, String receiptID, LoginVerify loginVerify) throws BarocertException;
	
	/**
     * AES256/GCM/NoPadding(java 1.8 이상) or AES/CBC/PKCS5Padding(java 1.8 미만) 암호화
     * 
     * @param plainText
	 * 			평문
     * @return String
	 * 			암호문
     * @throws BarocertException
     */
	public String encrypt(String plainText) throws BarocertException;

	/**
     * 선택된 암호화 알고리즘을 사용하여 평문 암호화
     * 
     * @param plainText
	 * 			평문
     * @param algorithm
	 * 			암호화 알고리즘
     * @return String
	 * 			암호문
     * @throws BarocertException
     */
	public String encrypt(String plainText, String algorithm) throws BarocertException;

}
