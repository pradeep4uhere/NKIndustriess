package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentDetailsResponse implements Serializable {

    public boolean status;
    public String message;
    public Data data;

    public class CustomerDetails implements Serializable{
        @SerializedName("FirstName")
        public String firstName;
        @SerializedName("EmailAddress")
        public String emailAddress;
        @SerializedName("PaymentDate")
        public String paymentDate;
        @SerializedName("PhoneNumber")
        public String phoneNumber;
    }

    public class PaymentDetails implements Serializable{
        @SerializedName("RefKey")
        public String refKey;
        @SerializedName("Status")
        public String status;
        @SerializedName("TransactionId")
        public String transactionId;
        @SerializedName("PaymentDate")
        public String paymentDate;
        @SerializedName("Source")
        public String source;
        @SerializedName("Amount")
        public String amount;
        public String charges;
    }

    public class PaymentCardDetails implements Serializable{
        @SerializedName("CardType")
        public String cardType;
        public String cardNum;
        public String issuingBank;
        public String cardCategory;
        @SerializedName("Source")
        public String source;
        @SerializedName("Amount")
        public String amount;
        public String failed;
    }

    public class MoreDetails implements Serializable{
        public String ip_address;
        public String userAgent;
        public String platform;
        public String latitude;
        public String longitude;
        public String zipCode;
        public String cityName;
        public String regionName;
    }

    public class Data implements Serializable{
        @SerializedName("CustomerDetails")
        public CustomerDetails customerDetails;
        @SerializedName("PaymentDetails")
        public PaymentDetails paymentDetails;
        @SerializedName("PaymentCardDetails")
        public PaymentCardDetails paymentCardDetails;
        @SerializedName("MoreDetails")
        public MoreDetails moreDetails;
    }

}
