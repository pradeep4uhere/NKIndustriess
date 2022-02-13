package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PaymentLinkResponse implements Serializable{

    public boolean success;
    public String message;
    public ArrayList<Datum> data;

    public class Datum implements Serializable {
        public String title;
        public String descriptions;
        public String use_for;
        public String payment_link;
        public String payment_source;
        public String button_name;
    }
}
