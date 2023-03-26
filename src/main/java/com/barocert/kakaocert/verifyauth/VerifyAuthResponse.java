package com.barocert.kakaocert.verifyauth;

public class VerifyAuthResponse {
	
	private String receiptID;
	private String scheme;
	
	public String getReceiptID() {
		return receiptID;
	}
	public void setReceiptID(String receiptID) {
		this.receiptID = receiptID;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

}
