package com.example.fixmajstorapp;


import android.annotation.SuppressLint;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ImageButton openJobsBtn = findViewById(R.id.openJobsBtn);
        openJobsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(HomeActivity.this, JobActivity.class);
                startActivity(registerIntent);
            }
        });



    }
}
