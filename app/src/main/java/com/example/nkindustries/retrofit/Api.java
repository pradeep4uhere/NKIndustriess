package com.example.nkindustries.retrofit;

import com.example.nkindustries.model.AccountListresponse;
import com.example.nkindustries.model.AccountValidationDMT2;
import com.example.nkindustries.model.AddAccountResponse;
import com.example.nkindustries.model.AddFundAccountResponse;
import com.example.nkindustries.model.AddFundContactResponse;
import com.example.nkindustries.model.AllOperatorResponse;
import com.example.nkindustries.model.BankListResponse;
import com.example.nkindustries.model.ChangePasswordResponse;
import com.example.nkindustries.model.CheckPayoutResponse;
import com.example.nkindustries.model.CityListResponse;
import com.example.nkindustries.model.ContactResponse;
import com.example.nkindustries.model.CreateContactResponse;
import com.example.nkindustries.model.FcmTokenResponse;
import com.example.nkindustries.model.FundTransferResponse;
import com.example.nkindustries.model.GetAgentNameResponse;
import com.example.nkindustries.model.GetNameResponse;
import com.example.nkindustries.model.GetUserUpdateResponse;
import com.example.nkindustries.model.HelpResponse;
import com.example.nkindustries.model.ImageUploadResponse;
import com.example.nkindustries.model.LoginOtpResponse;
import com.example.nkindustries.model.LoginResponse;
import com.example.nkindustries.model.MobileRechargeResponse;
import com.example.nkindustries.model.NotificationListResponse;
import com.example.nkindustries.model.PaymentDetailsResponse;
import com.example.nkindustries.model.PaymentLinkResponse;
import com.example.nkindustries.model.PaymentReportResponse;
import com.example.nkindustries.model.RegistrationResponse;
import com.example.nkindustries.model.StateListResponse;
import com.example.nkindustries.model.WalletReportResponse;
import com.example.nkindustries.model.WalletTransferResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "https://api.payertrust.in/public/index.php/";

    //    @Headers({"Content-Type: application/json",
