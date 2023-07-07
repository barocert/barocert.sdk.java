package com.barocert.kakaocert.sign;

/**
 *  전자서명 요청 응답 정보
 *  @field receiptId        - 접수아이디
 *  @field scheme           - 앱스킴
 */
public class MultiSignReceipt {

    private String receiptID;
    private String scheme;

    public String getReceiptID() {
        return receiptID;
    }

    public String getScheme() {
        return scheme;
    }

}
