package com.example.skillswap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    ImageView notificationIcon, profileIcon;
    Toolbar toolbar;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the toolbar and other views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        notificationIcon = findViewById(R.id.notificationIcon);
        profileIcon = findViewById(R.id.profileIcon);
        toolbar = findViewById(R.id.toolbar);
        titleText = findViewById(R.id.toolbarTitle);

        // Set the toolbar as the app bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Remove the default title

        // Set up bottom navigation item selection listener
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_AUTO);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Set up click listeners for notification and profile icons
        notificationIcon.setOnClickListener(v -> replaceFragment(new NotificationFragment(), "Notifications"));
        profileIcon.setOnClickListener(v -> replaceFragment(new ProfileFragment(), "Profile"));

        // Check if the intent has the flag to navigate to HomeFragment
        if (getIntent().getBooleanExtra("navigateToHome", false)) {
            replaceFragment(new HomeFragment(), "Home");
            bottomNavigationView.setSelectedItemId(R.id.homeFragment);
        } else {
            // Initially replace the fragment with HomeFragment
            replaceFragment(new HomeFragment(), "Home");
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String title = "";
        View view = bottomNavigationView.findViewById(item.getItemId());

        // Add animation effect to the selected bottom navigation item
        if (view != null) {
            view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction(() ->
                    view.animate().scaleX(1f).scaleY(1f).setDuration(150));
        }

        // Handle fragment navigation based on selected item
        int itemId = item.getItemId();
        if (itemId == R.id.homeFragment) {
            selectedFragment = new HomeFragment();
            title = "Home";
        } else if (itemId == R.id.searchFragment) {
            selectedFragment = new SearchFragment();
            title = "Search";
        } else if (itemId == R.id.postFragment) {
            selectedFragment = new PostFragment();
            title = "Post";
        } else if (itemId == R.id.messagesActivity) {
            startActivity(new Intent(MainActivity.this, MessagesActivity.class));
            return true;
        }

        // Replace the fragment and update the toolbar title
        if (selectedFragment != null) {
            replaceFragment(selectedFragment, title);
        }

        return true;
    }

    // Method to replace the fragment in the container
    private void replaceFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add custom animations for fragment transitions
        fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
        );
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Update the toolbar title dynamically
        titleText.setText(title);
    }
}