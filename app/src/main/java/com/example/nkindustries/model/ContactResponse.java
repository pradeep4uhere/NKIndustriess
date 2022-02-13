package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ContactResponse implements Serializable {
    public boolean success;
    public String message;
    public List<Datum> data;

    public class Datum implements Serializable{
        public int id;
        @SerializedName("Name")
        public String name;
        @SerializedName("MobileNumber")
        public String mobileNumber;
        @SerializedName("EmailAddress")
        public String emailAddress;
        @SerializedName("Status")
        public int status;
    }

}


