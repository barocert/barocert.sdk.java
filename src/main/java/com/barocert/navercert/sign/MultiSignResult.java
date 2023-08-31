package com.barocert.navercert.sign;

import java.util.List;

public class MultiSignResult {

    private String receiptID;
    private int state;

    private List<String> multiSignedData;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<String> getMultiSignedData() {
        return multiSignedData;
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
    public void setMultiSignedData(List<String> multiSignedData) {
        this.multiSignedData = multiSignedData;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

}
