package com.barocert.navercert.sign;

public class Sign {

    private String receiverHP;
    private String receiverName;
    private String receiverBirthday;

    private String reqTitle;
    private String reqMessage;
    private String callCenterNum;
    private Integer expireIn;

    private String token;
    private String tokenType;

    private String returnURL;
    private String deviceOSType;
    private boolean appUseYN;

    public String getReceiverHP() {
        return receiverHP;
    }

    public void setReceiverHP(String receiverHP) {
        this.receiverHP = receiverHP;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverBirthday() {
        return receiverBirthday;
    }

    public void setReceiverBirthday(String receiverBirthday) {
        this.receiverBirthday = receiverBirthday;
    }

    public String getReqTitle() {
        return reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getReqMessage() {
        return reqMessage;
    }

    public void setReqMessage(String reqMessage) {
        this.reqMessage = reqMessage;
    }

    public String getCallCenterNum() {
        return callCenterNum;
    }

    public void setCallCenterNum(String callCenterNum) {
        this.callCenterNum = callCenterNum;
    }

    public Integer getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Integer expireIn) {
        this.expireIn = expireIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getDeviceOSType() {
        return deviceOSType;
    }

    public void setDeviceOSType(String deviceOSType) {
        this.deviceOSType = deviceOSType;
    }

    public boolean isAppUseYN() {
        return appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }
}
