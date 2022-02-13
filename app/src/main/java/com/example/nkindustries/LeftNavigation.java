package com.example.nkindustries;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;

import com.example.nkindustries.util.Constants;
import com.example.nkindustries.util.MyPreferences;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nkindustries.databinding.ActivityLeftNavigationBinding;

public class LeftNavigation extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLeftNavigationBinding binding;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLeftNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myPreferences = new MyPreferences(this);

       TextView txtName =  binding.navView.getHeaderView(0).findViewById(R.id.txtName);
       TextView txtMobile =  binding.navView.getHeaderView(0).findViewById(R.id.txtMobile);
       txtName.setText(myPreferences.getString(Constants.firstName) +" "+ myPreferences.getString(Constants.lastName));
       txtMobile.setText("Mobile: "+myPreferences.getString(Constants.mobileNumber));

        setSupportActionBar(binding.appBarLeftNavigation.toolbar);
//        binding.appBarLeftNavigation.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //showToast("Clicked");
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_recharge,
                R.id.nav_wallet_pay,
                R.id.nav_contacts,
                R.id.nav_wallet_report,
                R.id.nav_payment_report,
                R.id.nav_profile,
                R.id.nav_change_password,
                R.id.nav_mpin_password,
                R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_left_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_slideshow){
                    logout();
                }
                else{
                    // for other than logout
                    NavigationUI.onNavDestinationSelected(item, navController);
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.left_navigation, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_noti:
                Intent in = new Intent(this,NotificationList.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_left_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure, want to logout ?.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int id) {
                        MyPreferences myPreferences = new MyPreferences(LeftNavigation.this);
                        myPreferences.clearData();
                        Intent intent = new Intent(LeftNavigation.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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


}