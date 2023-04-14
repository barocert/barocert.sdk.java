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
import com.barocert.kakaocert.sign.MultiSignResult;
import com.barocert.kakaocert.sign.SignResult;

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
	 * @return ResponseVerifyIdentity
	 * @throws BarocertException
	 */
	public IdentityResult verifyIdentity(String clientCode, String receiptID) throws BarocertException;
	
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
	public SignReceipt requestSign(String clientCode, Sign requestSign) throws BarocertException;
	
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
	public SignStatus getSignStatus(String clientCode, String receiptID) throws BarocertException;
	
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
	public SignResult verifySign(String clientCode, String receiptID) throws BarocertException;
	
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
	public MultiSignReceipt requestMultiSign(String clientCode, MultiSign requestMultiSign) throws BarocertException;
	
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
	public MultiSignStatus getMultiSignStatus(String clientCode, String receiptID) throws BarocertException;
	
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
	public MultiSignResult verifyMultiSign(String clientCode, String receiptID) throws BarocertException;
	
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
	 * @return ResponseVerifyCMS
	 * @throws BarocertException
	 */
	public CMSResult verifyCMS(String clientCode, String receiptID) throws BarocertException;
	
	/**
     * AES256/GCM/NoPadding (이상 1.8) or AES/CBC/PKCS5Padding(미만 1.8) 암호화
     * 
     * @param plainText
     * @return String
     * @throws BarocertException
     */
	public String encrypt(String plainText) throws BarocertException;
}
