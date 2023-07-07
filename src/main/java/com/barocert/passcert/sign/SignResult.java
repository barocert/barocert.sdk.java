package com.barocert.passcert.sign;

/**
	 *  전자서명 검증 응답 정보
     *  @field receiptID        - 접수아이디
     *  @field state            - 상태
     *  @field receiverHP       - 수신자 휴대폰번호
     *  @field receiverName     - 수신자 성명
     *  @field receiverBirthday - 수신자 생년월일
     *  @field receiverGender   - 수신자 성별
     *  @field telcoType        - 통신사 유형
     *  @field signedData       - 전자서명 데이터 전문
     *  @field ci               - Connection Information
	 */
public class SignResult {

    private String receiptID;
    private int state;
    private String receiverHP;
    private String receiverName;
    private String receiverBirthday;
    private String receiverGender;
    private String telcoType;
    private String signedData;
    private String ci;

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

    public String getReceiverGender() {
        return this.receiverGender;
    }

    public void setReceiverGender(String receiverGender) {
        this.receiverGender = receiverGender;
    }

    public String getTelcoType() {
        return this.telcoType;
    }

    public void setTelcoType(String telcoType) {
        this.telcoType = telcoType;
    }

    public String getSignedData() {
        return this.signedData;
    }

    public void setSignedData(String signedData) {
        this.signedData = signedData;
    }

    public String getCi() {
        return this.ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }
    
}
