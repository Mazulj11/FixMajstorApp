package com.example.fixmajstorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixmajstorapp.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private TextView fullNameTxt, mailTxt, locationTxt, addressTxt, telephoneTxt;
    private ImageView profileImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullNameTxt = findViewById(R.id.fullNameTxt);
        mailTxt = findViewById(R.id.mailTxt);
        locationTxt = findViewById(R.id.locationTxt);
        addressTxt = findViewById(R.id.addressTxt);
        telephoneTxt = findViewById(R.id.telephoneTxt);
        profileImageView = findViewById(R.id.editProfileImageView);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                    if (userProfile != null) {
                        fullNameTxt.setText(userProfile.getImePrezime());
                        mailTxt.setText(userProfile.getEmail());
                        locationTxt.setText(userProfile.getLokacija());
                        addressTxt.setText(userProfile.getAdresa());
                        telephoneTxt.setText(String.valueOf(userProfile.getTel()));
                        String imagePath = userProfile.getImagePath();
                        if (imagePath != null && !imagePath.isEmpty()) {
                            Picasso.get().load(imagePath).into(profileImageView);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UserProfileActivity.this, "Greška pri dohvaćanju podataka", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ImageButton openHomeBtn = findViewById(R.id.homeBtn);
        openHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(UserProfileActivity.this, HomeActivity.class);
                startActivity(registerIntent);
            }
        });
        ImageButton openJobsBtn = findViewById(R.id.openJobsBtn);
        openJobsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(UserProfileActivity.this, JobActivity.class);
                startActivity(registerIntent);
            }
        });
        Button editProfileBtn = findViewById(R.id.openEditProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfileModal();
            }
        });
    }

    public void openEditProfileModal() {
        EditProfileDialogFragment dialogFragment = new EditProfileDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "EditProfileDialog");
    }

    public static void updateUserProfile(Context context, String newName, String newEmail, String newLocation, String newAddress, String newPhone) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            // Ažurirajte podatke korisnika u bazi podataka
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("imePrezime", newName);
            updateData.put("email", newEmail);
            updateData.put("lokacija", newLocation);
            updateData.put("adresa", newAddress);
            updateData.put("tel", newPhone);

            userRef.updateChildren(updateData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Podaci ažurirani.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Greška pri ažuriranju podataka", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    public static void updateUserProfileImage(Context context, String imagePath) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            // Ažurirajte putanju slike u bazi podataka
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("imagePath", imagePath);

            userRef.updateChildren(updateData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Slika ažurirana.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Greška pri ažuriranju slike", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
