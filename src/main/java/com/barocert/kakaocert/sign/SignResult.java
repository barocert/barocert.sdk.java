package com.barocert.kakaocert.sign;

/**
	 *  전자서명 검증 응답 정보
     *  @field receiptID        - 접수아이디
     *  @field state            - 상태
     *  @field signedData       - 전자서명값
     *  @field ci               - Connection Information
	 */
public class SignResult {

    private String receiptID;
    private int state;
    private String signedData;
    private String ci;

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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
