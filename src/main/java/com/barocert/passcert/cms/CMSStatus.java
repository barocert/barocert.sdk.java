package com.barocert.passcert.cms;

public class CMSStatus {

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

    public int getExpireIn() {
        return this.expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getCallCenterName() {
        return this.callCenterName;
    }

    public void setCallCenterName(String callCenterName) {
        this.callCenterName = callCenterName;
    }

    public String getCallCenterNum() {
        return this.callCenterNum;
    }

    public void setCallCenterNum(String callCenterNum) {
        this.callCenterNum = callCenterNum;
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

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public boolean getUserAgreementYN() {
        return this.userAgreementYN;
    }

    public void setUserAgreementYN(boolean userAgreementYN) {
        this.userAgreementYN = userAgreementYN;
    }

    public boolean getReceiverInfoYN() {
        return this.receiverInfoYN;
    }

    public void setReceiverInfoYN(boolean receiverInfoYN) {
        this.receiverInfoYN = receiverInfoYN;
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

    public String getOriginalTypeCode() {
        return this.originalTypeCode;
    }

    public void setOriginalTypeCode(String originalTypeCode) {
        this.originalTypeCode = originalTypeCode;
    }

    public String getOriginalURL() {
        return this.originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getOriginalFormatCode() {
        return this.originalFormatCode;
    }

    public void setOriginalFormatCode(String originalFormatCode) {
        this.originalFormatCode = originalFormatCode;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean getAppUseYN() {
        return this.appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

}
