package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nkindustries.model.AddAccountResponse;
import com.example.nkindustries.model.AddFundAccountResponse;
import com.example.nkindustries.model.AddFundContactResponse;
import com.example.nkindustries.model.BankListResponse;
import com.example.nkindustries.model.ContactResponse;
import com.example.nkindustries.model.GetNameResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAccount extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    TextView txtContactName,getTxtContactNo,getTxtContactEmail;
    ImageView imageView;
    Button button;
    Dialog dialog;
    BankListResponse bankListResponse;
    ArrayAdapter<BankListResponse.Datum> adapter;
    String[] spinnerItems = { "Saving", "Credit Card"};
    String selectedSpinnerValue;
    String selectedBankName,bankId;
    MyPreferences myPreferences;
    EditText edtAccountNo,edtIfsc,edtBenifeciaryName;
    ContactResponse.Datum contactDetails;
    AddAccountResponse addAccountResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        contactDetails = (ContactResponse.Datum) getIntent().getSerializableExtra("contactDetails");
        myPreferences = new MyPreferences(this);
        txtContactName = findViewById(R.id.contact_name_id);
        getTxtContactNo = findViewById(R.id.contact_number_id);
        getTxtContactEmail = findViewById(R.id.contact_email_id);
        imageView = findViewById(R.id.contact_image_id);
        txtContactName.setText(contactDetails.name);
        getTxtContactNo.setText(contactDetails.mobileNumber);
        getTxtContactEmail.setText(contactDetails.emailAddress);

        getAllBank();
        ImageView imageView = (ImageView) findViewById(R.id.back_to_contact_details_btn);
        Button btnAddAccount = findViewById(R.id.btnAddAccount);
        edtAccountNo = findViewById(R.id.account_number);
        edtIfsc = findViewById(R.id.ifsc_code);
        edtBenifeciaryName = findViewById(R.id.benifeciery_name);
        spinner = findViewById(R.id.spinner);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accNO = edtAccountNo.getText().toString().trim();
                String ifsc = edtIfsc.getText().toString().trim();
                String name = edtBenifeciaryName.getText().toString().trim();
                if(bankId== null){
                    showToast("Select bank name");
                    return;
                }
                else if(TextUtils.isEmpty(accNO)){
                    showToast("Enter account number");
                    return;
                }
                else if(TextUtils.isEmpty(ifsc)){
                    showToast("Enter IFSC code");
                    return;
                }
                else if(TextUtils.isEmpty(name)){
                    showToast("Enter beneficiary name");
                    return;
                }
                else{
                    addBankAccount(accNO,ifsc,name);
                }


            }
        });

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

        //Check If Registered Name is validated
        Button btnGetBeneficiary = (Button) findViewById(R.id.get_benifeciery_name);
        btnGetBeneficiary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String accNO = edtAccountNo.getText().toString().trim();
                String ifsc = edtIfsc.getText().toString().trim();
                if(TextUtils.isEmpty(accNO)){
                    showToast("Enter account number");
                    return;
                }
                else if(TextUtils.isEmpty(ifsc)){
                    showToast("Enter IFSC code");
                    return;
                }
                else{
                    getBeneficiaryName(accNO,ifsc);
                }
            }
        });

    }


    private void initBankNameAutoList()
    {
        //UI reference of textView
        final AutoCompleteTextView customerAutoTV = findViewById(R.id.customerTextView);
        //Create adapter
        adapter = new ArrayAdapter<BankListResponse.Datum>(AddAccount.this, android.R.layout.simple_spinner_item, bankListResponse.data);

        //Set adapter
        customerAutoTV.setAdapter(adapter);
        customerAutoTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                selectedBankName = adapter.getItem(pos).name;
                bankId = String.valueOf(adapter.getItem(pos).id);
                edtIfsc.setText(adapter.getItem(pos).iFSCCode);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private void getAllBank() {

        Call<BankListResponse> call = RetrofitClient.getInstance().getMyApi().getAllBank();
        call.enqueue(new Callback<BankListResponse>() {
            @Override
            public void onResponse(Call<BankListResponse> call, Response<BankListResponse> response) {
                bankListResponse = response.body();
                if(bankListResponse!= null){
                    if(bankListResponse.success){
                        initBankNameAutoList();
                    }
                    else{
                        showToast(bankListResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<BankListResponse> call, Throwable t) {
                showToast("An error has occured");
            }

        });
    }

    private void addBankAccount(String accNo,String ifsc,String name) {
        showPopupProgressSpinner(true,this);
        String userId = myPreferences.getString(Constants.userId);
        String contactId = String.valueOf(contactDetails.id);
        //"Saving", "Credit Card"
        String accountType = "";
        if(selectedSpinnerValue.equalsIgnoreCase("Saving")){
            accountType = "1";
        }
        else{
            accountType = "2";
        }
        Call<AddAccountResponse> call = RetrofitClient.getInstance().getMyApi().addBankAccount(userId,contactId,name,accountType,accNo,ifsc,bankId);
        call.enqueue(new Callback<AddAccountResponse>() {
            @Override
            public void onResponse(Call<AddAccountResponse> call, Response<AddAccountResponse> response) {
                addAccountResponse = response.body();
                showPopupProgressSpinner(false,AddAccount.this);
                if(addAccountResponse!= null){
                    if(addAccountResponse.success){
                        showToast(addAccountResponse.message);
                        AddAccount.this.finish();
                        //addFundContact(String.valueOf(addAccountResponse.data.contactId));
                    }
                    else{
                        showToast(addAccountResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddAccountResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(true,AddAccount.this);
            }

        });
    }

    private void getBeneficiaryName(String accountId,String ifscCode) {
        showPopupProgressSpinner(true,this);
        String userId = myPreferences.getString(Constants.userId);
        Call<GetNameResponse> call = RetrofitClient.getInstance().getMyApi().getName(userId,accountId,ifscCode);
        call.enqueue(new Callback<GetNameResponse>() {
            @Override
            public void onResponse(Call<GetNameResponse> call, Response<GetNameResponse> response) {
                GetNameResponse getNameResponse = response.body();
                showPopupProgressSpinner(false,AddAccount.this);
                if(getNameResponse!= null){
                    if(getNameResponse.status){
                        showToast(getNameResponse.message);
                        edtBenifeciaryName.setText(getNameResponse.data.registredName);
                    }
                    else{
                        showToast(addAccountResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetNameResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(false,AddAccount.this);
            }

        });
    }


    private void addFundContact(String contactId) {
        showPopupProgressSpinner(true,this);
        String userId = myPreferences.getString(Constants.userId);
        Call<AddFundContactResponse> call = RetrofitClient.getInstance().getMyApi().addFundContact(userId,contactId,"1");
        call.enqueue(new Callback<AddFundContactResponse>() {
            @Override
            public void onResponse(Call<AddFundContactResponse> call, Response<AddFundContactResponse> response) {
                AddFundContactResponse addFundContactResponse = response.body();
                showPopupProgressSpinner(false,AddAccount.this);
                if(addFundContactResponse!= null){
                    if(addFundContactResponse.status){
                        addFundAccount(""+addFundContactResponse.data.payoutBankUserContactApi.payout_bank_contact_id,
                                ""+addAccountResponse.data.id,""+addFundContactResponse.data.payoutBankUserContactApi.payout_bank_contact_id);
                    }
                    else{
                        showToast(addAccountResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddFundContactResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(false,AddAccount.this);
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
                showPopupProgressSpinner(false,AddAccount.this);
                if(addAccountResponse!= null){
                    if(addAccountResponse.status){
                        showToast(addAccountResponse.message);
                        AddAccount.this.finish();
                    }
                    else{
                        showToast(addAccountResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddFundAccountResponse> call, Throwable t) {
                showToast("An error has occured");
                showPopupProgressSpinner(false,AddAccount.this);
            }

        });
    }

    private void showToast(String message)
    {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Dialog progressDialog = null;
    private ProgressBar progressBar;

    public void showPopupProgressSpinner(Boolean isShowing, Context context) {
        if (isShowing) {
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