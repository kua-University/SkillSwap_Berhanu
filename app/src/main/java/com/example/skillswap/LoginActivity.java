package com.example.skillswap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView forgotPassword, signUpLink;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        signUpLink = findViewById(R.id.tvRegister);
        passwordToggle = findViewById(R.id.passwordToggleLogin);

        // Handle Login Button Click
        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Input Validation
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(LoginActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                // Firebase Authentication login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Successful login
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // Failed login
                                Toast.makeText(LoginActivity.this, "you have entered wrong Email or password.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Handle Forgot Password Click
        forgotPassword.setOnClickListener(view -> {
            // Navigate to ForgotPasswordActivity
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // Handle Sign Up Click
        signUpLink.setOnClickListener(view -> {
            // Navigate to SignUpActivity
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        // Handle Password Toggle Visibility
        passwordToggle.setOnClickListener(view -> {
            if (isPasswordVisible) {
                passwordInput.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                passwordToggle.setImageResource(R.drawable.ic_eye_closed);  // Change to closed eye icon
            } else {
                passwordInput.setTransformationMethod(null);  // Show password
                passwordToggle.setImageResource(R.drawable.ic_eye_open);  // Change to open eye icon
            }
            isPasswordVisible = !isPasswordVisible;
        });
    }
}