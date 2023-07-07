package com.barocert.passcert.cms;

/**
 *  출금동의 요청 정보
 *  @field receiverHP       - 수신자 휴대폰번호
 *  @field receiverName     - 수신자 성명
 *  @field receiverBirthday - 수신자 생년월일
 *  @field reqTitle         - 메시지 제목
 *  @field reqMessage       - 메시지 내용
 *  @field callCenterNum    - 고객센터 연락처
 *  @field expireIn         - 요청 만료시간
 *  @field userAgreementYN  - 사용자동의 필요 여부
 *  @field bankName  		- 출금은행명
 *  @field bankAccountNum  	- 출금계좌번호
 *  @field bankAccountName  - 출금계좌 예금주명
 *  @field bankWithdraw     - 출금액
 *  @field bankServiceType  - 서비스종류
 *  @field telcoType        - 통신사 유형
 *  @field deviceOSType     - 모바일장비 유형
 *  @field appUseYN         - 앱사용유무
 *  @field useTssYN         - Tss 사용 여부
 */
public class CMS {

	private String receiverHP;
	private String receiverName;
	private String receiverBirthday;
	private String reqTitle;
	private String reqMessage;
	private String callCenterNum;
	private Integer expireIn;
	private boolean userAgreementYN;
	private String bankName;
	private String bankAccountNum;
	private String bankAccountName;
	private String bankWithdraw;
	private String bankServiceType;
	private String telcoType;
	private String deviceOSType;
	private boolean appUseYN;
	private boolean useTssYN;
	private String originalTypeCode;
	private String originalURL;
	private String originalFormatCode;

	public String getOriginalURL() {
		return this.originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getOriginalFormatCode() {
		return this.originalFormatCode;
	}

	public void setOriginalFormatCode(String originalFormatCode) {
		this.originalFormatCode = originalFormatCode;
	}

	public String getOriginalTypeCode() {
		return this.originalTypeCode;
	}

	public void setOriginalTypeCode(String originalTypeCode) {
		this.originalTypeCode = originalTypeCode;
	}



	public String getReceiverHP() {
		return this.receiverHP;
	}

	public void setReceiverHP(String receiverHP) {
		this.receiverHP = receiverHP;
	}

	public String getReceiverName() {
		return this.receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverBirthday() {
		return this.receiverBirthday;
	}

	public void setReceiverBirthday(String receiverBirthday) {
		this.receiverBirthday = receiverBirthday;
	}

	public String getReqTitle() {
		return this.reqTitle;
	}

	public void setReqTitle(String reqTitle) {
		this.reqTitle = reqTitle;
	}

	public String getReqMessage() {
		return this.reqMessage;
	}

	public void setReqMessage(String reqMessage) {
		this.reqMessage = reqMessage;
	}

	public String getCallCenterNum() {
		return this.callCenterNum;
	}

	public void setCallCenterNum(String callCenterNum) {
		this.callCenterNum = callCenterNum;
	}

	public Integer getExpireIn() {
		return this.expireIn;
	}

	public void setExpireIn(Integer expireIn) {
		this.expireIn = expireIn;
	}

	public boolean isUserAgreementYN() {
		return this.userAgreementYN;
	}

	public void setUserAgreementYN(boolean userAgreementYN) {
		this.userAgreementYN = userAgreementYN;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNum() {
		return this.bankAccountNum;
	}

	public void setBankAccountNum(String bankAccountNum) {
		this.bankAccountNum = bankAccountNum;
	}

	public String getBankAccountName() {
		return this.bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getBankWithdraw() {
		return this.bankWithdraw;
	}

	public void setBankWithdraw(String bankWithdraw) {
		this.bankWithdraw = bankWithdraw;
	}

	public String getBankServiceType() {
		return this.bankServiceType;
	}

	public void setBankServiceType(String bankServiceType) {
		this.bankServiceType = bankServiceType;
	}

	public String getTelcoType() {
		return this.telcoType;
	}

	public void setTelcoType(String telcoType) {
		this.telcoType = telcoType;
	}

	public String getDeviceOSType() {
		return this.deviceOSType;
	}

	public void setDeviceOSType(String deviceOSType) {
		this.deviceOSType = deviceOSType;
	}

	public boolean isAppUseYN() {
		return this.appUseYN;
	}

	public void setAppUseYN(boolean appUseYN) {
		this.appUseYN = appUseYN;
	}

	public boolean isUseTssYN() {
		return this.useTssYN;
	}

	public void setUseTssYN(boolean useTssYN) {
		this.useTssYN = useTssYN;
	}
	
}
