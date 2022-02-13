package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    public boolean success;
    public Data data;
    public String message;
    public class Data{
        public int id;
        @SerializedName("AgentCode")
        public String agentCode;
        public String firstName;
        public String lastName;
        public String emailAddress;
        public String mobileNumber;
        public int mobileVerified;
        public Object emailVerified;
        public int moneyTransferEnabled;
        public int status;
        @SerializedName("WalletBalance")
        public String walletBalance;
    }
}
