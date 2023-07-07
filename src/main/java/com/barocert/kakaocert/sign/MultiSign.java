package com.barocert.kakaocert.sign;

import java.util.ArrayList;
import java.util.List;

/**
 *  전자서명 요청 정보
 *  @field requestID        - 접수 아이디
 *  @field receiverHP       - 수신자 휴대폰번호
 *  @field receiverName     - 수신자 성명
 *  @field receiverBirthday - 수신자 생년월일
 *  @field reqTitle         - 메시지 제목
 *  @field expireIn         - 요청 만료시간
 *  @field tokens           - 원문(최대 20건)
 *  @field tokenType        - 원문 유형
 *  @field appUseYN         - 앱사용유무
 *  @field returnURL        - 복귀 URL
 */
public class MultiSign {

    private String requestID;
    private String receiverHP;
    private String receiverName;
    private String receiverBirthday;
    private String reqTitle;
    private Integer expireIn;

    private List<MultiSignTokens> tokens = new ArrayList<MultiSignTokens>();

    private String tokenType;
    private String returnURL;
    private boolean appUseYN;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

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

    public Integer getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Integer expireIn) {
        this.expireIn = expireIn;
    }

    public List<MultiSignTokens> getTokens() {
        return tokens;
    }
    
    public void addToken(MultiSignTokens token) {
        this.tokens.add(token);
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public boolean isAppUseYN() {
        return appUseYN;
    }

    public void setAppUseYN(boolean appUseYN) {
        this.appUseYN = appUseYN;
    }

}
