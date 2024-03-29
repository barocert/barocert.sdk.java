package com.barocert.kakaocert.cms;

public class CMS {

	private String receiverHP;
	private String receiverName;
	private String receiverBirthday;
	private String reqTitle;
	private String extraMessage;
	private Integer expireIn;
	private String returnURL;	
	private String requestCorp;
	private String bankName;
	private String bankAccountNum;
	private String bankAccountName;
	private String bankAccountBirthday;
	private String bankServiceType;
	private boolean appUseYN;

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

	public String getExtraMessage() {
		return this.extraMessage;
	}

	public void setExtraMessage(String extraMessage) {
		this.extraMessage = extraMessage;
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

	public boolean getAppUseYN() {
		return appUseYN;
	}

	public void setAppUseYN(boolean appUseYN) {
		this.appUseYN = appUseYN;
	}

}
