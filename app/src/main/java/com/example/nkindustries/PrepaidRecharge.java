package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nkindustries.databinding.ActivityPrepaidRechargeBinding;
import com.example.nkindustries.databinding.ActivityRechargeConfirmationBinding;
import com.example.nkindustries.model.AllOperatorResponse;
import com.example.nkindustries.model.MobileRechargeResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrepaidRecharge extends AppCompatActivity {

    //private ActivityPrepaidRechargeBinding binding;
    ArrayAdapter<AllOperatorResponse.Datum> adapter;
    AllOperatorResponse allOperatorResponse;
    int selectedProviderId;
    String selectedProviderName;
    ImageView imgBack;
    EditText edtMobile,edtAmount;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepaid_recharge);
        //binding = ActivityPrepaidRechargeBinding.inflate(getLayoutInflater());
        imgBack = findViewById(R.id.back_btn);
        edtMobile = findViewById(R.id.edtMobile);
        edtAmount = findViewById(R.id.edtAmount);
        tabLayout = findViewById(R.id.tabLayout);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrepaidRecharge.this.finish();
            }
        });
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = edtMobile.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                if(TextUtils.isEmpty(mobile)){
                   showToast("Please enter mobile number");
                   return;
                }
                else if(TextUtils.isEmpty(amount)){
                    showToast("Please enter amount");
                    return;
                }
                else if(TextUtils.isEmpty(selectedProviderName)){
                    showToast("Please select operator");
                    return;
                }
                else{
                    int selectedTab = tabLayout.getSelectedTabPosition() + 1;
                    mobileRecharge(selectedTab,mobile,String.valueOf(selectedProviderId),amount);
                }

            }
        });

        getAllOperator();
    }

    private void getAllOperator() {

        Call<AllOperatorResponse> call = RetrofitClient.getInstance().getMyApi().getAllOperator();
        call.enqueue(new Callback<AllOperatorResponse>() {
            @Override
            public void onResponse(Call<AllOperatorResponse> call, Response<AllOperatorResponse> response) {
                allOperatorResponse = response.body();
                if(allOperatorResponse!= null){
                    if(allOperatorResponse.status){
                        initOperatorAutoList();
                    }
                    else{
                        showToast(allOperatorResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AllOperatorResponse> call, Throwable t) {
                showToast("An error has occured");
            }

        });
    }

    private void initOperatorAutoList()
    {
        //UI reference of textView
        final AutoCompleteTextView customerAutoTV = findViewById(R.id.autoCompleteOperator);
        //Create adapter
        adapter = new ArrayAdapter<AllOperatorResponse.Datum>(PrepaidRecharge.this, android.R.layout.simple_spinner_item, allOperatorResponse.data);

        //Set adapter
        customerAutoTV.setAdapter(adapter);
        customerAutoTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                selectedProviderId = adapter.getItem(pos).provider_id;
                selectedProviderName = adapter.getItem(pos).provider_name;
            }
        });
    }

    private void mobileRecharge(int prepaidPostpaid,String mobileNo,String operator,String amount) {
        showPopupProgressSpinner(true,PrepaidRecharge.this);
        Call<MobileRechargeResponse> call = RetrofitClient.getInstance().getMyApi().mobileRecharge(prepaidPostpaid,mobileNo,operator,amount);
        call.enqueue(new Callback<MobileRechargeResponse>() {
            @Override
            public void onResponse(Call<MobileRechargeResponse> call, Response<MobileRechargeResponse> response) {
                showPopupProgressSpinner(false,PrepaidRecharge.this);
                MobileRechargeResponse mobileRechargeResponse = response.body();
                if(mobileRechargeResponse!= null){
                    if(mobileRechargeResponse.success){
                        Intent intent = new Intent(PrepaidRecharge.this, RechargeConfirmationActivity.class);
                        intent.putExtra("mobileRechargeResponse", (Serializable) mobileRechargeResponse);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), mobileRechargeResponse.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileRechargeResponse> call, Throwable t) {
                showPopupProgressSpinner(false,PrepaidRecharge.this);
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