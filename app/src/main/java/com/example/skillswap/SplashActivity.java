package com.example.skillswap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tvSkillSwap = findViewById(R.id.tvSkillSwap);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Apply fade-in animation
        tvSkillSwap.startAnimation(fadeIn);

        // Delay and apply fade-out animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            tvSkillSwap.startAnimation(fadeOut);
        }, 2000); // 2 seconds delay

        // Delay and start the next activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000); // 3 seconds delay
    }
}