package com.example.fixmajstorapp;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fixmajstorapp.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class EditProfileDialogFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editNameText, editEmailTxt, editLocationTxt, editAddressTxt, editPhoneTxt;
    private ImageView profileImageView;
    private UserProfile userProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Inicijalizirajte elemente za unos podataka
        editNameText = view.findViewById(R.id.editNameText);
        editEmailTxt = view.findViewById(R.id.editEmailTxt);
        editLocationTxt = view.findViewById(R.id.editLocationTxt);
        editAddressTxt = view.findViewById(R.id.editAddressTxt);
        editPhoneTxt = view.findViewById(R.id.editPhoneTxt);
        profileImageView = view.findViewById(R.id.editProfileImageView);

        // Postavite trenutne podatke o korisniku u polja za unos
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userProfile = dataSnapshot.getValue(UserProfile.class);

                    if (userProfile != null) {
                        editNameText.setText(userProfile.getImePrezime());
                        editEmailTxt.setText(userProfile.getEmail());
                        editLocationTxt.setText(userProfile.getLokacija());
                        editAddressTxt.setText(userProfile.getAdresa());
                        editPhoneTxt.setText(String.valueOf(userProfile.getTel()));

                        // Postavite trenutnu sliku korisnika
                        Picasso.get().load(userProfile.getImagePath()).into(profileImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Greška pri dohvaćanju podataka", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Dodajte OnClickListener za odabir nove slike
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pokrenite galeriju ili kameru putem Intent-a
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Dodajte gumbe za spremanje promjena i zatvaranje modala
        Button saveEditBtn = view.findViewById(R.id.saveEditBtn);
        Button closeBtn = view.findViewById(R.id.closeBtn);

        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editNameText.getText().toString();
                String newEmail = editEmailTxt.getText().toString();
                String newLocation = editLocationTxt.getText().toString();
                String newAddress = editAddressTxt.getText().toString();
                String newPhone = editPhoneTxt.getText().toString();

                UserProfileActivity.updateUserProfile(getContext(), newName, newEmail, newLocation, newAddress, newPhone);

                dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zatvorite dijalog bez spremanja promjena
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            if (imageUri.getPath() != null && !imageUri.getPath().isEmpty()) {
                Picasso.get().load(imageUri).into(profileImageView);

                String imagePath = imageUri.toString();
                UserProfileActivity.updateUserProfileImage(getContext(), imagePath);
            } else {
                Toast.makeText(getContext(), "Putanja slike je prazna", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String saveImageToFirebaseStorage(Uri imageUri) {
        // Kreirajte referencu u Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("user_images/" + UUID.randomUUID().toString());

        // Učitajte sliku u Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Dobijte putanju do sačuvane slike
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imagePath = uri.toString();
                        // Ažurirajte putanju slike u korisničkom profilu u Firebase Realtime Database
                        UserProfileActivity.updateUserProfileImage(getContext(), imagePath);
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Greška pri učitavanju slike", Toast.LENGTH_SHORT).show());

        return "";
    }
}
