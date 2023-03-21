package com.barocert.kakaocert.cms;

public class VerifyCMSResult {
	
	private String receiptID;
	private String requestID;
	private String state;
	private String signedData;
	private String ci;
	
	public String getReceiptID() {
		return receiptID;
	}
	public void setReceiptID(String receiptID) {
		this.receiptID = receiptID;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSignedData() {
		return signedData;
	}
	public void setSignedData(String signedData) {
		this.signedData = signedData;
	}
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	
}
