package com.example.skillswap;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SkillsToLearnActivity extends AppCompatActivity {

    private GridLayout skillGrid;
    private Button submitButton;
    private ProgressBar submitSpinner;
    private ArrayList<String> selectedSkills = new ArrayList<>();
    private static final int MAX_SKILL_SELECTION = 5;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private final ArrayList<String> learnableSkills = new ArrayList<>(Arrays.asList(
            "Photography", "Graphic Design", "Public Speaking", "Coding", "Cooking",
            "Fitness Training", "Gardening", "Music", "Language Tutoring", "Painting",
            "Writing", "Video Editing", "Yoga", "Business Strategy", "Marketing", "Data Analysis", "Project Management", "Web Development", "Public Speaking",
            "Creative Writing", "Digital Marketing", "Photography", "Cybersecurity",
            "App Development", "UI/UX Design", "Accounting", "Machine Learning",
            "Social Media Strategy", "Art & Illustration", "Video Production"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_list);

        TextView title = findViewById(R.id.titleText);
        skillGrid = findViewById(R.id.skillGrid);
        submitButton = findViewById(R.id.submitButton);
        submitSpinner = findViewById(R.id.submitSpinner);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title.setText("What do you want to learn?");
        applyFadeInAnimation(skillGrid);

        // Create buttons dynamically for each skill
        for (String skill : learnableSkills) {
            Button skillButton = createSkillButton(skill);
            skillGrid.addView(skillButton);
        }

        // Handle the submit button click
        submitButton.setOnClickListener(v -> {
            if (selectedSkills.size() == 0) {
                Toast.makeText(SkillsToLearnActivity.this, "Please select at least one skill", Toast.LENGTH_SHORT).show();
                return;
            }
            saveSkillsToDatabase("learn");
        });
    }

    private Button createSkillButton(final String skill) {
        Button button = new Button(this);
        button.setText(skill);
        button.setBackgroundResource(R.drawable.skill_button_default);
        button.setTextColor(Color.BLACK);
        button.setAllCaps(false);
        button.setOnClickListener(v -> {
            animateButtonClick(v);
            if (selectedSkills.contains(skill)) {
                selectedSkills.remove(skill);
                button.setBackgroundResource(R.drawable.skill_button_default);
            } else {
                if (selectedSkills.size() < MAX_SKILL_SELECTION) {
                    selectedSkills.add(skill);
                    button.setBackgroundResource(R.drawable.skill_button_selected);
                } else {
                    Toast.makeText(SkillsToLearnActivity.this, "You can select up to 5 skills only", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        button.setLayoutParams(params);

        return button;
    }

    private void animateButtonClick(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.1f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.1f, 1.0f);
        scaleX.setDuration(150);
        scaleY.setDuration(150);
        scaleX.start();
        scaleY.start();
    }

    private void applyFadeInAnimation(View view) {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        view.startAnimation(fadeIn);
    }

    private void saveSkillsToDatabase(String field) {
        Log.d("SkillsToLearnActivity", "saveSkillsToDatabase called");
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Log.e("SkillsToLearnActivity", "No user logged in!");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        submitSpinner.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);
        String userId = currentUser.getUid();
        Log.d("SkillsToLearnActivity", "User ID: " + userId);

        Map<String, Object> skillData = new HashMap<>();
        skillData.put(field, selectedSkills);

        db.collection("users").document(userId)
                .update(skillData)
                .addOnSuccessListener(aVoid -> {
                    submitSpinner.setVisibility(View.GONE);
                    Toast.makeText(SkillsToLearnActivity.this, "Skills saved successfully!", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity();
                })
                .addOnFailureListener(this::handleFailure);
    }

    private void handleFailure(Exception e) {
        submitSpinner.setVisibility(View.GONE);
        submitButton.setEnabled(true);
        Toast.makeText(SkillsToLearnActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(SkillsToLearnActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
