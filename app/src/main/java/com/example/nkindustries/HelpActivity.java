package com.example.nkindustries;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nkindustries.databinding.ActivityHelpBinding;
import com.example.nkindustries.databinding.ActivityNotificationListBinding;
import com.example.nkindustries.model.HelpResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends AppCompatActivity {

    ActivityHelpBinding binding;
    MyPreferences myPreferences;
    String imageStr="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help");
        myPreferences = new MyPreferences(this);

        String name = myPreferences.getString(Constants.firstName);
        String email = myPreferences.getString(Constants.email);
        String mobile = myPreferences.getString(Constants.mobileNumber);
        binding.edtName.setText(name);
        binding.edtEmail.setText(email);
        binding.edtMobile.setText(mobile);

        binding.pofileImgUploadBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(HelpActivity.this);
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.message.getText().toString().trim().equals("")){
                    Toast.makeText(HelpActivity.this,"Please enter message",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    contactUs(myPreferences.getString(Constants.userId),binding.message.getText().toString().trim(),imageStr);
                }
            }
        });

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

    public void contactUs(String userId,String message,String image) {
        showPopupProgressSpinner(true,this);
        Call<HelpResponse> call = RetrofitClient.getInstance().getMyApi().contactUs(userId,message,image);
        call.enqueue(new Callback<HelpResponse>() {
            @Override
            public void onResponse(Call<HelpResponse> call, Response<HelpResponse> response) {
                HelpResponse temp = response.body();
                showPopupProgressSpinner(false,HelpActivity.this);
                if(temp!= null){
                    Toast.makeText(HelpActivity.this,temp.message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpResponse> call, Throwable t) {
                //contactResponse.setValue(null);
                showPopupProgressSpinner(true,HelpActivity.this);
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

    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    try {
                        if (ActivityCompat.checkSelfPermission(HelpActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            permission.launch(new String[]{Manifest.permission.CAMERA});
                        }
                        else
                            takePicture.launch(getTempUri());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(pickPhoto , 1);
                    mGetContent.launch("image/*");
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    ActivityResultLauncher<String[]> permission = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String,Boolean>>() {
                @Override
                public void onActivityResult(Map<String,Boolean> permissions) {
                    if(permissions.get(Manifest.permission.CAMERA) != null){
                        if(permissions.get(Manifest.permission.CAMERA) == true){
                            try {
                                takePicture.launch(getTempUri());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    Uri tempUri;
    public Uri getTempUri() throws IOException {
        File storageDir  = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File temp = File.createTempFile("temp_image",".png",storageDir);
        tempUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", temp);
        return tempUri;
    }

    public String getBase64FromURI(Uri uri){
        String sImage = "";
        try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
            byte[] bytes=stream.toByteArray();
            sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
            return sImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sImage;
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    System.out.println(uri);
                    binding.imgProfileImage.setImageURI(uri);
                    imageStr = getBase64FromURI(uri);
                }
            });

    ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        imageStr = getBase64FromURI(tempUri);
                        binding.imgProfileImage.setImageURI(tempUri);
                    }
                }
            });

}