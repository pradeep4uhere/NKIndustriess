package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.nkindustries.adapter.NotificationListAdapter;
import com.example.nkindustries.databinding.ActivityNotificationListBinding;
import com.example.nkindustries.databinding.ActivityPaymentDetailsBinding;
import com.example.nkindustries.model.NotificationListResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationList extends AppCompatActivity {

    ActivityNotificationListBinding binding;
    MyPreferences myPreferences;
    NotificationListAdapter adapter;
    List<NotificationListResponse.Datum> datumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        binding = ActivityNotificationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        datumList = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationListAdapter(datumList,this);
        binding.recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        this,
                        LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        myPreferences = new MyPreferences(this);
        getAllNotification(myPreferences.getString(Constants.userId));
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

    public void getAllNotification(String userId) {
        showPopupProgressSpinner(true,this);
        Call<NotificationListResponse> call = RetrofitClient.getInstance().getMyApi().getAllNotification(userId);
        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                NotificationListResponse temp = response.body();
                showPopupProgressSpinner(false,NotificationList.this);
                if(temp!= null){
                    if(temp.success){
                        datumList.clear();
                        datumList.addAll(temp.data);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                //contactResponse.setValue(null);
                showPopupProgressSpinner(true,NotificationList.this);
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