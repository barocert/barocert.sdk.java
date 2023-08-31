package com.barocert.navercert.identity;

public class IdentityStatus {

    private String receiptID;
    private String clientCode;
    private int state;
    private int expireIn;
    private String callCenterName;
    private String callCenterNum;
    private String returnURL;
    private String requestDT;
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

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getRequestDT() {
        return requestDT;
    }

    public void setRequestDT(String requestDT) {
        this.requestDT = requestDT;
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

    public String getDeviceOSType() {
        return deviceOSType;
    }

    public void setDeviceOSType(String deviceOSType) {
        this.deviceOSType = deviceOSType;
    }

    public boolean getAppUseYN() {
        return appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

}
