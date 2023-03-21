package com.barocert;

import com.barocert.kakaocert.cms.RequestCMS;
import com.barocert.kakaocert.cms.ResultCMS;
import com.barocert.kakaocert.cms.ResultCMSState;
import com.barocert.kakaocert.cms.VerifyCMSResult;
import com.barocert.kakaocert.esign.BulkRequestESign;
import com.barocert.kakaocert.esign.BulkResultESignState;
import com.barocert.kakaocert.esign.BulkVerifyESignResult;
import com.barocert.kakaocert.esign.RequestESign;
import com.barocert.kakaocert.esign.ResultESign;
import com.barocert.kakaocert.esign.ResultESignState;
import com.barocert.kakaocert.esign.VerifyEsignResult;
import com.barocert.kakaocert.verifyauth.RequestVerifyAuth;
import com.barocert.kakaocert.verifyauth.ReqVerifyAuthResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthStateResult;
import com.barocert.kakaocert.verifyauth.VerifyAuthResult;

public interface KakaocertService {
	
	/**
	 * AES256 암호화
	 * @param plainText
	 * @return String
	 * @throws BarocertException
	 */
	public String AES256Encrypt(String plainText) throws BarocertException;
	
	/**
	 * 본인인증 요청
	 * @param ClientCode
	 * 			이용기관코드
	 * @param verifyAuthRequest
	 * 			본인인증 요청정보
	 * @param appUseYN
	 * 			App to App 방식 이용 여부
	 * @return ResultReqVerifyAuth
	 * @throws BarocertException
	 */
	public ReqVerifyAuthResult requestVerifyAuth(String ClientCode, RequestVerifyAuth verifyAuthRequest, boolean appUseYN) throws BarocertException;
	
	/**
	 * 본인인증 상태확인
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResultVerifyAuthState
	 * @throws BarocertException
	 */
	public VerifyAuthStateResult getVerifyAuthState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 본인인증 서명검증
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResultVerifyAuth
	 * @throws BarocertException
	 */
	public VerifyAuthResult verifyAuth(String ClientCode, String receiptID) throws BarocertException;
	
	
	/**
	 * 전자서명 요청(단건) 
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param esignRequest
	 * 			전자서명 요청정보
	 * @param appUseYN
	 * 			App to App 방식 이용 여부
	 * @return ResponseESign
	 * @throws BarocertException
	 */
	public ResultESign requestESign(String ClientCode, RequestESign esignRequest, boolean appUseYN) throws BarocertException;
	
	/**
	 * 전자서명 요청(다건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param esignRequest	
	 * 			전자서명 요청정보
	 * @param appUseYN
	 * 			App to App 방식 이용 여부
	 * @return ResponseESign
	 * @throws BarocertException
	 */
	public ResultESign bulkRequestESign(String ClientCode, BulkRequestESign esignRequest, boolean appUseYN) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(단건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResultESign
	 * @throws BarocertException
	 */
	public ResultESignState getESignState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 상태확인(다건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return BulkResultESign
	 * @throws BarocertException
	 */
	public BulkResultESignState getBulkESignState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(단건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResultVerifyEsign
	 * @throws BarocertException
	 */
	public VerifyEsignResult verifyESign(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(다건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return BulkVerifyResult
	 * @throws BarocertException
	 */
	public BulkVerifyESignResult bulkVerifyESign(String ClientCode, String receiptID) throws BarocertException;
	
	
	/**
	 * 출금동의 요청
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param cmsRequest
	 * 			출금동의 요청정보
	 * @param appUseYN
	 * 			App to App 방식 이용 여부
	 * @return ResponseCMS
	 * @throws BarocertException
	 */
	public ResultCMS requestCMS(String ClientCode, RequestCMS cmsRequest, boolean appUseYN) throws BarocertException;
	
	/**
	 * 출금동의 상태확인
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return ResultCMS
	 * @throws BarocertException
	 */
	public ResultCMSState getCMSState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 출금동의 서명검증
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return ResultVerifyCMS
	 * @throws BarocertException
	 */
	public VerifyCMSResult verifyCMS(String ClientCode, String receiptID) throws BarocertException;
	
}
