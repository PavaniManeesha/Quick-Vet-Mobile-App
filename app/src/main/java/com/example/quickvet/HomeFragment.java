package com.example.quickvet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickvet.Data.Display_Doctor;
import com.example.quickvet.Data.Doctor_Adapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

//    //Creating Necessary Objects
//    public RecyclerView recyclerView;
//    public Doctor_Adapter doctor_adapter;

    TextView doctorName, specialist, location;
    CircleImageView profile;

    FirebaseDatabase DB;
    DatabaseReference DB_Ref;

    FirebaseStorage STORAGE;
    StorageReference ST_REF;
    Bitmap bitmap;
    ConstraintLayout homePopup;

    String ID;

    public HomeFragment() {

    }
    public HomeFragment(String profile, String name, String location, String specialist) {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        DB_Ref = FirebaseDatabase.getInstance().getReference();
//        recyclerView = (RecyclerView)view.findViewById(R.id.home_fragment_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FirebaseRecyclerOptions<Display_Doctor> options =
//                        new FirebaseRecyclerOptions.Builder<Display_Doctor>()
//                                .setQuery(DB_Ref.child("Doctor_Data"), Display_Doctor.class)
//                                .build();
//
//                doctor_adapter = new Doctor_Adapter(options);
//                recyclerView.setAdapter(doctor_adapter);
//                return view;
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        doctor_adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        doctor_adapter.stopListening();

        doctorName = view.findViewById(R.id.display_doctor_name);
        specialist = view.findViewById(R.id.display_doctor_specialist);
        location = view.findViewById(R.id.display_doctor_location);
        profile = view.findViewById(R.id.display_doctor_icon);

        ID = "0773030030";

        //Database Code
        DB = FirebaseDatabase.getInstance();
        DB_Ref = DB.getReference();
        DB_Ref.child("Doctor_Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Display values
                doctorName.setText(snapshot.child(ID).child("full_Name").getValue(String.class));
                specialist.setText(snapshot.child(ID).child("specialization").getValue(String.class));
                location.setText(snapshot.child(ID).child("location").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //STORAGE CODE
        STORAGE = FirebaseStorage.getInstance();
        ST_REF = STORAGE.getReference("Doctor_Data");

        try {
            File file = File.createTempFile("temp",".jpg");
            ST_REF.child(ID).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    profile.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        homePopup = view.findViewById(R.id.home_popup);
        homePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePopup();
            }
        });

        return view;
    }
    public void homePopup(){
        Intent intent = new Intent(getActivity(), Doctor_Popup.class);
        intent.putExtra("ID", ID);
        getActivity().startActivity(intent);
    }
}