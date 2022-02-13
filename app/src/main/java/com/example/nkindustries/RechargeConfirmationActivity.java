package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nkindustries.model.LoginOtpResponse;
import com.example.nkindustries.model.MobileRechargeResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeConfirmationActivity extends AppCompatActivity {
    MobileRechargeResponse mobileRechargeResponse;
    TextView txtOperator,txtMobile,txtAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_confirmation);
        mobileRechargeResponse = (MobileRechargeResponse) getIntent().getSerializableExtra("mobileRechargeResponse");

        txtOperator = findViewById(R.id.txtOperator);
        txtMobile = findViewById(R.id.txtMobile);
        txtAmount = findViewById(R.id.txtRechargeAmount);

        txtOperator.setText(mobileRechargeResponse.data.postArr.operatorName);
        txtMobile.setText(mobileRechargeResponse.data.postArr.mobileNumber);
        txtAmount.setText(mobileRechargeResponse.data.postArr.amount);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rechargeConfirm();
            }
        });
    }

    private void rechargeConfirm() {
        MyPreferences myPreferences = new MyPreferences(this);
        String userid = myPreferences.getString(Constants.userId);
        showPopupProgressSpinner(true,RechargeConfirmationActivity.this);
        String lastidEncyptStr = mobileRechargeResponse.data.lastidEncyptStr;
        String mobile = mobileRechargeResponse.data.postArr.mobileNumber;
        String amount = mobileRechargeResponse.data.postArr.amount;
        Call<LoginOtpResponse> call = RetrofitClient.getInstance().getMyApi().paytmRecharge(lastidEncyptStr,mobile,amount,userid);
        call.enqueue(new Callback<LoginOtpResponse>() {
            @Override
            public void onResponse(Call<LoginOtpResponse> call, Response<LoginOtpResponse> response) {
                showPopupProgressSpinner(false,RechargeConfirmationActivity.this);
                LoginOtpResponse mobileRechargeResponse = response.body();
                if(mobileRechargeResponse!= null){
                    if(mobileRechargeResponse.success){
                        showToast(mobileRechargeResponse.message);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), mobileRechargeResponse.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginOtpResponse> call, Throwable t) {
                showPopupProgressSpinner(false,RechargeConfirmationActivity.this);
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

    private void showToast(String message)
    {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}