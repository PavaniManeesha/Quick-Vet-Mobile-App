package com.example.quickvet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView profileIcon;

    HomeFragment homeFragment = new HomeFragment();
    AppointmentFragment appointmentFragment = new AppointmentFragment();
    ReportFragment reportFragment = new ReportFragment();
    ClinicFragment chatFragment = new ClinicFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Tool Bar Code
        profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewProfile();
            }
        });

        //Navigation Bar Code
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.appointment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, appointmentFragment).commit();
                        return true;
                    case R.id.report:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reportFragment).commit();
                        return true;
                    case R.id.clinic:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    //Tool Bar Method
    public void viewProfile(){
        String TYPE = getIntent().getStringExtra("TYPE").trim();
        String ID = getIntent().getStringExtra("ID").trim();

        if(TYPE.equals("Doctor_Data")){

            Intent intent = new Intent(Home.this, Doctor_Profile.class);
            intent.putExtra("ID", ID);
            startActivity(intent);
        }else if(TYPE.equals("Client_Data")){

            Intent intent = new Intent(Home.this, Client_Profile.class);
            intent.putExtra("ID", ID);
            startActivity(intent);
        }
    }

}
