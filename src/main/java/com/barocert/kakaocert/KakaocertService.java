package com.barocert.kakaocert;

import com.barocert.BarocertException;
import com.barocert.kakaocert.cms.CMS;
import com.barocert.kakaocert.cms.CMSReceipt;
import com.barocert.kakaocert.cms.CMSStatus;
import com.barocert.kakaocert.cms.CMSResult;
import com.barocert.kakaocert.identity.Identity;
import com.barocert.kakaocert.identity.IdentityReceipt;
import com.barocert.kakaocert.identity.IdentityStatus;
import com.barocert.kakaocert.identity.IdentityResult;
import com.barocert.kakaocert.sign.MultiSign;
import com.barocert.kakaocert.sign.Sign;
import com.barocert.kakaocert.sign.MultiSignReceipt;
import com.barocert.kakaocert.sign.SignReceipt;
import com.barocert.kakaocert.sign.MultiSignStatus;
import com.barocert.kakaocert.sign.SignStatus;
import com.barocert.kakaocert.login.LoginResult;
import com.barocert.kakaocert.sign.MultiSignResult;
import com.barocert.kakaocert.sign.SignResult;

public interface KakaocertService {
	
	/**
	 * 본인인증 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param identity
	 * 			본인인증 요청정보
	 * @return ResponseVerify
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
	 * @return ResponseIdentityStatus
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
	 * @return IdentityResult
	 * 			본인인증 서명검증 응답정보
	 * @throws BarocertException
	 */
	public IdentityResult verifyIdentity(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(단건) 
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param sign
	 * 			전자서명 요청정보
	 * @return ResponseSign
	 * 			전자서명 요청 응답정보
	 * @throws BarocertException
	 */
	public SignReceipt requestSign(String clientCode, Sign sign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResponseSignStatus
	 * 			전자서명 상태확인 응답정보
	 * @throws BarocertException
	 */
	public SignStatus getSignStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return SignResult
	 * 			전자서명 서명검증 응답정보
	 * @throws BarocertException
	 */
	public SignResult verifySign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 요청(복수)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param multiSign	
	 * 			전자서명 요청정보
	 * @return MultiSignReceipt
	 * 			전자서명 요청 응답정보
	 * @throws BarocertException
	 */
	public MultiSignReceipt requestMultiSign(String clientCode, MultiSign multiSign) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(복수)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return MultiSignStatus
	 * 			전자서명 상태확인 응답정보
	 * @throws BarocertException
	 */
	public MultiSignStatus getMultiSignStatus(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(복수)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return MultiSignResult
	 * 			전자서명 서명검증 응답정보
	 * @throws BarocertException
	 */
	public MultiSignResult verifyMultiSign(String clientCode, String receiptID) throws BarocertException;
	
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
	 * @return CMSResult
	 * 			출금동의 서명검증 응답정보
	 * @throws BarocertException
	 */
	public CMSResult verifyCMS(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 간편로그인 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param txID
	 * 			간편로그인 txID
	 * @return LoginResult
	 * 			간편로그인 서명검증 응답정보
	 * @throws BarocertException
	 */			
	public LoginResult verifyLogin(String clientCode, String txID) throws BarocertException;

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
	public String sha256_base64url_file(byte[] target) throws BarocertException;
}
