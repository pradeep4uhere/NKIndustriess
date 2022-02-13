package com.example.nkindustries.ui.recharge;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nkindustries.R;
import com.example.nkindustries.databinding.FragmentRechargeBinding;
import com.example.nkindustries.model.PaymentLinkResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeFragment extends Fragment {

    private RechargeViewModel rechargeViewModel;
    private FragmentRechargeBinding binding;
    MyPreferences myPreferences;
    RechargeViewAdapter adapter;
    List<PaymentLinkResponse.Datum> datumList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rechargeViewModel =
                new ViewModelProvider(this).get(RechargeViewModel.class);

        binding = FragmentRechargeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        datumList = new ArrayList<>();
        binding.recview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RechargeViewAdapter(datumList,getActivity().getApplicationContext());
        binding.recview.addItemDecoration(
                new DividerItemDecoration(
                        getContext(),
                        LinearLayoutManager.VERTICAL));
        binding.recview.setAdapter(adapter);

        myPreferences = new MyPreferences(getContext());
        getAllPaymentLink(myPreferences.getString(Constants.userId));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    public void getAllPaymentLink(String userId) {
        showPopupProgressSpinner(true,getActivity());
        Call<PaymentLinkResponse> call = RetrofitClient.getInstance().getMyApi().getPaymentLink(userId);
        call.enqueue(new Callback<PaymentLinkResponse>() {
            @Override
            public void onResponse(Call<PaymentLinkResponse> call, Response<PaymentLinkResponse> response) {
                PaymentLinkResponse temp = response.body();
                showPopupProgressSpinner(false,getActivity());
                if(temp!= null){
                    if(temp.success){
                        datumList.clear();
                        datumList.addAll(temp.data);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentLinkResponse> call, Throwable t) {
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
}
