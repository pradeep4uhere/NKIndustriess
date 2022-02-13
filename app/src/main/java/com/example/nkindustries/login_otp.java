package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nkindustries.model.LoginOtpResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_otp extends AppCompatActivity {
    Button callSignUp, dashboardBtn;
    String userId;
    EditText edtOtp;
    TextView resend_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        userId = getIntent().getStringExtra("userId");
        if(userId== null){
            MyPreferences myPreferences = new MyPreferences(login_otp.this);
            userId = myPreferences.getString(Constants.userId);
        }
        loginOtp();
        dashboardBtn = findViewById(R.id.procced_btn);
        edtOtp = findViewById(R.id.otp_input);
        resend_text = findViewById(R.id.resend_text);

        resend_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginOtp();
            }
        });

        dashboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtOtp.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter OTP!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edtOtp.getText().toString().trim().length()<6){
                    Toast.makeText(getApplicationContext(), "Please enter min 6 digit OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    verifyOtp(edtOtp.getText().toString().trim());
                }
            }
        });
    }

    private void loginOtp() {
        showPopupProgressSpinner(true,login_otp.this);
        Call<LoginOtpResponse> call = RetrofitClient.getInstance().getMyApi().loginOtp(userId);
        call.enqueue(new Callback<LoginOtpResponse>() {
            @Override
            public void onResponse(Call<LoginOtpResponse> call, Response<LoginOtpResponse> response) {
                showPopupProgressSpinner(false,login_otp.this);
                LoginOtpResponse otpResponse = response.body();
                if(otpResponse!= null){
                    Toast.makeText(getApplicationContext(), otpResponse.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginOtpResponse> call, Throwable t) {
                showPopupProgressSpinner(false,login_otp.this);
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void verifyOtp(String otp) {
        showPopupProgressSpinner(true,login_otp.this);
        Call<LoginOtpResponse> call = RetrofitClient.getInstance().getMyApi().verifyOtp(userId,otp);
        call.enqueue(new Callback<LoginOtpResponse>() {
            @Override
            public void onResponse(Call<LoginOtpResponse> call, Response<LoginOtpResponse> response) {
                showPopupProgressSpinner(false,login_otp.this);
                LoginOtpResponse loginResponse = response.body();
                if(loginResponse!= null){
                    if(loginResponse.success){
                        MyPreferences myPreferences = new MyPreferences(login_otp.this);
                        myPreferences.saveBoolean(Constants.otpVerification, true);
                        Toast.makeText(getApplicationContext(), "OTP verified!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login_otp.this, LeftNavigation.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginOtpResponse> call, Throwable t) {
                showPopupProgressSpinner(false,login_otp.this);
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    private Dialog progressDialog = null;
    private ProgressBar progressBar;

    public void showPopupProgressSpinner(Boolean isShowing, Context context) {
        if (isShowing == true) {
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.popup_progressbar);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            progressBar = (ProgressBar) progressDialog
                    .findViewById(R.id.progressBar);
            progressDialog.show();
        } else if (isShowing == false) {
            progressDialog.dismiss();
        }
    }

}