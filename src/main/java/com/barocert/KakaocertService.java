package com.barocert;

import com.barocert.kakaocert.cms.CMSObject;
import com.barocert.kakaocert.cms.CMSResponse;
import com.barocert.kakaocert.cms.CMSStateResult;
import com.barocert.kakaocert.cms.CMSVerifyResult;
import com.barocert.kakaocert.esign.ESignMultiResponse;
import com.barocert.kakaocert.esign.ESignMultiObject;
import com.barocert.kakaocert.esign.MultiESignStateResult;
import com.barocert.kakaocert.esign.MultiESignVerifyResult;
import com.barocert.kakaocert.esign.ESignObject;
import com.barocert.kakaocert.esign.ESignResponse;
import com.barocert.kakaocert.esign.ESignStateResult;
import com.barocert.kakaocert.esign.ESignVerifyResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthObject;
import com.barocert.kakaocert.verifyauth.VerifyAuthResponse;
import com.barocert.kakaocert.verifyauth.VerifyAuthStateResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthResult;

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
	public VerifyAuthResponse verifyAuthRequest(String clientCode, VerifyAuthObject verifyAuthObject) throws BarocertException;
	
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
	public VerifyAuthStateResult getVerifyAuthState(String clientCode, String receiptID) throws BarocertException;
	
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
	public VerifyAuthResult verifyAuth(String clientCode, String receiptID) throws BarocertException;
	
	
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
	public ESignResponse eSignRequest(String clientCode, ESignObject eSignObject) throws BarocertException;
	
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
	public ESignMultiResponse eSignMultiRequest(String clientCode, ESignMultiObject eSignMultiObject) throws BarocertException;
	
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
	public ESignStateResult getESignState(String clientCode, String receiptID) throws BarocertException;
	
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
	public MultiESignStateResult getMultiESignState(String clientCode, String receiptID) throws BarocertException;
	
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
	public ESignVerifyResult eSignVerify(String clientCode, String receiptID) throws BarocertException;
	
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
	public MultiESignVerifyResult multiESignVerify(String clientCode, String receiptID) throws BarocertException;
	
	
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
	public CMSResponse cMSRequest(String clientCode, CMSObject cMSObject) throws BarocertException;
	
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
	public CMSStateResult getCMSState(String clientCode, String receiptID) throws BarocertException;
	
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
	public CMSVerifyResult cMSVerify(String clientCode, String receiptID) throws BarocertException;
	
	
	/**
	 * AES256 CBC 암호화
	 * 
	 * @param plainText
	 * @return String
	 * @throws BarocertException
	 */
	public String AES256Encrypt(String plainText) throws BarocertException;
}
