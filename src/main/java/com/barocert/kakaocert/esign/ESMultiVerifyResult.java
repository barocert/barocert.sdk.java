package com.barocert.kakaocert.esign;

import java.util.List;

public class ESMultiVerifyResult {
	
	private String receiptID;
	private String state;
	private List<String> bulkSignedData;
	private String ci;
	
	public String getReceiptID() {
		return receiptID;
	}
	public void setReceiptID(String receiptID) {
		this.receiptID = receiptID;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<String> getBulkSignedData() {
		return bulkSignedData;
	}
	public void setBulkSignedData(List<String> bulkSignedData) {
		this.bulkSignedData = bulkSignedData;
	}
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	
}
