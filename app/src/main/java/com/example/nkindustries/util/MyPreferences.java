package com.example.nkindustries.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private Context context;
    String PREFS_FILENAME = "com.payertrust.prefs";
    SharedPreferences sharedPreferences;
    public MyPreferences(Context context){
        if(sharedPreferences==null) {
            sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);
        }
    }

    public void saveBoolean(String key,boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void saveString(String key,String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }

    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

}
