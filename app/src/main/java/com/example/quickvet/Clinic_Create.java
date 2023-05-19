package com.example.quickvet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickvet.Data.Clinic;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Clinic_Create extends AppCompatActivity {

    EditText name, contactNumber,location, address;
    Button chooseBtn, createBtn;

    CircleImageView profile;
    ImageView backBtn;
    Bitmap bitmap;
    Uri ImageUri;

    FirebaseDatabase DB;
    DatabaseReference DB_Ref;

    FirebaseStorage STORAGE;
    StorageReference ST_REF;

    //Getting ID
    public String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_create);

        //Assigning Variables using IDs
        name = findViewById(R.id.create_clinic_name);
        contactNumber = findViewById(R.id.create_clinic_contact_number);
        location = findViewById(R.id.create_clinic_location);
        address = findViewById(R.id.create_clinic_address);

        profile = findViewById(R.id.create_clinic_profile);

        backBtn = findViewById(R.id.back_icon);
        chooseBtn = findViewById(R.id.create_clinic_btn_1);
        createBtn = findViewById(R.id.create_clinic_btn_2);

        //Back Button Code
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Clinic_Create.this, Doctor_Profile.class);
                startActivity(intent);
            }
        });

        //Choose Photo Button Code
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityIfNeeded(Intent.createChooser(intent, "select image"), 1);
            }
        });

        //Add Clinic Button Code
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClinic();
            }
        });
    }
    //After Choose Photo Button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == 1 && resultCode == RESULT_OK){
            ImageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(ImageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Create Account Button Code
    public void createClinic(){
        //Storage Code
        STORAGE = FirebaseStorage.getInstance();
        ST_REF = STORAGE.getReference().child("Clinic_Data");

        //Database Code
        DB = FirebaseDatabase.getInstance();
        DB_Ref = DB.getReference().child("Clinic_Data");

        //Validate Inputs
        if(name.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Your Clinic Name", Toast.LENGTH_LONG).show();
        }else if(contactNumber.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Your Contact Number", Toast.LENGTH_LONG).show();
        }else if(location.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Your Clinic Location", Toast.LENGTH_LONG).show();
        }else if(address.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Your Clinic Location", Toast.LENGTH_LONG).show();
        }else if(name.getText().toString().length() > 60){
            Toast.makeText(this, "Valid Clinic Name Must Be Less Than 60 Character", Toast.LENGTH_LONG).show();
        }else if(contactNumber.getText().toString().length() != 10){
        Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
        }else if(location.getText().toString().length() > 40){
            Toast.makeText(this, "Valid Location Be Less Than 20 Characters", Toast.LENGTH_LONG).show();
        }else if(address.getText().toString().length() > 250){
            Toast.makeText(this, "Valid Address Be Less Than 250 Characters", Toast.LENGTH_LONG).show();
        }else {
            //Create new Clinic Object
            Clinic clinic = new Clinic();

            //Assign Values to the Clinic Object
            ID = getIntent().getStringExtra("ID").trim();
            clinic.setClinic_Name(name.getText().toString().trim());
            clinic.setContact_Number(contactNumber.getText().toString().trim());
            clinic.setClinic_Location(location.getText().toString().trim());
            clinic.setClinic_Address(address.getText().toString().trim());
            clinic.setDoctor_ID(ID);

            //Passing Values to the DB
            DB_Ref.child(ID).setValue(clinic);
            Toast.makeText(getApplicationContext(), "Clinic Added Successfully", Toast.LENGTH_LONG).show();

            //Passing Image to the DB
            ST_REF.child(ID).putFile(ImageUri);

            ClearControls();

            Intent intent = new Intent(this, Doctor_Profile.class);
            startActivity(intent);
        }
    }
    //Cleaning Data
    private void ClearControls() {
        name.setText("");
        contactNumber.setText("");
        location.setText("");
        address.setText("");
    }
}