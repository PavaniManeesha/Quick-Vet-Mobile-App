package com.example.quickvet.Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickvet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class Clinic_Adapter extends FirebaseRecyclerAdapter<Display_Clinic, Clinic_Adapter.ClinicViewHolder> {

    public Clinic_Adapter(@NonNull FirebaseRecyclerOptions<Display_Clinic> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Clinic_Adapter.ClinicViewHolder holder, int position, @NonNull Display_Clinic model) {
        holder.name.setText(model.getClinic_Name());
        holder.location.setText(model.getClinic_Location());
        holder.contactNumber.setText(model.getClinic_Number());
        Glide.with(holder.profile.getContext()).load(model.getpUrl()).into(holder.profile);
    }

    @NonNull
    @Override
    public Clinic_Adapter.ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clinic_popup_items, parent, false);
        return new Clinic_Adapter.ClinicViewHolder(view);
    }

    public class ClinicViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile;
        TextView name, location, contactNumber;

        public ClinicViewHolder(@NonNull View view){
            super(view);

            profile = view.findViewById(R.id.display_clinic_icon);
            name = view.findViewById(R.id.display_clinic_name);
            location = view.findViewById(R.id.display_clinic_location);
            contactNumber = view.findViewById(R.id.display_clinic_number);
        }
    }
}
