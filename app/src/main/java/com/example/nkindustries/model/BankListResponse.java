package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BankListResponse implements Serializable {
    public boolean success;
    public String message;
    public List<Datum> data;

    public class Datum{
        public int id;
        @SerializedName("Name")
        public String name;
        @SerializedName("Status")
        public int status;
        public int isCreditCard;
        @SerializedName("IFSCCode")
        public String iFSCCode;
        public int transferType;
        @SerializedName("Icon")
        public String icon;

        @Override
        public String toString() {
            return name;
        }
    }
}
