package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class WalletTransferResponse implements Serializable {

    public boolean status;
    public String message;
    public Data data;

    public class ReciverDetails implements Serializable{
        @SerializedName("FirstName")
        public String firstName;
        @SerializedName("LastName")
        public String lastName;
        @SerializedName("AgentCode")
        public String agentCode;
        public String mobile;
    }

    public class WalletDetails implements Serializable{
        public int id;
        public int payment_wallet_id;
        public int debit_amount;
        public int credit_amount;
        public String transaction_number;
        public String transaction_date;
        public int user_id;
        public String status;
        public Object ds_wallet_balance_request_id;
        public String remarks;
        public int transfer_charge;
        public Object wallet_recharge_payment_id;
        public double updated_wallet_balance;
        public int verify_mobile_beneficiaries_bank_account_id;
        public Object payout_money_transfer_id;
        public Object payout_money_transfer_details;
        public Date created_at;
        public Date updated_at;
    }

    public class Data implements Serializable{
        @SerializedName("ReciverDetails")
        public ReciverDetails reciverDetails;
        @SerializedName("WalletDetails")
        public WalletDetails walletDetails;
    }


}
