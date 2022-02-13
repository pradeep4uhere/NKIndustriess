package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nkindustries.model.AccountListresponse;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;

public class TransferMoney extends AppCompatActivity {

    AccountListresponse.AccountList accountList;
    Button transferMoney;
    TextInputEditText edtAmount,edtRemarks;
    TextView edtName,edtBank,edtAccount,edtIfscCode,txtAccountType;
    TextView txtBalance,txtBenName;
    MyPreferences myPreferences;
    String[] spinnerItems = { "NEFT", "IMPS"};
    private Spinner spinner;
    String selectedSpinnerValue;
    ImageView bankImage;
    String accountType;// for dmt1 and dmt2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);
        myPreferences = new MyPreferences(this);
        accountList = (AccountListresponse.AccountList) getIntent().getSerializableExtra("accountList");
        accountType = getIntent().getStringExtra("accountType");

        spinner = findViewById(R.id.spinner);
        edtName = findViewById(R.id.benifeciery_name);
        edtBank = findViewById(R.id.edtBankName);
        edtAccount = findViewById(R.id.account_number);
        edtIfscCode = findViewById(R.id.edtIfsc);
        edtAmount = findViewById(R.id.transfer_amount);
        edtRemarks = findViewById(R.id.edtRemarks);
        bankImage = findViewById(R.id.bankimage);
        txtAccountType = findViewById(R.id.txtAccountType);
        txtBalance = findViewById(R.id.txtBalance);
        txtBenName = findViewById(R.id.txtBenName);
        txtBalance.setText("Rs. "+myPreferences.getString(Constants.walletBalance));
        txtBenName.setText("BENEFICIARY: " + accountList.beneficiary_name);
        initView();
        ImageView imageView = (ImageView) findViewById(R.id.back_to_contact_details_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Move to Transfer Money Confirmation Screen
        transferMoney = (Button) findViewById(R.id.transfer_money_submit_btn);
        transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edtAmount.getText().toString().trim();
                String remarks = edtAmount.getText().toString().trim();
                if(TextUtils.isEmpty(amount)){
                    showToast("Enter Amount");
                    return;
                }

                else if(Double.valueOf(amount)<1){
                    showToast("Enter Amount");
                    return;
                }
                else if(TextUtils.isEmpty(remarks)){
                    showToast("Enter Remarks");
                    return;
                }
                else{
                    Intent intent = new Intent(TransferMoney.this, FundTransferConfirmation.class);
                    intent.putExtra("accountList", (Serializable) accountList);
                    intent.putExtra("amount",amount);
                    intent.putExtra("remarks",remarks);
                    intent.putExtra("paymentMode",selectedSpinnerValue);
                    intent.putExtra("accountType",accountType);
                    startActivity(intent);
                }


            }
        });

    }


    public void initView(){

        ArrayAdapter ad
                = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpinnerValue = spinnerItems[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtName.setText(accountList.beneficiary_name);
        edtBank.setText(accountList.bankName);
        edtAccount.setText("A/C: "+accountList.account_number);
        edtIfscCode.setText("IFSC: "+accountList.ifsc_code);
        if(accountList.account_type==1){
            txtAccountType.setText("Saving");
        }
        else{
            txtAccountType.setText("Credit");
        }

        Glide.with(TransferMoney.this)
                .load(accountList.bankIcon)
                .into(bankImage);
    }

    private void showToast(String message)
    {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}