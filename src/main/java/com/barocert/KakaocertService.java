package com.barocert;

import com.barocert.kakaocert.cms.RequestCMS;
import com.barocert.kakaocert.cms.ResponseCMS;
import com.barocert.kakaocert.cms.ResultCMS;
import com.barocert.kakaocert.cms.ResultVerifyCMS;
import com.barocert.kakaocert.esign.BulkRequestESign;
import com.barocert.kakaocert.esign.BulkResultESign;
import com.barocert.kakaocert.esign.BulkVerifyResult;
import com.barocert.kakaocert.esign.RequestESign;
import com.barocert.kakaocert.esign.ResponseESign;
import com.barocert.kakaocert.esign.ResultESign;
import com.barocert.kakaocert.esign.ResultVerifyEsign;
import com.barocert.kakaocert.verifyauth.RequestVerifyAuth;
import com.barocert.kakaocert.verifyauth.ResultReqVerifyAuth;
import com.barocert.kakaocert.verifyauth.ResultVerifyAuth;
import com.barocert.kakaocert.verifyauth.ResultVerifyAuthState;

public interface KakaocertService {
	
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
	public ResponseESign requestESign(String ClientCode, RequestESign esignRequest, boolean appUseYN) throws BarocertException;
	
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
	public ResponseESign bulkRequestESign(String ClientCode, BulkRequestESign esignRequest, boolean appUseYN) throws BarocertException;
	
	/**
	 * 전자서명 상태 확인(단건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return ResultESign
	 * @throws BarocertException
	 */
	public ResultESign getESignState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 상태 확인(다건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return BulkResultESign
	 * @throws BarocertException
	 */
	public BulkResultESign getBulkESignState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 전자서명 서명검증(단건)
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			전자서명 접수아이디
	 * @return VerifyResult
	 * @throws BarocertException
	 */
	public ResultVerifyEsign verifyESign(String ClientCode, String receiptID) throws BarocertException;
	
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
	public BulkVerifyResult bulkVerifyESign(String ClientCode, String receiptID) throws BarocertException;
	
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
	public ResultReqVerifyAuth requestVerifyAuth(String ClientCode, RequestVerifyAuth verifyAuthRequest, boolean appUseYN) throws BarocertException;
	
	/**
	 * 본인인증 상태 확인
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResultVerifyAuthState
	 * @throws BarocertException
	 */
	public ResultVerifyAuthState getVerifyAuthState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 본인인증 서명 검증
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			본인인증 접수아이디
	 * @return ResultVerifyAuth
	 * @throws BarocertException
	 */
	public ResultVerifyAuth verifyAuth(String ClientCode, String receiptID) throws BarocertException;
	
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
	public ResponseCMS requestCMS(String ClientCode, RequestCMS cmsRequest, boolean appUseYN) throws BarocertException;
	
	/**
	 * 출금동의 상태 확인
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return ResultCMS
	 * @throws BarocertException
	 */
	public ResultCMS getCMSState(String ClientCode, String receiptID) throws BarocertException;
	
	/**
	 * 출금동의 서명 검증
	 * 
	 * @param ClientCode
	 * 			이용기관코드
	 * @param receiptID
	 * 			출금동의 접수아이디
	 * @return VerifyResult
	 * @throws BarocertException
	 */
	public ResultVerifyCMS verifyCMS(String ClientCode, String receiptID) throws BarocertException;
	
}
