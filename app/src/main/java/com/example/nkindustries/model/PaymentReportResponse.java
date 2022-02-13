package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.List;

public class PaymentReportResponse implements Serializable {

    public boolean status;
    public String message;
    public List<PaymentReportData> data;


}
