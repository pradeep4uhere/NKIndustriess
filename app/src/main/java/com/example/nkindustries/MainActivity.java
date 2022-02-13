package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

public class MainActivity extends AppCompatActivity  {

    //Variables
    Animation topAim, bottomAim;
    ImageView image;
    TextView logo, slogan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        //Animation
        topAim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.TextView);
        slogan = findViewById(R.id.TextView2);

        image.setAnimation(topAim);
        logo.setAnimation(bottomAim);
        slogan.setAnimation(bottomAim);

        int SPLASH_SCREEN = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyPreferences myPreferences = new MyPreferences(MainActivity.this);
                if(myPreferences.getBoolean(Constants.loginStatus)) {
                    if(myPreferences.getBoolean(Constants.otpVerification)) {
                        Intent intent = new Intent(MainActivity.this, LeftNavigation.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, login_otp.class);
                        startActivity(intent);
                    }
                }
                else{
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_SCREEN);


    }




}