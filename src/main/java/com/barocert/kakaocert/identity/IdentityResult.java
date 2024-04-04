package com.barocert.kakaocert.identity;

public class IdentityResult {

    private String receiptID;
    private int state;
    private String signedData;
    private String ci;
    private String receiverName;
    private String receiverYear;
    private String receiverDay;
    private String receiverHP;

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
    
    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverYear() { 
        return receiverYear; 
    }

    public void setReceiverYear(String receiverYear) { 
        this.receiverYear = receiverYear; 
    }

    public String getReceiverDay() { 
        return receiverDay; 
    }

    public void setReceiverDay(String receiverDay) { 
        this.receiverDay = receiverDay; 
    }

    public String getReceiverHP() {
        return this.receiverHP;
    }

    public void setReceiverHP(String receiverHP) {
        this.receiverHP = receiverHP;
    }
}
