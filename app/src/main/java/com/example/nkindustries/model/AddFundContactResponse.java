package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.Date;

public class AddFundContactResponse implements Serializable {

    public boolean status;
    public String message;
    public Data data;
    public class PayoutBankUserContactApi implements Serializable{
        public int id;
        public int payout_user_contact_id;
        public int user_id;
        public int payout_bank_id;
        public String payout_bank_name;
        public String bank_contact_api;
        public int payout_bank_contact_id;
        public String table_name;
        public Object payout_account_api;
        public int status;
        public Date created_at;
        public Date updated_at;
    }

    public class Data implements Serializable{
        public PayoutBankUserContactApi payoutBankUserContactApi;
    }

}
