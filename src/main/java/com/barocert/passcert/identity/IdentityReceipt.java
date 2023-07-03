package com.barocert.passcert.identity;

public class IdentityReceipt {

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
