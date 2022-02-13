package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nkindustries.adapter.AccountListAdapter;
import com.example.nkindustries.model.AccountListresponse;
import com.example.nkindustries.model.AccountValidationDMT2;
import com.example.nkindustries.model.AddFundAccountResponse;
import com.example.nkindustries.model.AddFundContactResponse;
import com.example.nkindustries.model.ContactResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactDetail extends AppCompatActivity {
    public FloatingActionButton floatingActionButton;
    Dialog dialog;
    Button button;
    TextView txtContactName,getTxtContactNo,getTxtContactEmail;
    ImageView imageView;
    ExtendedFloatingActionButton addAcount;
    RecyclerView recyclerView;
    AccountListAdapter adapter;
    List<AccountListresponse.AccountList> accountLists;
    AccountListresponse accountListresponse;
    MyPreferences myPreferences;
    ContactResponse.Datum contactDetails;
    String accountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        contactDetails = (ContactResponse.Datum) getIntent().getSerializableExtra("contactDetails");
        accountType = getIntent().getStringExtra("accountType");
        txtContactName = findViewById(R.id.contact_name_id);
        getTxtContactNo = findViewById(R.id.contact_number_id);
        getTxtContactEmail = findViewById(R.id.contact_email_id);
        imageView = findViewById(R.id.contact_image_id);
        txtContactName.setText(contactDetails.name);
        getTxtContactNo.setText(contactDetails.mobileNumber);
        getTxtContactEmail.setText(contactDetails.emailAddress);
        //Get Back To Contact List Screen
        ImageView imageView = (ImageView) findViewById(R.id.btn_from_conatct_details);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myPreferences = new MyPreferences(this);

        addAcount = (ExtendedFloatingActionButton) findViewById(R.id.add_contact_detail_fab_btn);
        addAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetail.this, AddAccount.class);
                intent.putExtra("contactDetails", contactDetails);
                startActivity(intent);
            }
        });

        accountLists = new ArrayList<>();
        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountListAdapter(accountLists,this,this);
        recyclerView.setAdapter(adapter);

        // getting search view of our item.
        SearchView searchView = findViewById(R.id.searchView);

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        //call api
        if(accountType.equalsIgnoreCase("dmt1")) {
            getAccountListDMT1();
        } else{
            getAccountListDMT2();
        }

    }

    public void getAccountListDMT1() {
        showPopupProgressSpinner(true,this);
        String userId = myPreferences.getString(Constants.userId);
        String contactId = String.valueOf(contactDetails.id);
        Call<AccountListresponse> call = RetrofitClient.getInstance().getMyApi().getAccountList(contactId,userId);
        call.enqueue(new Callback<AccountListresponse>() {
            @Override
            public void onResponse(Call<AccountListresponse> call, Response<AccountListresponse> response) {
                accountListresponse = response.body();
                showPopupProgressSpinner(false,ContactDetail.this);
                if(accountListresponse!= null){
                    if(accountListresponse.status){
                        accountLists.clear();
                        accountLists.addAll(accountListresponse.data.accountList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountListresponse> call, Throwable t) {
                showPopupProgressSpinner(false,ContactDetail.this);
                showToast(t.getMessage());
            }

        });
    }

    public void getAccountListDMT2() {
        showPopupProgressSpinner(true,this);
        String userId = myPreferences.getString(Constants.userId);
        String contactId = String.valueOf(contactDetails.id);
        Call<AccountListresponse> call = RetrofitClient.getInstance().getMyApi().getAccountListDmt2(contactId,userId);
        call.enqueue(new Callback<AccountListresponse>() {
            @Override
            public void onResponse(Call<AccountListresponse> call, Response<AccountListresponse> response) {
                accountListresponse = response.body();
                showPopupProgressSpinner(false,ContactDetail.this);
                if(accountListresponse!= null){
                    if(accountListresponse.status){
                        accountLists.clear();
                        accountLists.addAll(accountListresponse.data.accountList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountListresponse> call, Throwable t) {
                showPopupProgressSpinner(false,ContactDetail.this);
                showToast(t.getMessage());
            }

        });
    }

    private void filter(String text) {
        ArrayList<AccountListresponse.AccountList> filteredlist = new ArrayList<>();
        for (AccountListresponse.AccountList item : accountLists) {
            if (item.account_number.toLowerCase().contains(text.toLowerCase()) ||
                    item.beneficiary_name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }

    private void showToast(String message)
    {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    public void addFundContactDMT1AndDMT2(String accountId){
        if(accountType.equalsIgnoreCase("dmt1")) {
            addFundContact(accountId);
        } else{
            addFundContactDMT2(accountId);
        }
    }

    public void transferMoney(AccountListresponse.AccountList accountList){
        if(accountType.equalsIgnoreCase("dmt1")) {
            accountType = "dmt1";
        }
        else {accountType = "dmt2";}
        Intent intent = new Intent(this, TransferMoney.class);
        intent.putExtra("accountList", (Serializable) accountList);
        intent.putExtra("accountType",accountType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void addFundContact(String accountId) {
        showPopupProgressSpinner(true,this);
        String contactId = String.valueOf(contactDetails.id);
        String userId = myPreferences.getString(Constants.userId);
        Call<AddFundContactResponse> call = RetrofitClient.getInstance().getMyApi().addFundContact(userId,contactId,"1");

        call.enqueue(new Callback<AddFundContactResponse>() {
            @Override
            public void onResponse(Call<AddFundContactResponse> call, Response<AddFundContactResponse> response) {
                AddFundContactResponse addFundContactResponse = response.body();
                showPopupProgressSpinner(false,ContactDetail.this);
                if(addFundContactResponse!= null){
                    if(addFundContactResponse.status){
                        addFundAccount(""+addFundContactResponse.data.payoutBankUserContactApi.payout_bank_contact_id,
                                ""+accountId,""+addFundContactResponse.data.payoutBankUserContactApi.payout_user_contact_id);
                    }
                    else{
                        showToast(addFundContactResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddFundContactResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(false,ContactDetail.this);
            }

        });
    }


    public void addFundContactDMT2(String accountId) {
        showPopupProgressSpinner(true,this);
        String contactId = String.valueOf(contactDetails.id);
        String userId = myPreferences.getString(Constants.userId);
        Call<AccountValidationDMT2> call = RetrofitClient.getInstance().getMyApi().addFundContactDMT2(userId,contactId,accountId);

        call.enqueue(new Callback<AccountValidationDMT2>() {
            @Override
            public void onResponse(Call<AccountValidationDMT2> call, Response<AccountValidationDMT2> response) {
                AccountValidationDMT2 addFundContactResponse = response.body();
                showPopupProgressSpinner(false,ContactDetail.this);
                if(addFundContactResponse!= null){
                    if(addFundContactResponse.status){
                        getAccountListDMT2();
                        showToast(addFundContactResponse.message);
                    }
                    else{
                        showToast(addFundContactResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountValidationDMT2> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(false,ContactDetail.this);
            }

        });
    }

    private void addFundAccount(String bankContactId,String accountId,String userContactId) {
        showPopupProgressSpinner(true,this);
        String userId = myPreferences.getString(Constants.userId);
        Call<AddFundAccountResponse> call = RetrofitClient.getInstance().getMyApi().addFundAccount(userId,bankContactId,accountId,userContactId);
        call.enqueue(new Callback<AddFundAccountResponse>() {
            @Override
            public void onResponse(Call<AddFundAccountResponse> call, Response<AddFundAccountResponse> response) {
                AddFundAccountResponse addAccountResponse = response.body();
                showPopupProgressSpinner(false,ContactDetail.this);
                if(addAccountResponse!= null){
                    if(addAccountResponse.status){
                        showToast(addAccountResponse.message);
                        if(accountType.equalsIgnoreCase("dmt1")) {
                            getAccountListDMT1();
                        } else{
                            getAccountListDMT2();
                        }
                    }
                    else{
                        showToast(addAccountResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddFundAccountResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(false,ContactDetail.this);
            }

        });
    }
}