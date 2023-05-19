package com.example.quickvet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText userName, password;
    TextView forgotPassword;

    Button loginBtn;
    Button doctorBtn;
    Button patientBtn;

    FirebaseDatabase DB;
    DatabaseReference DB_Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);

        //Login Button Code
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        //When Forgot Password
        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

        //Doctor Button Code
        doctorBtn = findViewById(R.id.doctor_btn);
        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorSign();
            }
        });

        //Patient Button Code
        patientBtn = findViewById(R.id.patient_btn);
        patientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientSign();
            }
        });
    }

    //Login Button Method
    public void openHome(){
        //Getting DB Reference
        DB_Ref = FirebaseDatabase.getInstance().getReference();

        //Getting User Input Values
        String user_name = userName.getText().toString().trim();
        String user_password =password.getText().toString().trim();

        //Check UserName Password Is Empty
        if(user_name.isEmpty()){
            Toast.makeText(this, "Please Enter Your User Name", Toast.LENGTH_LONG).show();
        }else if(user_password.isEmpty()){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
        }else{

            //Validate User Login As Doctor
            DB_Ref.child("Doctor_Data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild(user_name)){
                        //Getting Relevant Password
                        final String get_password = snapshot.child(user_name).child("password").getValue(String.class);

                        //Checking Password
                        if(get_password.equals(user_password)){
                            Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_LONG).show();

                            String Type = "Doctor_Data";
                            ClearControls();

                            //Navigation After Login Success
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            intent.putExtra("TYPE", Type);
                            intent.putExtra("ID", user_name);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"Wrong Password", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

            //Validate User Login As Client
            DB_Ref.child("Client_Data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild(user_name)){
                        //Getting Relevant Password
                        final String get_password = snapshot.child(user_name).child("password").getValue(String.class);

                        //Checking Password
                        if(get_password.equals(user_password)){
                            Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_LONG).show();

                            String Type = "Client_Data";
                            ClearControls();

                            //Navigation After Login Success
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            intent.putExtra("TYPE", Type);
                            intent.putExtra("ID", user_name);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"Wrong Password", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }
    }
    //Cleaning Data
    private void ClearControls() {
        userName.setText("");
        password.setText("");
    }

    //Forgot Password Method
    public void forgotPassword(){
        Intent intent = new Intent(this, Change_Password.class);
        startActivity(intent);
    }

    //Doctor Button Method
    public void doctorSign(){
        Intent intent = new Intent(this, Doctor_Account_Create.class);
        startActivity(intent);
    }

    //Patient Button Method
    public void patientSign(){
        Intent intent = new Intent(this, Create_Client.class);
        startActivity(intent);
    }
}