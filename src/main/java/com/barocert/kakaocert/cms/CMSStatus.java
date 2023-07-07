package com.barocert.kakaocert.cms;

/**
	 *  출금동의 상태확인 응답 정보
     *  @field clientCode           - 이용기관 코드
     *  @field receiptID            - 접수 아이디
     *  @field state                - 상태
     *  @field expireIn             - 요청 만료시간
     *  @field callCenterName       - 이용기관명
     *  @field callCenterNum        - 이용기관 연락처
     *  @field reqTitle             - 메시지 제목
     *  @field authCategory         - 인증 분류
     *  @field tokenType            - 원문 유형
     *  @field requestDT            - 서명요청일시
     *  @field viewDT               - 서명요청일시
     *  @field completeDT           - 서명완료일시
     *  @field expireDT             - 서명만료일시
     *  @field verifyDT             - 서명검증일시
     *  @field scheme               - 앱스킴
     *  @field appUseYN             - 앱사용유무
     *  @field returnURL            - 복귀 URL
	 */
public class CMSStatus {

    private String receiptID;
    private String clientCode;
    private int state;
    private int expireIn;
    private String callCenterName;
    private String callCenterNum;
    private String reqTitle;
    private String authCategory;
    private String returnURL;
    private String tokenType;
    private String requestDT;
    private String viewDT;
    private String completeDT;
    private String expireDT;
    private String verifyDT;
    private String scheme;
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

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getCallCenterName() {
        return callCenterName;
    }

    public void setCallCenterName(String callCenterName) {
        this.callCenterName = callCenterName;
    }

    public String getCallCenterNum() {
        return callCenterNum;
    }

    public void setCallCenterNum(String callCenterNum) {
        this.callCenterNum = callCenterNum;
    }

    public String getReqTitle() {
        return reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getAuthCategory() {
        return authCategory;
    }

    public void setAuthCategory(String authCategory) {
        this.authCategory = authCategory;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getTokenType() {
        return tokenType;
    }

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

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean getAppUseYN() {
        return appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

}
