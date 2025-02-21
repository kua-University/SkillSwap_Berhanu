package com.example.skillswap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView fullNameTextView, occupationTextView, bioTextView, ageTextView, emailTextView;
    private Button logoutButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private static final String TAG = "ProfileFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImageView = view.findViewById(R.id.profileImageView);
        fullNameTextView = view.findViewById(R.id.fullNameTextView);
        occupationTextView = view.findViewById(R.id.occupationTextView);
        bioTextView = view.findViewById(R.id.bioTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadUserProfile();

        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();

        firestore.collection("users").document(uid)
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
                        Toast.makeText(getContext(), "Profile not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error loading profile", e);
                });
    }

    private void logout() {
        auth.signOut();
        // Navigate to the login screen or another appropriate screen
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}