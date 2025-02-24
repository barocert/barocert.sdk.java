package com.barocert.kakaocert.sign;

import java.util.List;

public class MultiSignResult {

    private String receiptID;
    private int state;

    private List<String> multiSignedData;

    private String ci;
    private String receiverName;
    private String receiverYear;
    private String receiverDay;
    private String receiverHP;
    private String receiverGender;
    private String receiverForeign;
    private String receiverTelcoType;

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
