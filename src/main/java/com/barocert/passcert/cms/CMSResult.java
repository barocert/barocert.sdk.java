package com.barocert.passcert.cms;

public class CMSResult {

    private String receiptID;
    private int state;
    private String receiverHP;
    private String receiverName;
    private String receiverDay;
    private String receiverYear;
    private String receiverGender;
    private String receiverTelcoType;
    private String signedData;
    private String ci;

    public String getReceiptID() {
        return this.receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReceiverHP() {
        return this.receiverHP;
    }

    public void setReceiverHP(String receiverHP) {
        this.receiverHP = receiverHP;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverDay() {
        return this.receiverDay;
    }

    public void setReceiverDay(String receiverDay) {
        this.receiverDay = receiverDay;
    }

    public String getReceiverYear() {
        return this.receiverYear;
    }

    public void setReceiverYear(String receiverYear) {
        this.receiverYear = receiverYear;
    }

    public String getReceiverGender() {
        return this.receiverGender;
    }

    public void setReceiverGender(String receiverGender) {
        this.receiverGender = receiverGender;
    }

    public String getReceiverTelcoType() {
        return this.receiverTelcoType;
    }

    public void setReceiverTelcoType(String telcoType) {
        this.receiverTelcoType = telcoType;
    }

    public String getSignedData() {
        return this.signedData;
    }

    public void setSignedData(String signedData) {
        this.signedData = signedData;
    }

    public String getCi() {
        return this.ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }
    
}
