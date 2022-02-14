package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nkindustries.model.AccountListresponse;
import com.example.nkindustries.model.FundTransferResponse;
import com.example.nkindustries.model.FundTransferResponseDMT2;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundTransferConfirmation extends AppCompatActivity {

    TextView txtBankName, txtAccount, txtIfsc, txtAccountType, txtName;
    TextView txtAmount, txtCharge, txtTotAmount;
    ImageView bankImage;
    MyPreferences myPreferences;
    AccountListresponse.AccountList accountList;
    String amount, remarks, paymentMode;
    int charge = 15;
    String accountType;// for dmt1 and dmt2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer_confirmation);
        myPreferences = new MyPreferences(this);
        accountList = (AccountListresponse.AccountList) getIntent().getSerializableExtra("accountList");
        accountType = getIntent().getStringExtra("accountType");
        amount = getIntent().getStringExtra("amount");
        remarks = getIntent().getStringExtra("remarks");
        paymentMode = getIntent().getStringExtra("paymentMode");
        txtBankName = findViewById(R.id.txtBankName);
        txtAccount = findViewById(R.id.txtAccountNo);
        txtIfsc = findViewById(R.id.txtIFSC);
        txtAccountType = findViewById(R.id.txtAccountType);
        txtName = findViewById(R.id.txtName);
        txtAmount = findViewById(R.id.txtAmount);
        txtCharge = findViewById(R.id.txtCharge);
        txtTotAmount = findViewById(R.id.txtTotAmount);
        bankImage = findViewById(R.id.bankimage);
        //initializeView
        initView();

        Button button = (Button) findViewById(R.id.money_transfer_submit_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountType.equalsIgnoreCase("dmt1")) {
                    fundTransfer();
                } else {
                    fundTransferByDmtTwo();
                }
            }
        });


        ImageView imageView = (ImageView) findViewById(R.id.back_to_fund_transfer_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fundTransferByDmtTwo() {
        showPopupProgressSpinner(true, this);
        String userId = myPreferences.getString(Constants.userId);
        String payoutBankUserContactApiId = String.valueOf(accountList.payout_bank_account_api.id);
        String fundApiId = String.valueOf(accountList.payout_bank_account_api.fund_api_id);
        String contactId = String.valueOf(accountList.payout_bank_account_api.contact_id);
        String accNo = String.valueOf(accountList.account_number);
        String ifsc = String.valueOf(accountList.ifsc_code);
        Call<ResponseBody> call = RetrofitClient.getInstance().getMyApi().transferMoneyDmt2(userId, payoutBankUserContactApiId, "2"
                , fundApiId, contactId, accNo, ifsc,amount, remarks, paymentMode);
        Log.d("GetAmtWhich", "fundTransferByDmtTwo: "+amount);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showPopupProgressSpinner(false, FundTransferConfirmation.this);
                    try {
                        String s = null;
                        assert response.body() != null;
                        s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        Log.d("TransferSuccess", "DMT Two: " + jsonObject);
                        if (jsonObject.getBoolean("status")) {
                            showToast(jsonObject.getString("message"));
                            JSONObject jsonDataSet = jsonObject.getJSONObject("dataSet");
                            FundTransferResponseDMT2 responseData = new Gson().fromJson(jsonDataSet.toString()
                                    , FundTransferResponseDMT2.class);
                            MoneyTransferSuccessForDMTTwo.getFundTransferResponse = responseData;
                            Intent intent = new Intent(FundTransferConfirmation.this, MoneyTransferSuccessForDMTTwo.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast(jsonObject.getString("message"));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                Log.d("TransferByDMT2", "onFailure: " + t.getMessage());
                showPopupProgressSpinner(false, FundTransferConfirmation.this);
            }
        });
    }


    public void initView() {
        txtName.setText(accountList.beneficiary_name);
        txtBankName.setText(accountList.bankName);
        txtAccount.setText(accountList.account_number);
        txtIfsc.setText(accountList.ifsc_code);
        if (accountList.account_type == 1) {
            txtAccountType.setText("Saving");
        } else {
            txtAccountType.setText("Credit");
        }
        txtAmount.setText("Rs. " + amount);
        txtCharge.setText("Rs. " + charge);
        txtTotAmount.setText("Rs. " + (Double.valueOf(amount) + charge));
        Glide.with(FundTransferConfirmation.this)
                .load(accountList.bankIcon)
                .into(bankImage);
    }

    private void fundTransfer() {
        showPopupProgressSpinner(true, this);
        String userId = myPreferences.getString(Constants.userId);
        String payoutBankUserContactApiId = String.valueOf(accountList.payout_bank_account_api.id);
        String fundApiId = String.valueOf(accountList.payout_bank_account_api.fund_api_id);
        String contactId = String.valueOf(accountList.payout_bank_account_api.contact_id);
        String accNo = String.valueOf(accountList.account_number);
        String ifsc = String.valueOf(accountList.ifsc_code);
        Call<FundTransferResponse> call = RetrofitClient.getInstance().getMyApi().transferMoney(userId, payoutBankUserContactApiId, "1", fundApiId, contactId, accNo, ifsc, String.valueOf(amount), remarks, paymentMode);
        call.enqueue(new Callback<FundTransferResponse>() {
            @Override
            public void onResponse(Call<FundTransferResponse> call, Response<FundTransferResponse> response) {
                FundTransferResponse fundTransferResponse = response.body();
                showPopupProgressSpinner(false, FundTransferConfirmation.this);
                Log.d("TransferSuccess", "DMT One: "+fundTransferResponse);
                if (fundTransferResponse != null) {
                    if (fundTransferResponse.status) {
                        showToast(fundTransferResponse.message);
                        Intent intent = new Intent(FundTransferConfirmation.this, MoneyTransferSuccess.class);
                        intent.putExtra("transferResponse", fundTransferResponse);
                        intent.putExtra("bankName", accountList.bankName);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast(fundTransferResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<FundTransferResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(false, FundTransferConfirmation.this);
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

    private void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}