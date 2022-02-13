package com.example.nkindustries;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nkindustries.model.FcmTokenResponse;
import com.example.nkindustries.model.LoginResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    Button callSignUp, dashboardBtn, forgotPasswdBtn;
    private LoginResponse loginResponse;
    EditText etNumber,etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        callSignUp = findViewById(R.id.login_back_from_success);
        etNumber = findViewById(R.id.etNumber);
        etPassword = findViewById(R.id.etPassword);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        dashboardBtn = findViewById(R.id.reset_btn_submit);
        dashboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = etNumber.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(TextUtils.isEmpty(number)){
                    Toast.makeText(Login.this,"Please enter number",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this,"Please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                   // new HTTPReqTask().execute();
                    loginApi(number,password);
                }
            }
        });


        forgotPasswdBtn = findViewById(R.id.fotgot_link_btn);
        forgotPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void loginApi(String mobile, String password) {

        Call<LoginResponse> call = RetrofitClient.getInstance().getMyApi().login(mobile,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginResponse = response.body();
                if(loginResponse!= null){
                    if(loginResponse.success){
                        MyPreferences myPreferences = new MyPreferences(Login.this);
                        myPreferences.saveBoolean(Constants.loginStatus,true);
                        if(loginResponse.data.mobileVerified == 1) {
                            myPreferences.saveBoolean(Constants.otpVerification, true);
                        }
                        myPreferences.saveString(Constants.mobileNumber,loginResponse.data.mobileNumber);
                        myPreferences.saveString(Constants.email,loginResponse.data.emailAddress);
                        myPreferences.saveString(Constants.agentCode,loginResponse.data.agentCode);
                        myPreferences.saveString(Constants.firstName,loginResponse.data.firstName);
                        myPreferences.saveString(Constants.lastName,loginResponse.data.lastName);
                        myPreferences.saveString(Constants.userId,String.valueOf(loginResponse.data.id));
//                        Intent intent = new Intent(Login.this, login_otp.class);
//                        startActivity(intent);
                        saveToken(myPreferences.getString(Constants.fcmToken));
                        if(loginResponse.data.mobileVerified == 1) {

                            Intent intent = new Intent(Login.this, LeftNavigation.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Intent intent = new Intent(Login.this, login_otp.class);
                            intent.putExtra("userId",String.valueOf(loginResponse.data.id));
                            startActivity(intent);
                            finish();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void saveToken(String token) {
        MyPreferences myPreferences = new MyPreferences(this);
        String userid = myPreferences.getString(Constants.userId);


        Call<FcmTokenResponse> call = RetrofitClient.getInstance().getMyApi().saveFCMToken(userid,token);
        call.enqueue(new Callback<FcmTokenResponse>() {
            @Override
            public void onResponse(Call<FcmTokenResponse> call, Response<FcmTokenResponse> response) {
                FcmTokenResponse fcmTokenResponse = response.body();
                if(fcmTokenResponse!= null){
                    System.out.println("FCM RESPONSE "+ fcmTokenResponse.message);
                }
            }

            @Override
            public void onFailure(Call<FcmTokenResponse> call, Throwable t) {
                System.out.println("FCM FAILURE "+ t.toString());
            }

        });
    }

   // https://newbedev.com/preemptive-basic-auth-with-httpurlconnection
     class HTTPReqTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection;
            String url;
            //   String data = json;
            String result = null;
            try {
                String username ="rzp_live_lhDjkNw0M4q50u";
                String password = "my8gI1Ktb69mWdZ1MIfFn1AJ";

                String auth =new String(username + ":" + password);
                byte[] data1 = auth.getBytes("UTF-8");
                String base64 = Base64.encodeToString(data1, Base64.NO_WRAP);
                //Connect
                urlConnection = (HttpURLConnection) ((new URL("https://api.payertrust.in/public/api/v1/login").openConnection()));
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Basic "+base64);
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Connection","close");
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(10000);
                urlConnection.connect();
                JSONObject obj = new JSONObject();

                obj.put("mobile", "9015901590");
                obj.put("password", "pradeep");

                String data = obj.toString();
                //Write
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(data);
                writer.close();
                outputStream.close();
                int responseCode=urlConnection.getResponseCode();
                String urlMessage=urlConnection.getResponseMessage();
                String metthod=urlConnection.getRequestMethod();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    //Read
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    result = sb.toString();

                }else {
                    //    return new String("false : "+responseCode);
                    new String("false : "+responseCode);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

}