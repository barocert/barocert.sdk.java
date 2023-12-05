package com.barocert.navercert.sign;

import java.util.List;

public class MultiSignStatus {

    private String receiptID;
    private String clientCode;
    private int state;
    private int expireIn;
    private String callCenterName;
    private String callCenterNum;
    private String reqTitle;
    private String returnURL;
    private List<String> tokenTypes;
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
    public String getReqTitle() {
        return reqTitle;
    }

    @Deprecated
    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
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
    public boolean getAppUseYN() {
        return appUseYN;
    }

    @Deprecated
    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
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
    public List<String> getTokenTypes() {
        return tokenTypes;
    }

    @Deprecated
    public void setTokenTypes(List<String> tokenTypes) {
        this.tokenTypes = tokenTypes;
    }
}