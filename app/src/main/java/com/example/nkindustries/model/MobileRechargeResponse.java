package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MobileRechargeResponse implements Serializable {

    public boolean success;
    public String message;
    public Data data;

    public class PostArr implements Serializable{
        public String customer_name;
        public String rechargeBillpayment;
        public String mobileNumber;
        public String operatorName;
        public int operator;
        public String operatorCircleName;
        public Object operatorCircle;
        public String amount;
        public String transactionOrderID;
        public ArrayList<Object> optionalArr;
    }

    public class Data implements Serializable{
        public PostArr postArr;
        public String serviceProviderIdEncyptStr;
        public String lastidEncyptStr;
        public String billerDetails;
        public ArrayList<Object> paymentTypeArr;
        public String billerName;
        public String billerCategory;
        public String params;
        public ArrayList<Object> optionalArr;
        public int provider_id;
        public String reference_id;
        public String customerDetails;
    }

}
