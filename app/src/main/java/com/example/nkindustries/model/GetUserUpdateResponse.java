package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetUserUpdateResponse implements Serializable {

    public boolean success;
    public String message;
    public Data data;

    public class UserProfile implements Serializable{
        public String name;
        @SerializedName("AgentCode")
        public String agentCode;
        @SerializedName("Mobile")
        public String mobile;
        @SerializedName("MobileVerified")
        public int mobileVerified;
        public String emailAddress;
        @SerializedName("EmailVerified")
        public int emailVerified;
        @SerializedName("KYC_Status")
        public String kYC_Status;
    }

    public class PaymentLink implements Serializable{
        @SerializedName("Staus")
        public int staus;
        public boolean payment_pg_1;
        public boolean payment_pg_2;
        public boolean payment_pg_3;
        public boolean payment_pg_4;
        public boolean payment_pg_5;
        public boolean payment_pg_7;
    }

    public class Transfer implements Serializable{
        public boolean money_transfer_1;
        public boolean money_transfer_2;
    }

    public class KYC implements Serializable{
        public String profile_image;
        @SerializedName("DateOfbirth")
        public String dateOfbirth;
        public String State;
        public String city;
        public int id;
        @SerializedName("CompanyName")
        public String companyName;
        @SerializedName("Address1")
        public String address1;
        @SerializedName("Address2")
        public String address2;
        @SerializedName("District")
        public String district;
        public int pincode;
        @SerializedName("PANCARD")
        public String pancard;
        @SerializedName("Pancard_approved")
        public int pancard_approved;
        @SerializedName("Pancard_url")
        public String pancard_url;
        public String aadhar_card;
        public int aadhar_approved;
        public String aadhar_url;
        @SerializedName("GST_number")
        public String gST_number;
        @SerializedName("GST_url")
        public String gST_url;
        @SerializedName("GST_approved")
        public int gST_approved;
    }

    public class Data{
        @SerializedName("WalletBalance")
        public String walletBalance;
        @SerializedName("MoneyTransfer")
        public int moneyTransfer;
        @SerializedName("UserProfile")
        public UserProfile userProfile;
        public int status;
        @SerializedName("PaymentLink")
        public PaymentLink paymentLink;
        @SerializedName("Transfer")
        public Transfer transfer;
        @SerializedName("KYC")
        public KYC kYC;
    }

}
