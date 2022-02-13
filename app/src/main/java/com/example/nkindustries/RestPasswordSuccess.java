package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RestPasswordSuccess extends AppCompatActivity {
    Button callLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password_success);


        callLogin = findViewById(R.id.login_back_from_success);

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestPasswordSuccess.this, Login.class);
                startActivity(intent);
            }
        });
    }
}