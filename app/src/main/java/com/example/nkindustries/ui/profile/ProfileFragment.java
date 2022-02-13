package com.example.nkindustries.ui.profile;

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
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nkindustries.R;
import com.example.nkindustries.databinding.FragmentProfileBinding;
import com.example.nkindustries.model.CityListResponse;
import com.example.nkindustries.model.GetUserUpdateResponse;
import com.example.nkindustries.model.ImageUploadResponse;
import com.example.nkindustries.model.StateListResponse;
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

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    MyPreferences myPreferences;
    GetUserUpdateResponse getUserUpdateResponse;
    String selectedState,stateId;
    String selectedCity,cityId;
    int clickedImage = 0;// 1 for profile, 2 for pan, 3 for adhar, 4 for gst

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        myPreferences = new MyPreferences(getContext());

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.pofileImgUploadBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImage = 1;
                chooseImage(getContext());
            }
        });
        binding.panCardUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImage = 2;
                chooseImage(getContext());
            }
        });
        binding.aadharUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImage = 3;
                chooseImage(getContext());
            }
        });
        binding.gstUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImage = 4;
                chooseImage(getContext());
            }
        });
        binding.saveKycBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address1 = binding.addressLine1.getText().toString().trim();
                String address2 = binding.addressLine2.getText().toString().trim();
                String pincode = binding.pincode.getText().toString().trim();
                String dob = binding.dateOfBirth.getText().toString().trim();
                String adhar = binding.aadharCardInput.getText().toString().trim();
                String pancard = binding.panCardInput.getText().toString().trim();
                String gst = binding.gstNumberInput.getText().toString().trim();
                if(TextUtils.isEmpty(address1)){
                    showToast("Enter address line 1");
                    return;
                }
                else if(TextUtils.isEmpty(address2)){
                    showToast("Enter address line 2");
                    return;
                }
                else if(TextUtils.isEmpty(selectedState)){
                    showToast("Enter state");
                    return;
                }
                else if(TextUtils.isEmpty(selectedCity)){
                    showToast("Enter city");
                    return;
                }
                else if(TextUtils.isEmpty(pincode)){
                    showToast("Enter pin code");
                    return;
                }
                else if(TextUtils.isEmpty(dob)){
                    showToast("Enter date of birth");
                    return;
                }
                else if(TextUtils.isEmpty(adhar)){
                    showToast("Enter Adhar number");
                    return;
                }
                else if(TextUtils.isEmpty(pancard)){
                    showToast("Enter PAN card number");
                    return;
                }
                else if(TextUtils.isEmpty(gst)){
                    showToast("Enter GST number");
                    return;
                }
                else {
                    updateUserDetailas(address1,address2,stateId,cityId,pincode,dob,pancard,adhar,gst);
                }
            }
        });
        getUserUpdate();
        getState();


        return root;
    }

    private void getUserUpdate() {
        showPopupProgressSpinner(true,getContext());
        String userId = myPreferences.getString(Constants.userId);
        Call<GetUserUpdateResponse> call = RetrofitClient.getInstance().getMyApi().getUserUpdate(userId);
        call.enqueue(new Callback<GetUserUpdateResponse>() {
            @Override
            public void onResponse(Call<GetUserUpdateResponse> call, Response<GetUserUpdateResponse> response) {
                showPopupProgressSpinner(false,getContext());
                getUserUpdateResponse = response.body();

                if(getUserUpdateResponse!= null){
                    if(getUserUpdateResponse.success){
                        updateUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserUpdateResponse> call, Throwable t) {
                showPopupProgressSpinner(false,getContext());
            }

        });
    }

    private void getState() {
        Call<StateListResponse> call = RetrofitClient.getInstance().getMyApi().getStateList();
        call.enqueue(new Callback<StateListResponse>() {
            @Override
            public void onResponse(Call<StateListResponse> call, Response<StateListResponse> response) {
                StateListResponse stateListResponse = response.body();

                if(stateListResponse!= null){
                    if(stateListResponse.success){
                        initStateAutoList(stateListResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<StateListResponse> call, Throwable t) {
            }

        });
    }


    private void getCity(String stateId) {
        showPopupProgressSpinner(true,getContext());
        String userId = myPreferences.getString(Constants.userId);
        Call<CityListResponse> call = RetrofitClient.getInstance().getMyApi().getCityList(stateId);
        call.enqueue(new Callback<CityListResponse>() {
            @Override
            public void onResponse(Call<CityListResponse> call, Response<CityListResponse> response) {
                showPopupProgressSpinner(false,getContext());
                CityListResponse cityListResponse = response.body();

                if(cityListResponse!= null){
                    if(cityListResponse.success){
                        initCityAutoList(cityListResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<CityListResponse> call, Throwable t) {
                showPopupProgressSpinner(false,getContext());
            }

        });
    }

    private void initStateAutoList(StateListResponse stateListResponse)
    {
        //Create adapter
        ArrayAdapter adapter = new ArrayAdapter<StateListResponse.Datum>(getContext(), android.R.layout.simple_spinner_item, stateListResponse.data);

        //Set adapter
        binding.aoutoCompleteState.setAdapter(adapter);
        binding.aoutoCompleteState.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                selectedState = ((StateListResponse.Datum)adapter.getItem(pos)).state_name;
                stateId = String.valueOf(((StateListResponse.Datum)adapter.getItem(pos)).id);

                getCity(stateId);

            }
        });
    }

    private void initCityAutoList(CityListResponse cityListResponse)
    {
        //Create adapter
        ArrayAdapter adapter = new ArrayAdapter<CityListResponse.Datum>(getContext(), android.R.layout.simple_spinner_item, cityListResponse.data);
        //Set adapter
        binding.aoutoCompleteCity.setAdapter(adapter);
        binding.aoutoCompleteCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                selectedCity = ((CityListResponse.Datum)adapter.getItem(pos)).city_name;
                cityId = String.valueOf(((CityListResponse.Datum)adapter.getItem(pos)).id);

            }
        });
    }

    public void updateUI(){
        RequestOptions options = new RequestOptions();
        Glide.with(getActivity())
                .load(getUserUpdateResponse.data.kYC.profile_image)
                .apply(options.circleCropTransform())
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .into(binding.imgProfile);
        binding.txtNmae.setText(getUserUpdateResponse.data.userProfile.name);
        binding.txtMobile.setText("Mobile: "+getUserUpdateResponse.data.userProfile.mobile);
        binding.txtEmail.setText("Email: "+getUserUpdateResponse.data.userProfile.emailAddress);
        binding.txtAgentCode.setText(getUserUpdateResponse.data.userProfile.agentCode);
        binding.txtKycActive.setText(getUserUpdateResponse.data.userProfile.kYC_Status);

        if(getUserUpdateResponse.data.userProfile.kYC_Status.equalsIgnoreCase("Active")){
            binding.linLayAddress.setVisibility(View.VISIBLE);
            binding.linLayAddressEdit.setVisibility(View.GONE);
            binding.linLayKYC.setVisibility(View.VISIBLE);
            binding.linLayKYCEdit.setVisibility(View.GONE);

            binding.txtAddress1.setText(getUserUpdateResponse.data.kYC.address1);
            binding.txtAddress2.setText(getUserUpdateResponse.data.kYC.address2);
            binding.txtPinCode.setText(getUserUpdateResponse.data.kYC.city+", "+
                    getUserUpdateResponse.data.kYC.pincode);

            binding.txtDOB.setText(getUserUpdateResponse.data.kYC.dateOfbirth);
            binding.txtAdharCard.setText(getUserUpdateResponse.data.kYC.aadhar_card);
            binding.txtPanCard.setText(getUserUpdateResponse.data.kYC.pancard);
            binding.txtGSTNo.setText(getUserUpdateResponse.data.kYC.gST_number);

            binding.pofileImgUploadBtn2.setVisibility(View.GONE);
            binding.panCardUploadBtn.setVisibility(View.GONE);
            binding.gstUploadBtn.setVisibility(View.GONE);
            binding.aadharUploadBtn.setVisibility(View.GONE);

            Glide.with(getActivity())
                    .load(getUserUpdateResponse.data.kYC.profile_image)
                    .into(binding.imgProfileImage);
            Glide.with(getActivity())
                    .load(getUserUpdateResponse.data.kYC.pancard_url)
                    .into(binding.imgPanCard);
            Glide.with(getActivity())
                    .load(getUserUpdateResponse.data.kYC.aadhar_url)
                    .into(binding.imgAdhar);

            Glide.with(getActivity())
                    .load(getUserUpdateResponse.data.kYC.gST_url)
                    .into(binding.imgGST);

        }
        else{
            binding.linLayAddress.setVisibility(View.GONE);
            binding.linLayAddressEdit.setVisibility(View.VISIBLE);
            binding.linLayKYC.setVisibility(View.GONE);
            binding.linLayKYCEdit.setVisibility(View.VISIBLE);

            binding.addressLine1.setText(getUserUpdateResponse.data.kYC.address1);
            binding.addressLine2.setText(getUserUpdateResponse.data.kYC.address2);
            binding.txtPinCode.setText(getUserUpdateResponse.data.kYC.district+", "+
                    getUserUpdateResponse.data.kYC.pincode);

        }
    }

    private void updateUserDetailas(String address_1,
                                    String address_2,
                                    String state_id,
                                    String city_id,
                                    String pincode,
                                    String date_of_birth,
                                    String pan_card_number,
                                    String aadhar_card_number,
                                    String gst_number) {
        showPopupProgressSpinner(true,getContext());
        String userId = myPreferences.getString(Constants.userId);
        Call<GetUserUpdateResponse> call = RetrofitClient.getInstance().getMyApi().userDetailsUpdate(userId,address_1,address_2,state_id,city_id,pincode,date_of_birth,pan_card_number,aadhar_card_number,gst_number);
        call.enqueue(new Callback<GetUserUpdateResponse>() {
            @Override
            public void onResponse(Call<GetUserUpdateResponse> call, Response<GetUserUpdateResponse> response) {
                showPopupProgressSpinner(false,getContext());
                getUserUpdateResponse = response.body();

                if(getUserUpdateResponse!= null){
                    if(getUserUpdateResponse.success){
                        updateUI();
                    }
                    showToast(getUserUpdateResponse.message);
                }
            }

            @Override
            public void onFailure(Call<GetUserUpdateResponse> call, Throwable t) {
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


    // function to let's the user to choose image from camera or gallery
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
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    System.out.println(uri);
                    if(clickedImage==1) {
                        binding.imgProfileImage.setImageURI(uri);
                    }
                    if(clickedImage==2) {
                        binding.imgPanCard.setImageURI(uri);
                    }
                    if(clickedImage==3) {
                        binding.imgAdhar.setImageURI(uri);
                    }
                    if(clickedImage==4) {
                        binding.imgGST.setImageURI(uri);
                    }
                    uploadImage(getBase64FromURI(uri));
                }
            });

    ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        if (clickedImage == 1) {
                            binding.imgProfileImage.setImageURI(tempUri);
                        }
                        if (clickedImage == 2) {
                            binding.imgPanCard.setImageURI(tempUri);
                        }
                        if (clickedImage == 3) {
                            binding.imgAdhar.setImageURI(tempUri);
                        }
                        if (clickedImage == 4) {
                            binding.imgGST.setImageURI(tempUri);
                        }
                        uploadImage(getBase64FromURI(tempUri));
                    }
                }
            });

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
        File storageDir  = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File temp = File.createTempFile("temp_image",".png",storageDir);
        tempUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", temp);
        return tempUri;
    }

    public String getBase64FromURI(Uri uri){
        String sImage = "";
        try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
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

    private void uploadImage(String base64Image) {
        showPopupProgressSpinner(true,getContext());
        String userId = myPreferences.getString(Constants.userId);
        Call<ImageUploadResponse> call = RetrofitClient.getInstance().getMyApi().uploadImage(userId,clickedImage,base64Image);
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                showPopupProgressSpinner(false,getContext());
                ImageUploadResponse imageUploadResponse = response.body();

                if(imageUploadResponse!= null){
                    if(imageUploadResponse.success){
                       if(clickedImage==1){
                           System.out.println("Image URL:"+imageUploadResponse.data);
                       }
                    }
                    showToast(imageUploadResponse.message);
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                System.out.println("image_exception"+t.toString());
                showPopupProgressSpinner(false,getContext());
            }

        });
    }
}