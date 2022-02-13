package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class AccountValidationDMT2 implements Serializable {

    public boolean status;
    public String message;
    public Data data;
    public class PayoutBankUserContactBankAccountApi implements Serializable{
        public int id;
        public int payout_bank_contact_id;
        public int payout_user_contact_id;
        public int payout_account_id;
        public int payout_bank_id;
        public int user_id;
        public String payout_bank_name;
        public String table_name;
        public int status;
        public Date created_at;
        public Date updated_at;
    }

    public class Data implements Serializable{
        @SerializedName("PayoutBankUserContactBankAccountApi")
        public PayoutBankUserContactBankAccountApi payoutBankUserContactBankAccountApi;
    }


}
