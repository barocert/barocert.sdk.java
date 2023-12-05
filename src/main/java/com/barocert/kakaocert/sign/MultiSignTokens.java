package com.barocert.kakaocert.sign;

public class MultiSignTokens {

    private String reqTitle;
    private String signTitle;
    private String token;

    @Deprecated
    public String getReqTitle() {
        return reqTitle;
    }

    @Deprecated
    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getSignTitle() {
        return this.signTitle;
    }

    public void setSignTitle(String signTitle) {
        this.signTitle = signTitle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
