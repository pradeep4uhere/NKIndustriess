package com.example.nkindustries.model;

import java.io.Serializable;

public class AddFundAccountResponse implements Serializable {

    public boolean status;
    public String message;
    public DataSet dataSet;

    public class DataSet implements Serializable{
        public String user_id;
        public String payout_account_id;
        public String payout_user_contact_id;
        public String payout_bank_contact_id;
        public int payout_bank_user_contact_bank_account_api_id;
        public int payout_contact_bank_account_id;
        public String fund_api_id;
        public String fund_api_contact_id;
        public String account_type;
        public String bank_name;
        public String name;
        public String account_number;
        public String ifsc;
        public Object card_number;
        public Object card_network_type;
        public Object credit_card_type;
        public Object card_sub_type;
        public int active;
        public Object account_validation_request;
        public Object register_name_with_bank;
        public String validation_status;
        public Object account_status;
    }

}
