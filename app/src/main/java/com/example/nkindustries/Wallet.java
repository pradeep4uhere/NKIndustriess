package com.example.nkindustries;

public class Wallet {


    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    private int Thumbnail;
    private String txndate;
    private String title;
    private String txnno;
    private String status;
    private int amount;
    private int balamount;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBalamount() {
        return balamount;
    }

    public void setBalamount(int balamount) {
        this.balamount = balamount;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getTxnno() {
        return txnno;
    }

    public void setTxnno(String txnno) {
        this.txnno = txnno;
    }

    public Wallet(String txndate, String title,   String txnno , String status, int amount, int balamount,int Thumbnail) {
            this.Thumbnail      = Thumbnail;
            this.title          = title;
            this.txnno          = txnno;
            this.status         = status;
            this.amount         = amount;
            this.balamount      = balamount;
            this.txndate        = txndate;
    }

}
