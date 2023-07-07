package com.barocert.kakaocert.sign;

import java.util.List;

/**
	 *  전자서명 검증 응답 정보
     *  @field receiptID        - 접수아이디
     *  @field state            - 상태
     *  @field multiSignedData  - 전자서명값
     *  @field ci               - Connection Information
	 */
public class MultiSignResult {

    private String receiptID;
    private int state;

    private List<String> multiSignedData;

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
