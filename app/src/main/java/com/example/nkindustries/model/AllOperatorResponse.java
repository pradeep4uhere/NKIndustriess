package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AllOperatorResponse implements Serializable {
    public boolean status;
    public String message;
    public ArrayList<Datum> data;

    public class Datum implements Serializable{
        public int id;
        public int provider_id;
        public String provider_name;
        public String description;
        public String icon;
        public Object active;

        @Override
        public String toString() {
            return provider_name;
        }
    }

}
