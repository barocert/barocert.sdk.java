package com.barocert.kakaocert.esign;

import java.util.List;

public class MultiESignVerifyResult {
	
	private String receiptID;
	private String state;
	private List<String> multiSignedData;
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
	public List<String> getMultiSignedData() {
		return multiSignedData;
	}
	public void setMultiSignedData(List<String> multiSignedData) {
		this.multiSignedData = multiSignedData;
	}
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	
}
