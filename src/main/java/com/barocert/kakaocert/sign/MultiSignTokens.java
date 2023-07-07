package com.barocert.kakaocert.sign;

/**
 *  전자서명(복수) 원문 개별 객체
 *  @field reqTitle        - 인증요청 메시지 제목
 *  @field token           - 원문
 */
public class MultiSignTokens {

    private String reqTitle;
    private String token;

    public String getReqTitle() {
        return reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
