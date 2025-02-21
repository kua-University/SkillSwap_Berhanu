package com.example.skillswap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirestoreRecyclerAdapter<PostModel, PostViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadPosts();

        return view;
    }

    private void loadPosts() {
        Query query = db.collection("posts").orderBy("content", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<PostModel> options = new FirestoreRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostModel, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostModel model) {
                String postId = getSnapshots().getSnapshot(position).getId();
                holder.bind(model, postId);
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // ViewHolder class
    private class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivUserProfile, ivLike, ivShare;
        private TextView tvUserName, tvContent, tvLikeCount;
        private List<Integer> profileDrawables; // List to hold drawable resources
        private Random random;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivShare = itemView.findViewById(R.id.ivShare);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);

            // Initialize the list of drawables
            profileDrawables = new ArrayList<>();
            profileDrawables.add(R.drawable.ic_profile);
            profileDrawables.add(R.drawable.ic_pro);
            profileDrawables.add(R.drawable.ic_pr);

            // Initialize the random number generator
            random = new Random();
        }

        public void bind(PostModel post, String postId) {
            // Set the username
            tvUserName.setText(post.getUserName()); // Assuming PostModel has a getUserName() method

            tvContent.setText(post.getContent());
            updateLikeCount(post);
            loadRandomProfile(); // Load a random profile image
            FirebaseUser currentUser = auth.getCurrentUser();
            String currentUserId = currentUser != null ? currentUser.getUid() : "";
            // Set the like icon based on whether the current user has liked the post
            if (post.getLikes() != null && post.getLikes().contains(currentUserId)) {
                ivLike.setImageResource(R.drawable.ic_liked);
                ivLike.setTag("liked");
            } else {
                ivLike.setImageResource(R.drawable.ic_unliked);
                ivLike.setTag("unliked");
            }

            ivLike.setOnClickListener(v -> handleLikeClick(post, postId));
            ivShare.setOnClickListener(v -> handleShareClick(post));
        }

        private void loadRandomProfile() {
            // Generate a random index
            int randomIndex = random.nextInt(profileDrawables.size());

            // Get the drawable resource ID from the list
            int drawableId = profileDrawables.get(randomIndex);

            // Set the image to the ImageView
            ivUserProfile.setImageResource(drawableId);
        }

        private void updateLikeCount(PostModel post) {
            if (post.getLikes() != null) {
                tvLikeCount.setText(String.valueOf(post.getLikes().size()));
            } else {
                tvLikeCount.setText("0");
            }
        }

        private void handleLikeClick(PostModel post, String postId) {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }
            String currentUserId = currentUser.getUid();
            List<String> likes = post.getLikes();
            if (likes == null) {
                likes = new ArrayList<>();
            }
            // Create a new list to avoid modifying the original list directly
            List<String> updatedLikes = new ArrayList<>(likes);

            if (updatedLikes.contains(currentUserId)) {
                updatedLikes.remove(currentUserId);
            } else {
                updatedLikes.add(currentUserId);
            }

            db.collection("posts").document(postId).update("likes", updatedLikes)
                    .addOnSuccessListener(aVoid -> {
                        // Update the post's likes with the new list
                        post.setLikes(updatedLikes);
                        updateLikeCount(post);
                        if (updatedLikes.contains(currentUserId)) {
                            ivLike.setImageResource(R.drawable.ic_liked);
                            ivLike.setTag("liked");
                        } else {
                            ivLike.setImageResource(R.drawable.ic_unliked);
                            ivLike.setTag("unliked");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to update like.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating like", e);
                    });
        }

        private void handleShareClick(PostModel post) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this post: " + post.getContent());
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }
    }
}