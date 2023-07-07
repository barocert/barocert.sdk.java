package com.barocert.kakaocert.identity;

/**
 *  본인인증 요청 응답 정보
 *  @field receiptId        - 접수아이디
 *  @field scheme           - 앱스킴
 */
public class IdentityReceipt {

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
