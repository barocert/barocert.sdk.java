package com.barocert.kakaocert.identity;

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
    private String reqTitle;
    @Deprecated
    private String authCategory;
    @Deprecated
    private String returnURL;
    @Deprecated
    private String tokenType;
    private String requestDT;
    private String viewDT;
    private String completeDT;
    private String expireDT;
    private String verifyDT;
    @Deprecated
    private String scheme;
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
    public String getReqTitle() {
        return reqTitle;
    }

    @Deprecated
    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    @Deprecated
    public String getAuthCategory() {
        return authCategory;
    }

    @Deprecated
    public void setAuthCategory(String authCategory) {
        this.authCategory = authCategory;
    }

    @Deprecated
    public String getReturnURL() {
        return returnURL;
    }

    @Deprecated
    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    @Deprecated
    public String getTokenType() {
        return this.tokenType;
    }

    @Deprecated
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRequestDT() {
        return requestDT;
    }

    public void setRequestDT(String requestDT) {
        this.requestDT = requestDT;
    }

    public String getViewDT() {
        return viewDT;
    }

    public void setViewDT(String viewDT) {
        this.viewDT = viewDT;
    }

    public String getCompleteDT() {
        return completeDT;
    }

    public void setCompleteDT(String completeDT) {
        this.completeDT = completeDT;
    }

    public String getExpireDT() {
        return expireDT;
    }

    public void setExpireDT(String expireDT) {
        this.expireDT = expireDT;
    }

    public String getVerifyDT() {
        return verifyDT;
    }

    public void setVerifyDT(String verifyDT) {
        this.verifyDT = verifyDT;
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

}
