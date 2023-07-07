package com.barocert.kakaocert.cms;

/**
 *  출금동의 요청 정보
 * 	@field requestID		- 접수아이디
 *  @field receiverHP       - 수신자 휴대폰번호
 *  @field receiverName     - 수신자 성명
 *  @field receiverBirthday - 수신자 생년월일
 *  @field reqTitle         - 메시지 제목
 *  @field expireIn         - 요청 만료시간
 *  @field requestCorp      - 청구기관명
 *  @field bankName  		- 출금은행명
 *  @field bankAccountNum  	- 출금계좌번호
 *  @field bankAccountName  - 출금계좌 예금주명
 *  @field bankAccountBirthday - 출금계좌 예금주 생년월일
 *  @field bankServiceType  - 서비스종류
 *  @field appUseYN         - 앱사용유무
 *	@field returnURL        - 복귀URL
 */
public class CMS {

	private String requestID;
	private String receiverHP;
	private String receiverName;
	private String receiverBirthday;
	private String reqTitle;
	private Integer expireIn;
	private String returnURL;	
	private String requestCorp;
	private String bankName;
	private String bankAccountNum;
	private String bankAccountName;
	private String bankAccountBirthday;
	private String bankServiceType;
	private boolean appUseYN;

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getReceiverHP() {
		return receiverHP;
	}

	public void setReceiverHP(String receiverHP) {
		this.receiverHP = receiverHP;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverBirthday() {
		return receiverBirthday;
	}

	public void setReceiverBirthday(String receiverBirthday) {
		this.receiverBirthday = receiverBirthday;
	}

	public String getReqTitle() {
		return reqTitle;
	}

	public void setReqTitle(String reqTitle) {
		this.reqTitle = reqTitle;
	}

	public Integer getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(Integer expireIn) {
		this.expireIn = expireIn;
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public String getRequestCorp() {
		return requestCorp;
	}

	public void setRequestCorp(String requestCorp) {
		this.requestCorp = requestCorp;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNum() {
		return bankAccountNum;
	}

	public void setBankAccountNum(String bankAccountNum) {
		this.bankAccountNum = bankAccountNum;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getBankAccountBirthday() {
		return bankAccountBirthday;
	}

	public void setBankAccountBirthday(String bankAccountBirthday) {
		this.bankAccountBirthday = bankAccountBirthday;
	}

	public String getBankServiceType() {
		return bankServiceType;
	}

	public void setBankServiceType(String bankServiceType) {
		this.bankServiceType = bankServiceType;
	}

	public boolean isAppUseYN() {
		return appUseYN;
	}

	public void setAppUseYN(boolean appUseYN) {
		this.appUseYN = appUseYN;
	}

}
