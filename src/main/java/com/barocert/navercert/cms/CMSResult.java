package com.barocert.navercert.cms;

public class CMSResult {

    private String receiptID;
    private String state;
    private String signedData;
    private String ci;
    private String receiverName;
    private String receiverDay;
    private String receiverYear;
    private String receiverHP;
    private String receiverGender;
    private String receiverEmail;
    private String receiverForeign;

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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverDay() {
        return receiverDay;
    }

    public void setReceiverDay(String receiverDay) {
        this.receiverDay = receiverDay;
    }

    public String getReceiverYear() {
        return receiverYear;
    }

    public void setReceiverYear(String receiverYear) {
        this.receiverYear = receiverYear;
    }

    public String getReceiverHP() {
        return receiverHP;
    }

    public void setReceiverHP(String receiverHP) {
        this.receiverHP = receiverHP;
    }

    public String getReceiverGender() {
        return receiverGender;
    }

    public void setReceiverGender(String receiverGender) {
        this.receiverGender = receiverGender;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverForeign() {
        return receiverForeign;
    }

    public void setReceiverForeign(String receiverForeign) {
        this.receiverForeign = receiverForeign;
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
