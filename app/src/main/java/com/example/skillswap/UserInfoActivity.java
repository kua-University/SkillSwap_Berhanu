package com.example.skillswap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Button uploadImageButton, saveButton;
    private EditText fullNameInput, occupationInput, bioInput, ageInput;
    private Uri profileImageUri;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        uploadImageButton = findViewById(R.id.btnUploadImage);
        fullNameInput = findViewById(R.id.etFullName);
        occupationInput = findViewById(R.id.etOccupation);
        bioInput = findViewById(R.id.etBio);
        ageInput = findViewById(R.id.etAge);
        saveButton = findViewById(R.id.btnSave);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        uploadImageButton.setOnClickListener(v -> openGallery());
        saveButton.setOnClickListener(v -> validateAndSaveUserProfile());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            Glide.with(this).load(profileImageUri).circleCrop().into(profileImageView);
        }
    }

    private void validateAndSaveUserProfile() {
        String fullName = fullNameInput.getText().toString().trim();
        String occupation = occupationInput.getText().toString().trim();
        String bio = bioInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();

        if (fullName.isEmpty() || occupation.isEmpty() || bio.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        saveUserDataToFirestore(fullName, occupation, bio, age);
    }

    private void saveUserDataToFirestore(String fullName, String occupation, String bio, int age) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        String lowercaseName = fullName.toLowerCase(); // Create lowercase name for searching

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", fullName);
        userData.put("occupation", occupation);
        userData.put("bio", bio);
        userData.put("age", age);
        userData.put("email", currentUser.getEmail());
        userData.put("searchName", lowercaseName); // Add searchName

        if (profileImageUri != null) {
            userData.put("image", profileImageUri.toString());
        }

        firestore.collection("users").document(uid)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserInfoActivity.this, SkillsToTeachActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}