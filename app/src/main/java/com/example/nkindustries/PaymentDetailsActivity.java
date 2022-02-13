package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.nkindustries.databinding.ActivityPaymentDetailsBinding;
import com.example.nkindustries.model.PaymentDetailsResponse;
import com.example.nkindustries.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailsActivity extends AppCompatActivity {

    ActivityPaymentDetailsBinding binding;
    private int paymentId;
    PaymentDetailsResponse paymentDetailsResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_payment_details);
        binding = ActivityPaymentDetailsBinding.inflate(getLayoutInflater());
        paymentId = getIntent().getIntExtra("id",0);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment Details");
        getPaymentDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPaymentDetails() {
        showPopupProgressSpinner(true,this);
        Call<PaymentDetailsResponse> call = RetrofitClient.getInstance().getMyApi().paymentDetails(String.valueOf(paymentId));
        call.enqueue(new Callback<PaymentDetailsResponse>() {
            @Override
            public void onResponse(Call<PaymentDetailsResponse> call, Response<PaymentDetailsResponse> response) {
                paymentDetailsResponse = response.body();
                showPopupProgressSpinner(false,PaymentDetailsActivity.this);
                if(paymentDetailsResponse!= null){
                    if(paymentDetailsResponse.status){
                        updateUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentDetailsResponse> call, Throwable t) {
                showPopupProgressSpinner(true,PaymentDetailsActivity.this);
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

    public void updateUI(){
        if(paymentDetailsResponse.data.paymentDetails != null) {
            binding.txtDate.setText(paymentDetailsResponse.data.paymentDetails.paymentDate);
            binding.amount.setText("Rs. "+paymentDetailsResponse.data.paymentDetails.amount);
            binding.paymentRefKey.setText(paymentDetailsResponse.data.paymentDetails.refKey);
            binding.transactionId.setText(paymentDetailsResponse.data.paymentDetails.transactionId);
            binding.paymentStatus.setText(paymentDetailsResponse.data.paymentDetails.status);

            binding.txtAmount.setText("Rs. "+paymentDetailsResponse.data.paymentDetails.amount);
            binding.txtStatus.setText(paymentDetailsResponse.data.paymentDetails.status);
        }
        if(paymentDetailsResponse.data.customerDetails != null) {
            binding.txtName.setText(paymentDetailsResponse.data.customerDetails.firstName);
            binding.txtEmail.setText(paymentDetailsResponse.data.customerDetails.emailAddress);
            binding.txtMobile.setText(paymentDetailsResponse.data.customerDetails.phoneNumber);
        }

        if(paymentDetailsResponse.data.paymentCardDetails != null) {
            binding.cardDetails.setText(paymentDetailsResponse.data.paymentCardDetails.cardNum);
            binding.cardBankDetails.setText(paymentDetailsResponse.data.paymentCardDetails.issuingBank);
            binding.failedReason.setText(paymentDetailsResponse.data.paymentCardDetails.failed);
        }
    }

}