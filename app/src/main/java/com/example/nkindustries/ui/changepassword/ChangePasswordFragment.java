package com.example.nkindustries.ui.changepassword;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nkindustries.R;
import com.example.nkindustries.databinding.FragmentChangePasswordBinding;
import com.example.nkindustries.model.ChangePasswordResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    private ChangePasswordViewModel changepasswordViewModel;
    private FragmentChangePasswordBinding binding;
    MyPreferences myPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changepasswordViewModel =
                new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        myPreferences = new MyPreferences(getContext());

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = binding.edtOldPass.getText().toString().trim();
                String newPass = binding.edtNewPass.getText().toString().trim();
                String cnfPass = binding.edtCnfPass.getText().toString().trim();
                if(TextUtils.isEmpty(oldPass)){
                    showToast("Enter old password");
                    return;
                }
                else if(TextUtils.isEmpty(newPass)){
                    showToast("Enter new password");
                    return;
                }
                else if(TextUtils.isEmpty(cnfPass)){
                    showToast("Enter confirm password");
                    return;
                }
                else{
                    changePassword(oldPass,newPass,cnfPass);
                }
            }
        });

        return root;
    }

    private void changePassword(String oldPass, String newPassword, String cnfPassword) {
        showPopupProgressSpinner(true,getContext());
        String userId = myPreferences.getString(Constants.userId);
        Call<ChangePasswordResponse> call = RetrofitClient.getInstance().getMyApi().changePassword(userId,oldPass,newPassword,cnfPassword);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                showPopupProgressSpinner(false,getContext());
                ChangePasswordResponse changePasswordResponse = response.body();

                if(changePasswordResponse!= null){
                    if(changePasswordResponse.status){

                    }
                    showToast(changePasswordResponse.message);
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                showPopupProgressSpinner(false,getContext());
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
