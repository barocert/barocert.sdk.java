package com.barocert.kakaocert.sign;

public class Sign {

    private String receiverHP;
    private String receiverName;
    private String receiverBirthday;
    private String reqTitle;
    private String signTitle;
    private String extraMessage;
    private Integer expireIn;
    private String token;
    private String tokenType;
    private String returnURL;
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

    public String getSignTitle() {
        return this.signTitle;
    }

    public void setSignTitle(String signTitle) {
        this.signTitle = signTitle;
    }

    public String getExtraMessage() {
        return this.extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
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
