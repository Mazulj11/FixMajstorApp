package com.example.fixmajstorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import com.example.fixmajstorapp.model.Job;

public class AddJobActivity extends AppCompatActivity {

    Button selectImageBtn;
    Button saveBtn;
    Button uploadImageBtn;
    ImageView imagePreview;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://advertisementapp-e50f9-default-rtdb.firebaseio.com/");

    Uri filePath;
    String jobImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        this.selectImageBtn = findViewById(R.id.selectImageBtn);
        this.saveBtn = findViewById(R.id.saveBtn);
        this.uploadImageBtn = findViewById(R.id.uploadImageBtn);
        this.imagePreview = findViewById(R.id.imagePreview);

        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        EditText professionEditTxt = findViewById(R.id.professionEditText);
        EditText nameEditTxt = findViewById(R.id.fullnameEditText);
        EditText locationEditTxt = findViewById(R.id.locationEditText);
        EditText addressEditTxt = findViewById(R.id.addressEditText);


        DatabaseReference jobsReference = mDatabase.getReference("/jobs");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profession = professionEditTxt.getText().toString();
                String name = nameEditTxt.getText().toString();
                String location = locationEditTxt.getText().toString();
                String address = addressEditTxt.getText().toString();
                Double rating = 0.0;
                Job j = new Job( name, location, address, profession, jobImage, rating);
                jobsReference.push().setValue(j);
                Intent i = new Intent(AddJobActivity.this, JobActivity.class);
                startActivity(i);
            }
        });

        ImageButton openHomeBtn = findViewById(R.id.homeBtn);
        openHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(AddJobActivity.this, HomeActivity.class);
                startActivity(registerIntent);
            }
        });

        ImageButton openJobsBtn = findViewById(R.id.openJobsBtn);
        openJobsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(AddJobActivity.this, JobActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            this.filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectImage () {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(i, "Odaberite sliku posla"), 22
        );
    }

    private void uploadImage() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Učitavam sliku");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());


            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload success, retrieve download URL
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        jobImage = task.getResult().toString();
                                        // Continue with your logic here
                                    } else {
                                        // Handle failure to get download URL
                                        Toast.makeText(getApplicationContext(), "Error getting download URL", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure to upload
                            Toast.makeText(getApplicationContext(), "Error uploading image", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

}
