package com.example.nkindustries.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationListResponse implements Serializable {

    public boolean success;
    public String message;
    public ArrayList<Datum> data;
    public class Datum implements Serializable{
        @SerializedName("Title")
        public String title;
        @SerializedName("Desriptions")
        public String desriptions;
        @SerializedName("Time")
        public String time;
    }

}
