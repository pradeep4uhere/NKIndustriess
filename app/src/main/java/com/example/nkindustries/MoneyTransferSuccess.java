package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nkindustries.model.CheckPayoutResponse;
import com.example.nkindustries.model.FundTransferResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoneyTransferSuccess extends AppCompatActivity {

    TextView txtBankName,txtAccount,txtIfsc,txtRefNo,txtName,txtPaymentMode,txtPaymentStatus,
    txtAmount,txtDate;
    FundTransferResponse fundTransferResponse;
    String bankName;
    ExtendedFloatingActionButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_success);
        fundTransferResponse = (FundTransferResponse) getIntent().getSerializableExtra("transferResponse");
        bankName = getIntent().getStringExtra("bankName");
        txtBankName = findViewById(R.id.txtBankName);
        txtAccount = findViewById(R.id.txtAccountNo);
        txtIfsc = findViewById(R.id.txtIFSC);
        txtRefNo = findViewById(R.id.txtRefNo);
        txtName = findViewById(R.id.txtName);
        txtPaymentMode = findViewById(R.id.txtPaymentMode);
        txtPaymentStatus = findViewById(R.id.txtPaymentStatus);
        txtAmount = findViewById(R.id.txtAmount);
        txtDate = findViewById(R.id.txtDate);
        btnGoBack = findViewById(R.id.btnGoBack);
        //Get Back To Contact List Screen
        ImageView imageView = (ImageView) findViewById(R.id.back_to_fund_transfer_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });


        initView();

    }


    public void initView(){
        txtBankName.setText(bankName);
        txtName.setText(fundTransferResponse.dataSet.bank.name);
        txtAccount.setText("Acc No "+fundTransferResponse.dataSet.bank.account_number);
        txtIfsc.setText("IFSC Code "+fundTransferResponse.dataSet.bank.ifsc_code);
        txtPaymentMode.setText(fundTransferResponse.dataSet.mode);
        txtAmount.setText("Rs. "+fundTransferResponse.dataSet.amount);
        Log.d("GetStatusType", "By DMT1: "+fundTransferResponse.dataSet.status);
        if (fundTransferResponse.dataSet.status.equalsIgnoreCase("SUCCESS")){
            txtPaymentStatus.setTextColor(Color.GREEN);
        }
        txtPaymentStatus.setText(fundTransferResponse.dataSet.status);
        txtRefNo.setText("Ref No "+fundTransferResponse.dataSet.RefNo);
        txtDate.setText(fundTransferResponse.dataSet.PaymentDate);
    }

    private void refresh() {
        showPopupProgressSpinner(true,this);

        Call<CheckPayoutResponse> call = RetrofitClient.getInstance().getMyApi().checkpayout(fundTransferResponse.dataSet.id);
        call.enqueue(new Callback<CheckPayoutResponse>() {
            @Override
            public void onResponse(Call<CheckPayoutResponse> call, Response<CheckPayoutResponse> response) {
                CheckPayoutResponse checkPayoutResponse = response.body();
                showPopupProgressSpinner(false,MoneyTransferSuccess.this);
                if(checkPayoutResponse!= null){
                    if(checkPayoutResponse.status){
                        Log.d("GetStatusType", "By DMT1 Refresh: "+fundTransferResponse.dataSet.status);
                        if (checkPayoutResponse.message.equalsIgnoreCase("SUCCESS")){
                            txtPaymentStatus.setTextColor(Color.GREEN);
                        }
                        txtPaymentStatus.setText(checkPayoutResponse.message);
                    }
                    else{
                        showToast(checkPayoutResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckPayoutResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(true,MoneyTransferSuccess.this);
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