package com.example.nkindustries.ui.walletpay;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nkindustries.R;
import com.example.nkindustries.WalletPaySuccess;
import com.example.nkindustries.databinding.FragmentContactBinding;
import com.example.nkindustries.databinding.FragmentPaymentReportBinding;
import com.example.nkindustries.databinding.FragmentWalletPayBinding;
import com.example.nkindustries.model.GetAgentNameResponse;
import com.example.nkindustries.model.WalletTransferResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletPayFragment extends Fragment {


    MyPreferences myPreferences;
    private WalletPayViewModel walletpayViewModel;
    private FragmentWalletPayBinding binding;

    Button button;
    Dialog dialog;

    public  WalletPayFragment(){

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View view = inflater.inflate(R.layout.fragment_wallet_pay,container,false);
        myPreferences = new MyPreferences(getContext());
        binding = FragmentWalletPayBinding.inflate(inflater, container, false);
        binding.txtWalletBalance.setText("Rs. "+myPreferences.getString(Constants.walletBalance));
        binding.walletPaySubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(binding.edtMobile.getText().toString().trim())){
                    showToast("Enter Mobile");
                    return;
                }
                else if(TextUtils.isEmpty(binding.edtName.getText().toString().trim())){
                    showToast("Get Name");
                    return;
                }
                else if(TextUtils.isEmpty(binding.edtAmount.getText().toString().trim())){
                    showToast("Enter Amount");
                    return;
                }
                else if(TextUtils.isEmpty(binding.edtRemarks.getText().toString().trim())){
                    showToast("Enter Remark");
                    return;
                }
                else{
                    walletTransfer(binding.edtMobile.getText().toString().trim(),
                            binding.edtAmount.getText().toString().trim(),
                            binding.edtRemarks.getText().toString().trim());
                }
            }
        });

        binding.getNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(binding.edtMobile.getText().toString().trim())){
                    getAgentName(binding.edtMobile.getText().toString().trim());
                }
            }
        });

        return  binding.getRoot();

    }

    public void getAgentName(String mobileNo) {
        ;
        Call<GetAgentNameResponse> call = RetrofitClient.getInstance().getMyApi().getAgentName(mobileNo);
        call.enqueue(new Callback<GetAgentNameResponse>() {
            @Override
            public void onResponse(Call<GetAgentNameResponse> call, Response<GetAgentNameResponse> response) {
                GetAgentNameResponse temp = response.body();
                if(temp!= null){
                    if(temp.success){
                        binding.edtName.setText(temp.data);
                    }
                    else showToast(temp.message);
                }

            }

            @Override
            public void onFailure(Call<GetAgentNameResponse> call, Throwable t) {
                showToast("Error, Try again");
            }

        });
    }

    public void walletTransfer(String mobileNo,String amount,String remark) {
        showPopupProgressSpinner(true,getActivity());
        Call<WalletTransferResponse> call = RetrofitClient.getInstance().getMyApi().walletTransfer(myPreferences.getString(Constants.userId),mobileNo,amount,remark);
        call.enqueue(new Callback<WalletTransferResponse>() {
            @Override
            public void onResponse(Call<WalletTransferResponse> call, Response<WalletTransferResponse> response) {
                WalletTransferResponse temp = response.body();
                showPopupProgressSpinner(false,getActivity());
                if(temp!= null){
                    if(temp.status){
                        Intent intent = new Intent(getContext(), WalletPaySuccess.class);
                        intent.putExtra("transferDetails",temp);
                        startActivity(intent);
                    }
                    else showToast(temp.message);
                }
            }

            @Override
            public void onFailure(Call<WalletTransferResponse> call, Throwable t) {
                //contactResponse.setValue(null);
                showPopupProgressSpinner(true,getActivity());
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
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}
