package com.barocert.kakaocert.sign;

public class RequestSign {

    private String requestID;
    private String receiverHP;
    private String receiverName;
    private String receiverBirthday;
    private String ci;
    private String reqTitle;
    private Integer expireIn;
    private String token;
    private String tokenType;
    private String returnURL;
    private boolean appUseYN;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

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

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getReqTitle() {
        return reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
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

    public boolean getAppUseYN() {
        return appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

}
