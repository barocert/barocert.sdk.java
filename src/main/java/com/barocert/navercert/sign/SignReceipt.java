package com.barocert.navercert.sign;

public class SignReceipt {

    private String receiptID;
    private String scheme;
    private String marketUrl;

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getMarketUrl() {
        return marketUrl;
    }

    public void setMarketUrl(String marketUrl) {
        this.marketUrl = marketUrl;
    }

    public String getScheme() {
        return scheme;
    }

}
