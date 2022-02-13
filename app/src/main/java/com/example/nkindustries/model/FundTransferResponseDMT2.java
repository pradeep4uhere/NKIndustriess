package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FundTransferResponseDMT2 implements Serializable {
    public String id;
    public String entity;
    public String fund_account_id;
    public int amount;
    public String currency;
    public String notes;
    public String fees;
    public String tax;
    public String status;
    public String purposes;
    public String utr;
    public String mode;
    public String reference_id;
    public String narration;
    public String batch_id;
    public String failure_reason;
    public int created_at;
    public String fee_type;
    public String razor_contact_id;
    public String razor_contact_bank_account_id;
    public String date;
    @SerializedName("Bank")
    public Bank bank;
    public class Bank implements Serializable{
        public String name;
        public String account_number;
        public String ifsc_code;
    }
}
