package com.example.skillswap;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText etSignUpEmail, etSignUpPassword, etConfirmPassword;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private ImageView passwordToggleSignUp, confirmPasswordToggle;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progressBar);
        passwordToggleSignUp = findViewById(R.id.passwordToggleSignUp);
        confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle);
        tvLogin = findViewById(R.id.tvLogin);

        btnSignUp.setOnClickListener(v -> signUpWithEmailAndPassword());
        passwordToggleSignUp.setOnClickListener(v -> togglePasswordVisibility(etSignUpPassword, passwordToggleSignUp));
        confirmPasswordToggle.setOnClickListener(v -> togglePasswordVisibility(etConfirmPassword, confirmPasswordToggle));
        tvLogin.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void signUpWithEmailAndPassword() {
        String email = etSignUpEmail.getText().toString().trim();
        String password = etSignUpPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validateInputs(email, password, confirmPassword)) {
            return;
        }

        createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid(), email);
                        } else {
                            Toast.makeText(SignUpActivity.this, "User creation successful, but user data not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String uid, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);

        db.collection("users").document(uid)
                .set(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, UserInfoActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to save user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs(String email, String password, String confirmPassword) {
        if (email.isEmpty()) {
            etSignUpEmail.setError("Email is required");
            etSignUpEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etSignUpEmail.setError("Invalid email format");
            etSignUpEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etSignUpPassword.setError("Password is required");
            etSignUpPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etSignUpPassword.setError("Password must be at least 6 characters");
            etSignUpPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirm Password is required");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleImageView) {
        if (editText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggleImageView.setImageResource(R.drawable.ic_eye_open);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleImageView.setImageResource(R.drawable.ic_eye_closed);
        }
    }
}