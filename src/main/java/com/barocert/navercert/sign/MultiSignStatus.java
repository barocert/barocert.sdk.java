package com.barocert.navercert.sign;

public class MultiSignStatus {

    private String receiptID;
    private String clientCode;
    private int state;
    private int expireIn;
    private String callCenterName;
    private String callCenterNum;
    private String reqTitle;
    private String returnURL;
    private String tokenType;
    private String expireDT;
    private String scheme;
    private String deviceOSType;
    private boolean appUseYN;

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getCallCenterName() {
        return callCenterName;
    }

    public void setCallCenterName(String callCenterName) {
        this.callCenterName = callCenterName;
    }

    public String getCallCenterNum() {
        return callCenterNum;
    }

    public void setCallCenterNum(String callCenterNum) {
        this.callCenterNum = callCenterNum;
    }

    public String getReqTitle() {
        return reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getExpireDT() {
        return expireDT;
    }

    public void setExpireDT(String expireDT) {
        this.expireDT = expireDT;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean getAppUseYN() {
        return appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

    public String getDeviceOSType() {
        return deviceOSType;
    }

    public void setDeviceOSType(String deviceOSType) {
        this.deviceOSType = deviceOSType;
    }
}