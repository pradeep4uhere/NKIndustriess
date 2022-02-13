package com.example.nkindustries.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Constants {

    public static String loginStatus = "LOGIN_STATUS";
    public static String otpVerification = "OTP_VERIFICATION";
    public static String mobileNumber = "MOBILE_NUMBER";
    public static String firstName = "FIRST_NAME";
    public static String lastName = "LAST_NAME";
    public static String email = "EMAIL";
    public static String userId = "ID";
    public static String agentCode = "AGENT_CODE";
    public static String walletBalance = "WALLET_BALANCE";
    public static String fcmToken = "FCM_TOKEN";

    public static String getAuthToken() {
        byte[] data = new byte[0];
        try {
            data = ("rzp_live_FBqFABOTjOOAmo"+ ":" + "NbZbPr3QpS4qSyZqTlCgjrHy").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }

}
