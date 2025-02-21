package com.example.skillswap;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the Toolbar as the ActionBar
        setContentView(R.layout.activity_messages); // Make sure you have this layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button (back button)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Create a TextView to display "Coming Soon"
        TextView comingSoonTextView = new TextView(this);
        comingSoonTextView.setText("Coming Soon");
        comingSoonTextView.setTextSize(24); // Adjust the text size as needed
        comingSoonTextView.setGravity(Gravity.CENTER); // Center the text
        comingSoonTextView.setTextColor(getResources().getColor(android.R.color.black)); // Set text color

        // Set layout parameters to center the TextView
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        comingSoonTextView.setLayoutParams(layoutParams);

        // Set the TextView as the content view
        ViewGroup rootView = findViewById(R.id.content_frame); // Assuming you have a FrameLayout with this ID
        rootView.addView(comingSoonTextView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the Up button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}