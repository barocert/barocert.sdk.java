package com.barocert.kakaocert.login;

public class LoginResult {

    private String txID;
    private int state;
    private String signedData;
    private String ci;
    private String receiverName;
    private String receiverYear;
    private String receiverDay;
    private String receiverHP;
    private String receiverGender;
    private String receiverForeign;
    private String receiverTelcoType;

    public String getTxID() {
        return txID;
    }

    public void setTxID(String txID) {
        this.txID = txID;
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

    public String getReceiverGender() {
        return receiverGender;
    }

    public void setReceiverGender(String receiverGender) {
        this.receiverGender = receiverGender;
    }

    public String getReceiverForeign() {return receiverForeign;}

    public void setReceiverForeign(String receiverForeign) {this.receiverForeign = receiverForeign;}

    public String getReceiverTelcoType() {return receiverTelcoType;}

    public void setReceiverTelcoType(String receiverTelcoType) {this.receiverTelcoType = receiverTelcoType;}
}
