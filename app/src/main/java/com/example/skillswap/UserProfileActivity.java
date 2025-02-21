package com.example.skillswap;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private ImageView profileImageView;
    private TextView fullNameTextView, occupationTextView, bioTextView, ageTextView, emailTextView;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        occupationTextView = findViewById(R.id.occupationTextView);
        bioTextView = findViewById(R.id.bioTextView);
        ageTextView = findViewById(R.id.ageTextView);
        emailTextView = findViewById(R.id.emailTextView);

        firestore = FirebaseFirestore.getInstance();

        String userId = getIntent().getStringExtra("userId");
        if (userId != null) {
            loadUserProfile(userId);
        } else {
            Toast.makeText(this, "User ID not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserProfile(String userId) {
        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("name");
                        String occupation = documentSnapshot.getString("occupation");
                        String bio = documentSnapshot.getString("bio");
                        Long ageLong = documentSnapshot.getLong("age");
                        String email = documentSnapshot.getString("email");
                        String imageUriString = documentSnapshot.getString("image");

                        fullNameTextView.setText(fullName);
                        occupationTextView.setText(occupation);
                        bioTextView.setText(bio);
                        if (ageLong != null) {
                            ageTextView.setText(String.valueOf(ageLong));
                        }
                        emailTextView.setText(email);

                        if (imageUriString != null && !imageUriString.isEmpty()) {
                            Uri imageUri = Uri.parse(imageUriString);
                            Glide.with(this).load(imageUri).circleCrop().into(profileImageView);
                        }
                    } else {
                        Toast.makeText(this, "Profile not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error loading profile", e);
                });
    }
}