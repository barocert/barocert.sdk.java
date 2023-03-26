package com.barocert;

import com.barocert.kakaocert.cms.CMSRequest;
import com.barocert.kakaocert.cms.CMSResponse;
import com.barocert.kakaocert.cms.CMSStateResult;
import com.barocert.kakaocert.cms.CMSVerifyResult;
import com.barocert.kakaocert.esign.ESMultiResponse;
import com.barocert.kakaocert.esign.ESMultiRequest;
import com.barocert.kakaocert.esign.MultiStateResult;
import com.barocert.kakaocert.esign.ESMultiVerifyResult;
import com.barocert.kakaocert.esign.ESRequest;
import com.barocert.kakaocert.esign.ESResponse;
import com.barocert.kakaocert.esign.ESStateResult;
import com.barocert.kakaocert.esign.ESVerifyResult;
import com.barocert.kakaocert.verifyauth.VARequest;
import com.barocert.kakaocert.verifyauth.VAResponse;
import com.barocert.kakaocert.verifyauth.VAStateResult;
import com.barocert.kakaocert.verifyauth.VAVerifyResult;

public interface KakaocertService {
	
	/**
	 * AES256 암호화
	 * 
	 * @param plainText
	 * @return String
	 * @throws BarocertException
	 */
	public String AES256Encrypt(String plainText) throws BarocertException;
	
	/**
	 * 본인인증 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param vARequest
	 * 			본인인증 요청정보
	 * @param isAppUseYN
	 * 			App to App 방식 이용 여부
	 * @return VAResponse
	 * @throws BarocertException
	 */
	public VAResponse requestVerifyAuth(String clientCode, VARequest vARequest) throws BarocertException;
	
	/**
	 * 본인인증 상태확인
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return VAStateResult
	 * @throws BarocertException
	 */
	public VAStateResult getVerifyAuthState(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 본인인증 서명검증
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return VAVerifyResult
	 * @throws BarocertException
	 */
	public VAVerifyResult verifyAuth(String clientCode, String receiptID) throws BarocertException;
	
	
	/**
	 * 전자서명 요청(단건) 
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param eSRequest
	 * 			전자서명 요청정보
	 * @param isAppUseYN
	 * 			App to App 방식 이용 여부
	 * @return ESResponse
	 * @throws BarocertException
	 */
	public ESResponse requestESign(String clientCode, ESRequest eSRequest) throws BarocertException;
	
	/**
	 * 전자서명 요청(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param eSMultiRequest	
	 * 			전자서명 요청정보
	 * @return ESMultiResponse
	 * @throws BarocertException
	 */
	public ESMultiResponse requestMultiESign(String clientCode, ESMultiRequest eSMultiRequest) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ESStateResult
	 * @throws BarocertException
	 */
	public ESStateResult getESignState(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return MultiStateResult
	 * @throws BarocertException
	 */
	public MultiStateResult getMultiESignState(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(단건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ESVerifyResult
	 * @throws BarocertException
	 */
	public ESVerifyResult verifyESign(String clientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(다건)
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ESMultiVerifyResult
	 * @throws BarocertException
	 */
	public ESMultiVerifyResult multiVerifyESign(String clientCode, String receiptID) throws BarocertException;
	
	
	/**
	 * 출금동의 요청
	 * 
	 * @param clientCode
	 * 			이용기관코드
	 * @param cMSRequest
	 * 			출금동의 요청정보
	 * @param isAppUseYN
	 * 			App to App 방식 이용 여부
	 * @return CMSResponse
	 * @throws BarocertException
	 */
	public CMSResponse requestCMS(String clientCode, CMSRequest cMSRequest) throws BarocertException;
	
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
	public CMSVerifyResult verifyCMS(String clientCode, String receiptID) throws BarocertException;
	
}
