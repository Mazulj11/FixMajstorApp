package com.example.fixmajstorapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import com.example.fixmajstorapp.R;
import com.example.fixmajstorapp.model.Job;

public class JobAdapter extends FirebaseRecyclerAdapter<Job, JobAdapter.JobViewHolder> {

    Context cxt;
    public JobAdapter(@NonNull FirebaseRecyclerOptions<Job> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobAdapter.JobViewHolder holder, int position, @NonNull Job model) {
        holder.nameTxt.setText(model.getName());
        holder.locationTxt.setText(model.getLocation());
        holder.addressTxt.setText(model.getAddress());
        holder.professionTxt.setText(model.getProfession());
        holder.ratingBar.setRating(model.getRating().floatValue());
        Picasso
                .get()
                .load(model.getImage())
                .into(holder.jobImg);
    }

    @NonNull
    @Override
    public JobAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.cxt = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }


    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, locationTxt, addressTxt, professionTxt;
        RatingBar ratingBar;
        ImageView jobImg;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            professionTxt = itemView.findViewById(R.id.professionTxt);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            jobImg = itemView.findViewById(R.id.jobImg);
        }
    }
}
