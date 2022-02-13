package com.example.nkindustries.model;

import java.io.Serializable;
import java.util.List;

public class CityListResponse implements Serializable {
    public boolean success;
    public String message;
    public List<Datum> data;

    public class Datum implements Serializable{
        public int id;
        public String city_name;
        public int state_id;
        public String state_name;
        public int status;

        @Override
        public String toString() {
            return city_name;
        }
    }
}
