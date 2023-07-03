package com.barocert.passcert.identity;

public class Identity {

    private String receiverHP;
    private String receiverName;
    private String receiverBirthday;
    private String reqTitle;
    private String reqMessage;
    private String callCenterNum;
    private Integer expireIn;
    private String token;
    private boolean userAgreementYN;
    private String telcoType;
    private String deviceOSType;
    private boolean appUseYN;
    private boolean useTssYN;

    public String getReceiverHP() {
        return this.receiverHP;
    }

    public void setReceiverHP(String receiverHP) {
        this.receiverHP = receiverHP;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverBirthday() {
        return this.receiverBirthday;
    }

    public void setReceiverBirthday(String receiverBirthday) {
        this.receiverBirthday = receiverBirthday;
    }

    public String getReqTitle() {
        return this.reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getReqMessage() {
        return this.reqMessage;
    }

    public void setReqMessage(String reqMessage) {
        this.reqMessage = reqMessage;
    }

    public String getCallCenterNum() {
        return this.callCenterNum;
    }

    public void setCallCenterNum(String callCenterNum) {
        this.callCenterNum = callCenterNum;
    }

    public Integer getExpireIn() {
        return this.expireIn;
    }

    public void setExpireIn(Integer expireIn) {
        this.expireIn = expireIn;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isUserAgreementYN() {
        return this.userAgreementYN;
    }

    public void setUserAgreementYN(boolean userAgreementYN) {
        this.userAgreementYN = userAgreementYN;
    }

    public String getTelcoType() {
        return this.telcoType;
    }

    public void setTelcoType(String telcoType) {
        this.telcoType = telcoType;
    }

    public String getDeviceOSType() {
        return this.deviceOSType;
    }

    public void setDeviceOSType(String deviceOSType) {
        this.deviceOSType = deviceOSType;
    }

    public boolean getAppUseYN() {
        return this.appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

    public boolean isUseTssYN() {
        return this.useTssYN;
    }

    public void setUseTssYN(boolean useTssYN) {
        this.useTssYN = useTssYN;
    }

            
}
