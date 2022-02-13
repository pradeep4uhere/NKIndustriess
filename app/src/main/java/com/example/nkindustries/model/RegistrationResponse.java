package com.example.nkindustries.model;

import java.util.Date;

public class RegistrationResponse {
    public boolean success;
    public Data data;
    public String message;



}
class UserDetails{
    public String mobile;
    public String first_name;
    public String last_name;
    public String email;
    public Date updated_at;
    public Date created_at;
    public int id;
}
class Data{
    public String token;
    public UserDetails userDetails;
}