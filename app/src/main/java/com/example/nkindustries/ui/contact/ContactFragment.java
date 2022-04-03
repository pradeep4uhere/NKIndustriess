package com.example.nkindustries.ui.contact;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nkindustries.ContactDetail;
import com.example.nkindustries.ContactViewAdapter;
import com.example.nkindustries.R;

import com.example.nkindustries.databinding.FragmentContactBinding;
import com.example.nkindustries.model.AccountListresponse;
import com.example.nkindustries.model.ContactResponse;
import com.example.nkindustries.model.CreateContactResponse;
import com.example.nkindustries.retrofit.RetrofitClient;
import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFragment extends Fragment  {


    private static final String AGR_PARAM1 = "param1";
    private static final String AGR_PARAM2 = "param2";

    public  FloatingActionButton floatingActionButton;


    private String mParam1;
    private String mParam2;

    private ContactViewModel contactViewModel;
    private FragmentContactBinding binding;
    List<ContactResponse.Datum> lstPerson;
    RecyclerView rcv;

    RecyclerView recyclerView;
    SearchView searchContactList;
    ContactViewAdapter cadapter;
    NavigationView navigationView;
    Button button;
    Dialog dialog;
    MyPreferences myPreferences;

    public  ContactFragment(){

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_contact,container,false);
        lstPerson = new ArrayList<>();
        contactViewModel= new ViewModelProvider(this).get(ContactViewModel.class);
        myPreferences = new MyPreferences(getContext());
        getAllContact(myPreferences.getString(Constants.userId));
//        contactViewModel.getContactList().observe(getViewLifecycleOwner(), new Observer<ContactResponse>() {
//            @Override
//            public void onChanged(ContactResponse contactResponse) {
//               if(contactResponse != null){
//
//               }
//            }
//        });

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_contact_fab_btn);
        floatingActionButton.bringToFront();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Log.e("Onclick","Onclick");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("You want to add New Contact ?.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                //showToast("Button Clicked From Facb Icon");
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    alert.setView(R.layout.add_new_contact_popup);
                                }
                                final AlertDialog dialog = alert.create();
                                //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                //finally show the dialog box in android all
                                dialog.show();
                                Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
                                btnSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        EditText etName = dialog.findViewById(R.id.first_name);
                                        String name = etName.getText().toString().trim();
                                        EditText etMobile = dialog.findViewById(R.id.phone_number);
                                        String mobile = etMobile.getText().toString().trim();
                                        EditText etEmail = dialog.findViewById(R.id.email_address);
                                        String email = etEmail.getText().toString().trim();
                                        if(TextUtils.isEmpty(name)){
                                            Toast.makeText(getActivity(),"Enter Name",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else if(TextUtils.isEmpty(mobile) || mobile.length()<1){
                                            Toast.makeText(getActivity(),"Enter Mobile Number",Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        else if(TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                            Toast.makeText(getActivity(),"Enter Valid Email",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else{
                                            dialog.dismiss();
                                            createContact(name,mobile,email);
                                        }
                                    }
                                });

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                dialog1.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });


        recyclerView =view.findViewById(R.id.recview);
        searchContactList =view.findViewById(R.id.searchContactList);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cadapter = new ContactViewAdapter(lstPerson,getActivity().getApplicationContext(),this);
        recyclerView.setAdapter(cadapter);

        //Search Contact by Nsme or Number . . .
        searchContactList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false ;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return  view;

    }

    private void filter(String query) {
        ArrayList<ContactResponse.Datum> filteredlist = new ArrayList<>();
        for (ContactResponse.Datum item : lstPerson) {
            if (item.mobileNumber.toLowerCase().contains(query.toLowerCase()) ||
                    item.name.toLowerCase().contains(query.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            cadapter.filterList(filteredlist);
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


    public void getAllContact(String userId) {
        showPopupProgressSpinner(true,getActivity());
        Call<ContactResponse> call = RetrofitClient.getInstance().getMyApi().getAllContact(userId);
        call.enqueue(new Callback<ContactResponse>() {
            @Override
            public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {
                ContactResponse temp = response.body();
                showPopupProgressSpinner(false,getActivity());
                if(temp!= null){
                    if(temp.success){
                        lstPerson.clear();
                        lstPerson.addAll(temp.data);
                        Log.d("CheckSelectedUser", "Check Temp Data: "+temp.data);
                        cadapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ContactResponse> call, Throwable t) {
                //contactResponse.setValue(null);
                showPopupProgressSpinner(true,getActivity());
            }

        });
    }

    public void createContact(String name,String mobile,String email) {

        Call<CreateContactResponse> call = RetrofitClient.getInstance().getMyApi().createContact(myPreferences.getString(Constants.userId)
                ,name,mobile,email);
        call.enqueue(new Callback<CreateContactResponse>() {
            @Override
            public void onResponse(Call<CreateContactResponse> call, Response<CreateContactResponse> response) {
                CreateContactResponse temp = response.body();
                if(temp!= null){
                    if(temp.success){
                        getAllContact(myPreferences.getString(Constants.userId));
                    }
                    showToast(temp.message);
                }
            }

            @Override
            public void onFailure(Call<CreateContactResponse> call, Throwable t) {
                showToast("Error, Try again");
            }

        });
    }

    private Dialog progressDialog = null;
    private ProgressBar progressBar;

    public void showPopupProgressSpinner(Boolean isShowing,Context context) {
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

    public void openBottomSheet(int position, ContactResponse.Datum datum){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.dmt_bottomsheet);
        Button btnDmt1 = bottomSheetDialog.findViewById(R.id.btnDMT1);
        btnDmt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), ContactDetail.class);
                intent.putExtra("contactDetails", (Serializable) datum);
                intent.putExtra("accountType","dmt1");
                Log.d("CheckSelectedUser", "In DMT1: "+datum.id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Button btnDmt2 = bottomSheetDialog.findViewById(R.id.btnDMT2);
        btnDmt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), ContactDetail.class);
                intent.putExtra("contactDetails", (Serializable) datum);
                intent.putExtra("accountType","dmt2");
                Log.d("CheckSelectedUser", "In DMT2: "+datum.id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();
    }

}
