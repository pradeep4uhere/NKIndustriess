package com.example.nkindustries.model;

public class CreateContactResponse{
    public boolean success;
    public String message;
    public Data data;

    public class Data{
        public int id;
        public int userId;
        public String contactName;
        public long mobileNumber;
        public String emailAddress;
        public int status;
    }
}