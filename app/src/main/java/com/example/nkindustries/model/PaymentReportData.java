package com.example.nkindustries.model;

import java.io.Serializable;

public class PaymentReportData implements Serializable {
    public int id;
    public int user_id;
    public String firstname;
    public String time;
    public String updated_wallet;
    public String email;
    public String payment_date;
    public double net_amount_credit;
    public String payment_source;
    public String error_Message;
    public String phone;
    public String payment_ref_key;
    public String status;
    public String txnid;
    public double amount;
    public int payment_mode;
    public double charges;
}
