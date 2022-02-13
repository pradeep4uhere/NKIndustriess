package com.example.nkindustries.model;

import java.io.Serializable;

public class WalletData implements Serializable {
    public int id;
    public String title;
    public String description;
    public String wallet_balance;
    public int debit_amount;
    public double credit_amount;
    public String transaction_number;//ref no
    public String transaction_date;
    public String transaction_time;
    public String status;
    public String remarks;
    public int transfer_charge;
    public int wallet_recharge_payment_id;// ref no if greater than 0
    public int payment_id;
    public int user_id;
    public String firstname;
    public String email;
    public String payment_date;//wallet_recharge_payment_id if >0
    public int net_amount_credit;
    public String payment_source;
    public String message;
    public String phone;
    public String payment_ref_key;
    public String txnid;
    public String productinfo;
    public String card_type;
    public String cardnum;
    public int payment_mode;
    public double charges;
    public String ip_address;
    public String userAgent;
}
