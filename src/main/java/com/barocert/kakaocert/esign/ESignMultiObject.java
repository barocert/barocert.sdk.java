package com.barocert.kakaocert.esign;

import java.util.List;

public class ESignMultiObject {
	
	private String clientCode;
	private String requestID;
	private String receiverHP;
	private String receiverName;
	private String receiverBirthday;
	private String ci;
	private String reqTitle;
	private Integer expireIn;
	
	private List<MultiESignTokens> tokens;
	
	private String tokenType;
	private String returnURL;
	private boolean isAppUseYN;
	
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
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
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
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
	public List<MultiESignTokens> getTokens() {
		return tokens;
	}
	public void setTokens(List<MultiESignTokens> token) {
		this.tokens = token;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getReturnURL() {
		return returnURL;
	}
	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}
	public boolean isAppUseYN() {
		return isAppUseYN;
	}
	public void setAppUseYN(boolean isAppUseYN) {
		this.isAppUseYN = isAppUseYN;
	}
}
