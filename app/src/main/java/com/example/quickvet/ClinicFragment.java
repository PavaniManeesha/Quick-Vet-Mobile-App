package com.example.quickvet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickvet.Data.Clinic_Adapter;
import com.example.quickvet.Data.Display_Clinic;
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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClinicFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView name, location, number;
    CircleImageView profile;

    FirebaseDatabase DB;
    DatabaseReference DB_Ref;

    FirebaseStorage STORAGE;
    StorageReference ST_REF;
    Bitmap bitmap;

    public String ID;

//    RecyclerView recyclerView;
//    Clinic_Adapter clinic_adapter;
//    ArrayList<Display_Clinic> list;

    public ClinicFragment() {

    }

    public static ClinicFragment newInstance(String param1, String param2) {
        ClinicFragment fragment = new ClinicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_clinic, container, false);

//        recyclerView = (RecyclerView)view.findViewById(R.id.clinic_fragment_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FirebaseRecyclerOptions<Display_Clinic> options =
//                new FirebaseRecyclerOptions.Builder<Display_Clinic>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Clinic_Data"), Display_Clinic.class)
//                        .build();
//
//        clinic_adapter = new Clinic_Adapter(options);
//        recyclerView.setAdapter(clinic_adapter);
        ID = getActivity().getIntent().getStringExtra("ID").trim();

        name = view.findViewById(R.id.display_clinic_name);
        location = view.findViewById(R.id.display_clinic_location);
        number = view.findViewById(R.id.display_clinic_number);
        profile = view.findViewById(R.id.display_clinic_icon);

        //Database Code
        DB = FirebaseDatabase.getInstance();
        DB_Ref = DB.getReference();
        DB_Ref.child("Clinic_Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(ID)){
                    //Display values
                    name.setText(snapshot.child(ID).child("clinic_Name").getValue(String.class));
                    location.setText(snapshot.child(ID).child("clinic_Location").getValue(String.class));
                    number.setText(snapshot.child(ID).child("contact_Number").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //STORAGE CODE
        STORAGE = FirebaseStorage.getInstance();
        ST_REF = STORAGE.getReference("Clinic_Data");

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

        return view;
    }
}