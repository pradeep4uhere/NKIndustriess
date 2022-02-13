package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetNameResponse implements Serializable {

    public boolean status;
    public String message;
    public Data data;
    public class Data{
        @SerializedName("RegistredName")
        public String registredName;
        public String amountDeposited;
        public String refId;
    }

}
