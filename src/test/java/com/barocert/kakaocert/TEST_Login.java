package com.barocert.kakaocert;

import org.junit.Test;
import com.barocert.BarocertException;
import com.barocert.kakaocert.login.LoginResult;

public class TEST_Login {

    private final String testLinkID = "TESTER";
    private final String testSecretKey = "SwWxqU+0TErBXy/9TVjIPEnI0VTUMMSQZtJf3Ed8q3I=";

    private KakaocertService kakaocertService;

    public TEST_Login() {
        KakaocertServiceImp service = new KakaocertServiceImp();
        service.setLinkID(testLinkID);
        service.setSecretKey(testSecretKey);
        service.setIPRestrictOnOff(true);
        service.setUseStaticIP(false);
        kakaocertService = service;
    }

    /*
     * 완료된 전자서명을 검증하고 전자서명 데이터 전문(signedData)을 반환 받습니다.
     * 카카오 보안정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류가 반환됩니다.
     * 전자서명 완료일시로부터 10분 이후에 검증 API를 호출하면 오류가 반환됩니다.
     * https://developers.barocert.com/reference/kakao/java/login/api#VerifyLogin
     */
    @Test
    public void TEST_VerifyLogin() throws BarocertException {
        try {
            // 검증하기 API는 완료된 전자서명 요청당 1회만 요청 가능하며, 사용자가 서명을 완료후 유효시간(10분)이내에만 요청가능 합니다.
            LoginResult result = kakaocertService.verifyLogin("023040000001", "01daa94d3f-5ac9-429c-8661-40d0ad9ce3e3");

            System.out.println("txID : " + result.getTxID());
            System.out.println("State : " + result.getState()); // 대기(0),완료(1),만료(2),거절(3),실패(4)
            System.out.println("SignedData : " + result.getSignedData());
            System.out.println("ci : " + result.getCi());
            System.out.println("ReceiverName : " + result.getReceiverName());
            System.out.println("ReceiverYear : " + result.getReceiverYear());
            System.out.println("ReceiverDay : " + result.getReceiverDay());
            System.out.println("ReceiverHP : " + result.getReceiverHP());
        } catch (BarocertException be) {
            System.out.println("Code : " + be.getCode());
            System.out.println("Message : " + be.getMessage());
        }
    }

}