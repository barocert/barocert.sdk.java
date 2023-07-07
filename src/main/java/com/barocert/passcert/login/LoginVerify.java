package com.barocert.passcert.login;

/**
	 *  간편로그인 검증 요청 정보
     *  @field receiverHP           - 수신자 휴대폰번호
     *  @field receiverName         - 수신자 성명
	 */
public class LoginVerify {

    private String receiverHP;
    private String receiverName;

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
    
}
