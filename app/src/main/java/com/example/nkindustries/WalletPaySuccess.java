package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nkindustries.databinding.ActivityLeftNavigationBinding;
import com.example.nkindustries.databinding.ActivityWalletPaySuccess2Binding;
import com.example.nkindustries.model.WalletTransferResponse;

public class WalletPaySuccess extends AppCompatActivity {

    private ActivityWalletPaySuccess2Binding binding;
    private WalletTransferResponse walletTransferResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletTransferResponse = (WalletTransferResponse) getIntent().getSerializableExtra("transferDetails");
        binding = ActivityWalletPaySuccess2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wallet Pay");
        updateUI();
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

    private void updateUI(){
        binding.txtName.setText(walletTransferResponse.data.reciverDetails.firstName + " " +
                walletTransferResponse.data.reciverDetails.firstName);
        binding.txtMobile.setText("Mobile: "+walletTransferResponse.data.reciverDetails.mobile);
        binding.txtAmount.setText("Rs. "+ walletTransferResponse.data.walletDetails.debit_amount);
        binding.txtDate.setText(walletTransferResponse.data.walletDetails.transaction_date);
        binding.txtRefNo.setText("Ref No: "+ walletTransferResponse.data.walletDetails.transaction_number);
    }

}