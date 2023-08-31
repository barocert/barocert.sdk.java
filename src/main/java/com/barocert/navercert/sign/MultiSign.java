package com.barocert.navercert.sign;

import java.util.ArrayList;
import java.util.List;

public class MultiSign {

    private String receiverHP;
    private String receiverName;
    private String receiverBirthday;
    private String reqTitle;
    private String reqMessage;
    private String callCenterNum;
    private Integer expireIn;
    private String returnURL;
    private String deviceOSType;
    private boolean appUseYN;

    private List<MultiSignTokens> tokens = new ArrayList<MultiSignTokens>();


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

    public List<MultiSignTokens> getTokens() {
        return tokens;
    }

    public void setTokens(List<MultiSignTokens> tokens) {
        this.tokens = tokens;
    }

    public boolean getAppUseYN() {
        return appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

    public void addToken(MultiSignTokens token) {
        this.tokens.add(token);
    }

}
