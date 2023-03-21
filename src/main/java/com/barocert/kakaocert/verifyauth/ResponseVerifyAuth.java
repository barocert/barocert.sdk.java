package com.barocert.kakaocert.verifyauth;

public class ResponseVerifyAuth {
	
	private String clientCode;
	private String receiptID;
	
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getReceiptID() {
		return receiptID;
	}
	public void setReceiptID(String receiptID) {
		this.receiptID = receiptID;
	}
	
}
