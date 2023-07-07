package com.barocert.passcert.identity;

/**
	 *  본인인증 상태확인 응답 정보
     *  @field clientCode           - 이용기관 코드
     *  @field receiptID            - 접수 아이디
     *  @field state                - 상태
     *  @field expireIn             - 요청 만료시간
     *  @field callCenterName       - 이용기관명
     *  @field callCenterNum        - 이용기관 연락처
     *  @field reqTitle             - 메시지 제목
     *  @field reqMessage           - 메시지 내용
     *  @field requestDT            - 서명요청일시
     *  @field completeDT           - 서명완료일시
     *  @field expireDT             - 서명만료일시
     *  @field rejectDT             - 서명거절일시
     *  @field tokenType            - 원문 유형
     *  @field userAgreementYN      - 사용자동의 필요 여부
     *  @field telcoType            - 통신사 유형
     *  @field deviceOSType         - 모바일장비 유형
     *  @field scheme               - 앱스킴
     *  @field appUseYN             - 앱사용유무
	 */
public class IdentityStatus {

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
    private String telcoType;
    private String deviceOSType;
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
