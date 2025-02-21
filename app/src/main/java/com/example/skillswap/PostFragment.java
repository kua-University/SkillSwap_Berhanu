package com.example.skillswap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    private ImageView ivUserProfile;
    private TextView tvUserName;
    private EditText etPostContent;
    private Button btnCreatePost;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        ivUserProfile = view.findViewById(R.id.ivUserProfile);
        tvUserName = view.findViewById(R.id.tvUserName);
        etPostContent = view.findViewById(R.id.etPostContent);
        btnCreatePost = view.findViewById(R.id.btnCreatePost);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Set default profile image
        ivUserProfile.setImageResource(R.drawable.ic_profile);

        // Fetch and display user name
        fetchUserName();

        btnCreatePost.setOnClickListener(v -> createPost());

        return view;
    }

    private void fetchUserName() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");
                            if (userName != null) {
                                tvUserName.setText(userName);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(getContext(), "Could not retrieve user data", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(exception -> {
                        Log.d(TAG, "get failed with ", exception);
                        Toast.makeText(getContext(), "Could not retrieve user data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void createPost() {
        String postContent = etPostContent.getText().toString().trim();

        if (postContent.isEmpty()) {
            Toast.makeText(getContext(), "Please write something to post.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userName = tvUserName.getText().toString();

            PostModel post = new PostModel();
            post.setUserId(userId);
            post.setContent(postContent);
            post.setLikes(new ArrayList<>());
            post.setUserName(userName); // Set the userName here!

            db.collection("posts").add(post)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getContext(), "Post created successfully!", Toast.LENGTH_SHORT).show();
                        etPostContent.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getContext(), "Failed to create post.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}