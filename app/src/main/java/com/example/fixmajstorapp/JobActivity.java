package com.example.fixmajstorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.fixmajstorapp.adapter.JobAdapter;
import com.example.fixmajstorapp.model.Job;

public class JobActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    JobAdapter jobAdapter;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://advertisementapp-e50f9-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        this.recyclerView = findViewById(R.id.jobListView);
        this.recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        FirebaseRecyclerOptions<Job> options = new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(mDatabase.getReference("jobs"), Job.class)
                .build();


        this.jobAdapter = new JobAdapter(options);
        this.recyclerView.setAdapter(this.jobAdapter);

//        FloatingActionButton openAddMovieBtn = findViewById(R.id.openAddMovieBtn);
//        openAddMovieBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(MovieActivity.this, AddMovieActivity.class);
//                startActivity(i);
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.jobAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.jobAdapter.stopListening();
    }
}