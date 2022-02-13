package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FundTransferResponse implements Serializable {

    public boolean status;
    public String message;
    public DataSet dataSet;

    public class Bank implements Serializable{
        public String name;
        public String ifsc_code;
        public String account_number;
    }

    public class DataSet implements Serializable{
        public String id;
        public String entity;
        public String fund_account_id;
        public int amount;
        public String currency;
        public int fees;
        public int tax;
        public String status;
        public String purpose;
        public String mode;
        public String reference_id;
        public String narration;
        public Object batch_id;
        public Object failure_reason;
        public int created_at;
        public String fee_type;
        public int razor_contact_id;
        public int razor_contact_bank_account_id;
        public String RefNo;
         public String PaymentDate;
        @SerializedName("Bank")
        public Bank bank;
    }
}
