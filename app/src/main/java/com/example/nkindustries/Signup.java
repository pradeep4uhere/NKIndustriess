package com.example.nkindustries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.nkindustries.databinding.ActivitySignupBinding;
import com.example.nkindustries.model.RegistrationResponse;
import com.example.nkindustries.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = binding.phoneNumber.getEditText().getText().toString();
                String firstName = binding.firstName.getEditText().getText().toString();
                String lastName = binding.lastName.getEditText().getText().toString();
                String email = binding.emailAddress.getEditText().getText().toString();
                String password = binding.password.getEditText().getText().toString();
                if(TextUtils.isEmpty(mobile) || mobile.length()<1){
                    Toast.makeText(Signup.this,"Enter Mobile Number",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(firstName)){
                    Toast.makeText(Signup.this,"Enter First Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(lastName)){
                    Toast.makeText(Signup.this,"Enter Last Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Signup.this,"Enter Valid Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    signupApi(mobile,firstName,lastName,email,password);
                }
            }
        });
    }

    private void signupApi(String mobile, String firstName, String lastName, String email, String password) {

        Call<RegistrationResponse> call = RetrofitClient.getInstance().getMyApi().signup(mobile,firstName,lastName,email,password,password);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if(registrationResponse!= null){
                    if(registrationResponse.success){
                        Intent intent = new Intent(Signup.this, Login.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), registrationResponse.message, Toast.LENGTH_LONG).show();
                    }
                   else {
                        Toast.makeText(getApplicationContext(), registrationResponse.message, Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

}