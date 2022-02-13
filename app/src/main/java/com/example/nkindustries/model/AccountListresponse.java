package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AccountListresponse implements Serializable {

    public boolean status;
    public String message;
    public Data data;

    public class ContactDetails implements Serializable{
        public String name;
        public String mobile_number;
        public String email_address;
    }

    public class AccountList implements Serializable{
        public int id;
        public String beneficiary_name;
        public int account_type;
        public String account_number;
        public String ifsc_code;
        public int master_bank_id;
        @SerializedName("BankName")
        public String bankName;
        @SerializedName("BankIcon")
        public String bankIcon;
        @SerializedName("IsCreditCard")
        public int isCreditCard;
        public int transfer_type;
        public int is_validated;
        public PayoutBankAccountApi payout_bank_account_api;
    }

    public class Data implements Serializable{
        public ContactDetails contactDetails;
        public List<AccountList> accountList;
    }

    public class PayoutBankAccountApi implements Serializable{
        public int id;
        public int payout_bank_contact_id;
        public int payout_user_contact_id;
        public int payout_account_id;
        public String fund_api_id;
        public String contact_id;
    }

}