//            "Authorization: Basic cnpwX2xpdmVfbGhEamtOdzBNNHE1MHU6bXk4Z0kxS3RiNjltV2RaMU1JZkZuMUFK"})
    @POST("api/v1/login")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("mobile") String mobile, @Field("password") String password);

    @POST("api/v1/signup")
    @FormUrlEncoded
    Call<RegistrationResponse> signup(@Field("mobile") String mobile, @Field("first_name") String firstName,
                                      @Field("last_name") String lastName, @Field("email") String email,
                                      @Field("password") String password, @Field("confirm_password") String confirmPassword);

    @POST("api/v1/contactlist")
    @FormUrlEncoded
    Call<ContactResponse> getAllContact(@Field("user_id") String userId);

    @POST("api/v1/createcontact")
    @FormUrlEncoded
    Call<CreateContactResponse> createContact(@Field("user_id") String userId,
                                              @Field("name") String name,
                                              @Field("mobile_number") String mobile, @Field("email_address") String email);

    @POST("api/v1/allbanklist")
    Call<BankListResponse> getAllBank();

    @POST("api/v1/addaccount")
    @FormUrlEncoded
    Call<AddAccountResponse> addBankAccount(@Field("user_id") String userId, @Field("contact_id") String contactId,
                                            @Field("beneficiary_name") String beneficiaryName, @Field("account_type") String accountType,
                                            @Field("account_number") String accountNumber, @Field("ifsc_code") String ifscCode,
                                            @Field("master_bank_id") String masterBankId);

    @POST("api/v1/accountlist")
    @FormUrlEncoded
    Call<AccountListresponse> getAccountList(@Field("contact_id") String contactId, @Field("user_id") String userId);

    @POST("api/v1/accountlistwithcf")
    @FormUrlEncoded
    Call<AccountListresponse> getAccountListDmt2(@Field("contact_id") String contactId, @Field("user_id") String userId);

    @POST("api/v1/addfundcontact")
    @FormUrlEncoded
    Call<AddFundContactResponse> addFundContact(@Field("user_id") String userId, @Field("payout_contact_id") String contactId,
                                                @Field("payout_bank_id") String bankid);

    @POST("api/v1/accountvalidationwithcf")
    @FormUrlEncoded
    Call<AccountValidationDMT2> addFundContactDMT2(@Field("user_id") String userId, @Field("payout_contact_id") String contactId,
                                                   @Field("payout_bank_id") String bankid);

    @POST("api/v1/addfundaccount")
    @FormUrlEncoded
    Call<AddFundAccountResponse> addFundAccount(@Field("user_id") String userId, @Field("payout_bank_contact_id") String bankContactId,
                                                @Field("payout_account_id") String accountId, @Field("payout_user_contact_id") String userContactId);

    @POST("api/v1/getname")
    @FormUrlEncoded
    Call<GetNameResponse> getName(@Field("user_id") String userId, @Field("account_number") String accountNo,
                                  @Field("ifsc_code") String ifscCode);

    @POST("api/v1/fundtransfer")
    @FormUrlEncoded
    Call<FundTransferResponse> transferMoney(@Field("user_id") String userId,
                                             @Field("payout_bank_user_contact_api_id") String payoutBankUserContactApiId,
                                             @Field("payout_bank_id") String payoutBankId,
                                             @Field("fund_api_id") String fundApiId,
                                             @Field("contact_id") String contactId,
                                             @Field("account_number") String accountNo,
                                             @Field("ifsc_code") String ifscCode,
                                             @Field("amount") String amount,
                                             @Field("remarks") String remarks,
                                             @Field("payment_mode") String paymentMode);

    //DMT- 2 Type . . .
    @POST("api/v1/fundtransferwithcf")
    @FormUrlEncoded
    Call<ResponseBody> transferMoneyDmt2(
            @Field("user_id") String userId,
            @Field("payout_bank_user_contact_api_id") String payoutBankUserContactApiId,
            @Field("payout_bank_id") String payoutBankId,
            @Field("fund_api_id") String fundApiId,
            @Field("contact_id") String contactId,
            @Field("account_number") String accountNo,
            @Field("ifsc_code") String ifscCode,
            @Field("amount") String amount,
            @Field("remarks") String remarks,
            @Field("payment_mode") String paymentMode);

    @POST("api/v1/walletreport")
    @FormUrlEncoded
    Call<WalletReportResponse> walletReport(@Field("user_id") String userId,
                                            @Field("offset") String offset);

    @POST("api/v1/paymentreport")
    @FormUrlEncoded
    Call<PaymentReportResponse> paymentReport(@Field("user_id") String userId,
                                              @Field("offset") String offset,
                                              @Field("from_date") String fromDate,
                                              @Field("to_date") String toDate);

    @POST("api/v1/checkpayout")
    @FormUrlEncoded
    Call<CheckPayoutResponse> checkpayout(@Field("payout_id") String userId);

    @POST("api/v1/getusersupdate")
    @FormUrlEncoded
    Call<GetUserUpdateResponse> getUserUpdate(@Field("user_id") String userId);

    @POST("api/v1/getagentdetail")
    @FormUrlEncoded
    Call<GetAgentNameResponse> getAgentName(@Field("mobile_number") String mobileNo);

    @POST("api/v1/wallettransfer")
    @FormUrlEncoded
    Call<WalletTransferResponse> walletTransfer(@Field("user_id") String userId,
                                                @Field("mobile_number") String mobileNo,
                                                @Field("amount") String amount,
                                                @Field("remarks") String remark);

    @POST("api/v1/allstatelist")
    Call<StateListResponse> getStateList();

    @POST("api/v1/allcitylist")
    @FormUrlEncoded
    Call<CityListResponse> getCityList(@Field("state_id") String state_id);

    @POST("api/v1/userdetailsupdate")
    @FormUrlEncoded
    Call<GetUserUpdateResponse> userDetailsUpdate(@Field("user_id") String userId,
                                                  @Field("address_1") String address_1,
                                                  @Field("address_2") String address_2,
                                                  @Field("state_id") String state_id,
                                                  @Field("city_id") String city_id,
                                                  @Field("pincode") String pincode,
                                                  @Field("date_of_birth") String date_of_birth,
                                                  @Field("pan_card_number") String pan_card_number,
                                                  @Field("aadhar_card_number") String aadhar_card_number,
                                                  @Field("gst_number") String gst_number);

    @POST("api/v1/uploadfile")
    @FormUrlEncoded
    Call<ImageUploadResponse> uploadImage(@Field("user_id") String userId, @Field("type") int type, @Field("image_name") String image_name);

    @POST("api/v1/changepassword")
    @FormUrlEncoded
    Call<ChangePasswordResponse> changePassword(@Field("user_id") String userId, @Field("current_password") String current_password,
                                                @Field("password") String password, @Field("confirm_password") String confirm_password);


    @POST("api/v1/paymentdetails")
    @FormUrlEncoded
    Call<PaymentDetailsResponse> paymentDetails(@Field("payment_id") String payment_id);

    @POST("api/v1/allpaymentlinks")
    @FormUrlEncoded
    Call<PaymentLinkResponse> getPaymentLink(@Field("user_id") String userId);

    @POST("api/v1/allnotification")
    @FormUrlEncoded
    Call<NotificationListResponse> getAllNotification(@Field("user_id") String userId);

    @POST("api/v1/loginotp")
    @FormUrlEncoded
    Call<LoginOtpResponse> loginOtp(@Field("user_id") String userId);

    @POST("api/v1/otpverification")
    @FormUrlEncoded
    Call<LoginOtpResponse> verifyOtp(@Field("user_id") String userId, @Field("otp") String otp);

    @POST("api/v1/contactus")
    @FormUrlEncoded
    Call<HelpResponse> contactUs(@Field("user_id") String userId, @Field("message") String message, @Field("image_name") String image_name);

    @POST("api/v1/getservicelist")
    Call<AllOperatorResponse> getAllOperator();

    @POST("api/v1/mobilerecharge")
    @FormUrlEncoded
    Call<MobileRechargeResponse> mobileRecharge(@Field("rechargeBillpayment") int rechargeBillpayment,
                                                @Field("mobileNumber") String mobileNumber,
                                                @Field("operator") String operator,
                                                @Field("amount") String amount);

    @POST("api/v1/ptmrfwrecharge")
    @FormUrlEncoded
    Call<LoginOtpResponse> paytmRecharge(@Field("lastidEncyptStr") String lastidEncyptStr,
                                         @Field("mobile") String mobile,
                                         @Field("request_amount") String amount,
                                         @Field("user_id") String userid);

    @POST("api/v1/savetoken")
    @FormUrlEncoded
    Call<FcmTokenResponse> saveFCMToken(@Field("user_id") String userId, @Field("token") String token);

//
//    @POST("/api/users")
//    Call<User> createUser(@Body User user);
//
//    @GET("/api/users?")
//    Call<UserList> doGetUserList(@Query("page") String page);
//
//    @FormUrlEncoded
//    @POST("/api/users?")
//    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);

    //Request Money API . . . - api/v1/paymentrequest
    @POST("api/v1/paymentrequest")
    @FormUrlEncoded
    Call<ResponseBody> requestMoney(
            @Field("user_id") String userId,
            @Field("amount") String amount,
            @Field("reason") String reason,
            @Field("txn_id") String txn_id,
            @Field("image") String image
    );
}
