package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.List;

public class WalletReportResponse implements Serializable {

    public boolean status;
    public String message;
    public List<WalletData> data;
}
