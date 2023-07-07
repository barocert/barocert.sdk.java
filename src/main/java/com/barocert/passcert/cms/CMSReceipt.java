package com.barocert.passcert.cms;

/**
 *  출금동의 요청 응답 정보
 *  @field receiptId        - 접수아이디
 *  @field scheme           - 앱스킴
 *  @field marketUrl        - 앱다운로드 URL
 */
public class CMSReceipt {

    private String receiptId;
    private String scheme;
    private String marketUrl;

    public String getReceiptID() {
        return receiptId;
    }

    public void setReceiptID(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getMarketUrl() {
        return this.marketUrl;
    }

    public void setMarketUrl(String marketUrl) {
        this.marketUrl = marketUrl;
    }

}