package com.example.nkindustries.model;

import java.io.Serializable;

public class AddBankAccountResponse implements Serializable {

    public boolean success;
    public String message;
    public Data data;
    public class Data{
        public int id;
        public int userId;
        public int contactId;
        public String beneficiaryName;
        public int accountType;
        public String accountNumber;
        public String ifscCode;
        public int bankId;
        public int status;
    }

}
