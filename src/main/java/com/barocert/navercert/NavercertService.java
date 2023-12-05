package com.barocert.navercert;

import com.barocert.BarocertException;
import com.barocert.navercert.identity.Identity;
import com.barocert.navercert.identity.IdentityReceipt;
import com.barocert.navercert.identity.IdentityResult;
import com.barocert.navercert.identity.IdentityStatus;
import com.barocert.navercert.sign.Sign;
import com.barocert.navercert.sign.SignReceipt;
import com.barocert.navercert.sign.SignResult;
import com.barocert.navercert.sign.SignStatus;
import com.barocert.navercert.sign.MultiSign;
import com.barocert.navercert.sign.MultiSignReceipt;
import com.barocert.navercert.sign.MultiSignResult;
import com.barocert.navercert.sign.MultiSignStatus;
import com.barocert.navercert.cms.CMS;
import com.barocert.navercert.cms.CMSReceipt;
import com.barocert.navercert.cms.CMSResult;
import com.barocert.navercert.cms.CMSStatus;

public interface NavercertService {
	
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
	 * @return ResponseVerify
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
	 * @return ResponseCMSStatus
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

	public String encrypt(String plainText) throws BarocertException;

}
