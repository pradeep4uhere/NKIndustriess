package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.List;

public class StateListResponse implements Serializable {

    public boolean success;
    public String message;
    public List<Datum> data;

    public class Datum implements Serializable{
        public int id;
        public String state_name;
        public int country_id;
        public int status;

        @Override
        public String toString() {
            return state_name;
        }
    }

}
