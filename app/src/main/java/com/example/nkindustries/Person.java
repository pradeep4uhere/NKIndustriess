package com.example.nkindustries;

public class Person {


    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    private int Thumbnail;
    private String title;
    private String mobile;
    private String email;


    public String getTitle() {
        return title;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Person(int Thumbnail, String title, String mobile, String email) {
        this.Thumbnail = Thumbnail;
        this.title = title;
        this.mobile = mobile;
        this.email = email;
    }

}
