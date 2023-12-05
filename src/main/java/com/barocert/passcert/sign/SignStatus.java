package com.barocert.passcert.sign;

public class SignStatus {

    private String clientCode;
    private String receiptID;
    private int state;
    private int expireIn;
    private String callCenterName;
    private String callCenterNum;
    private String reqTitle;
    private String reqMessage;
    private String requestDT;
    private String completeDT;
    private String expireDT;
    private String rejectDT;
    private String tokenType;
    private boolean userAgreementYN;
    private boolean receiverInfoYN;
    private String telcoType;
    private String deviceOSType;
    private String originalTypeCode;
    private String originalURL;
    private String originalFormatCode;
    private String scheme;
    private boolean appUseYN;

    public String getClientCode() {
        return this.clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getReceiptID() {
        return this.receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Deprecated
    public int getExpireIn() {
        return this.expireIn;
    }

    @Deprecated
    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    @Deprecated
    public String getCallCenterName() {
        return this.callCenterName;
    }

    @Deprecated
    public void setCallCenterName(String callCenterName) {
        this.callCenterName = callCenterName;
    }

    @Deprecated
    public String getCallCenterNum() {
        return this.callCenterNum;
    }

    @Deprecated
    public void setCallCenterNum(String callCenterNum) {
        this.callCenterNum = callCenterNum;
    }

    @Deprecated
    public String getReqTitle() {
        return this.reqTitle;
    }

    @Deprecated
    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    @Deprecated
    public String getReqMessage() {
        return this.reqMessage;
    }

    @Deprecated
    public void setReqMessage(String reqMessage) {
        this.reqMessage = reqMessage;
    }

    public String getRequestDT() {
        return this.requestDT;
    }

    public void setRequestDT(String requestDT) {
        this.requestDT = requestDT;
    }

    public String getCompleteDT() {
        return this.completeDT;
    }

    public void setCompleteDT(String completeDT) {
        this.completeDT = completeDT;
    }

    public String getExpireDT() {
        return this.expireDT;
    }

    public void setExpireDT(String expireDT) {
        this.expireDT = expireDT;
    }

    public String getRejectDT() {
        return this.rejectDT;
    }

    public void setRejectDT(String rejectDT) {
        this.rejectDT = rejectDT;
    }

    @Deprecated
    public String getTokenType() {
        return this.tokenType;
    }

    @Deprecated
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Deprecated
    public boolean getUserAgreementYN() {
        return this.userAgreementYN;
    }

    @Deprecated
    public void setUserAgreementYN(boolean userAgreementYN) {
        this.userAgreementYN = userAgreementYN;
    }

    @Deprecated
    public boolean getReceiverInfoYN() {
        return this.receiverInfoYN;
    }

    @Deprecated
    public void setReceiverInfoYN(boolean receiverInfoYN) {
        this.receiverInfoYN = receiverInfoYN;
    }

    @Deprecated
    public String getTelcoType() {
        return this.telcoType;
    }

    @Deprecated
    public void setTelcoType(String telcoType) {
        this.telcoType = telcoType;
    }

    @Deprecated
    public String getDeviceOSType() {
        return this.deviceOSType;
    }

    @Deprecated
    public void setDeviceOSType(String deviceOSType) {
        this.deviceOSType = deviceOSType;
    }

    @Deprecated
    public String getOriginalTypeCode() {
        return this.originalTypeCode;
    }

    @Deprecated
    public void setOriginalTypeCode(String originalTypeCode) {
        this.originalTypeCode = originalTypeCode;
    }

    @Deprecated
    public String getOriginalURL() {
        return this.originalURL;
    }

    @Deprecated
    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    @Deprecated
    public String getOriginalFormatCode() {
        return this.originalFormatCode;
    }

    @Deprecated
    public void setOriginalFormatCode(String originalFormatCode) {
        this.originalFormatCode = originalFormatCode;
    }

    @Deprecated
    public String getScheme() {
        return this.scheme;
    }

    @Deprecated
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Deprecated
    public boolean getAppUseYN() {
        return this.appUseYN;
    }

    @Deprecated
    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

}