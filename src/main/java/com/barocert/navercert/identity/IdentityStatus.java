package com.barocert.navercert.identity;

public class IdentityStatus {

    private String receiptID;
    private String clientCode;
    private int state;
    @Deprecated
    private int expireIn;
    @Deprecated
    private String callCenterName;
    @Deprecated
    private String callCenterNum;
    @Deprecated
    private String returnURL;
    private String expireDT;
    @Deprecated
    private String scheme;
    @Deprecated
    private String deviceOSType;
    @Deprecated
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

    @Deprecated
    public int getExpireIn() {
        return expireIn;
    }

    @Deprecated
    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    @Deprecated
    public String getCallCenterName() {
        return callCenterName;
    }

    @Deprecated
    public void setCallCenterName(String callCenterName) {
        this.callCenterName = callCenterName;
    }

    @Deprecated
    public String getCallCenterNum() {
        return callCenterNum;
    }

    @Deprecated
    public void setCallCenterNum(String callCenterNum) {
        this.callCenterNum = callCenterNum;
    }

    @Deprecated
    public String getReturnURL() {
        return returnURL;
    }

    @Deprecated
    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getExpireDT() {
        return expireDT;
    }

    public void setExpireDT(String expireDT) {
        this.expireDT = expireDT;
    }

    @Deprecated
    public String getScheme() {
        return scheme;
    }

    @Deprecated
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Deprecated
    public String getDeviceOSType() {
        return deviceOSType;
    }

    @Deprecated
    public void setDeviceOSType(String deviceOSType) {
        this.deviceOSType = deviceOSType;
    }

    @Deprecated
    public boolean getAppUseYN() {
        return appUseYN;
    }

    @Deprecated
    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

}
