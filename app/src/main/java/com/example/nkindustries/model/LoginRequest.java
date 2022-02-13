package com.example.nkindustries.model;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    public LoginRequest(String mobile,String password){
        this.mobile = mobile;
        this.password = password;
    }
    String mobile;
    String password;
}
