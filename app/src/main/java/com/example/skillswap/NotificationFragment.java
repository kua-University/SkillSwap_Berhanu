package com.example.skillswap;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements NotificationAdapter.OnNotificationClickListener {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationList;
    private FirebaseFirestore db;
    private ListenerRegistration postsListener;
    private TextView emptyStateTextView;

    private static final String CHANNEL_ID = "post_notifications";
    private static final String TAG = "NotificationFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        emptyStateTextView = view.findViewById(R.id.emptyStateTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(), notificationList, this);
        recyclerView.setAdapter(notificationAdapter);

        db = FirebaseFirestore.getInstance();

        createNotificationChannel();
        listenForNewPosts();

        return view;
    }

    private void listenForNewPosts() {
        Query query = db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING);

        postsListener = query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getContext(), "Failed to fetch notifications.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error fetching notifications", error);
                return;
            }

            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        DocumentSnapshot doc = dc.getDocument();
                        if (doc.getMetadata().hasPendingWrites()) continue; // Ignore local changes

                        String postId = doc.getId();
                        String userName = doc.getString("userName"); // Get the username from the document
                        String notificationText = userName + " has posted a new post";

                        // Add the notification to the list
                        notificationList.add(0, new NotificationModel(notificationText, postId));
                        notificationAdapter.notifyItemInserted(0); // Notify the adapter of the new item
                        updateEmptyStateVisibility();

                        // Show the system notification
                        showSystemNotification(userName, postId);
                    }
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Post Notifications";
            String description = "Notifies when a user posts a new update.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showSystemNotification(String userName, String postId) {
        Intent intent = new Intent(getContext(), MainActivity.class); // Use MainActivity as the target
        intent.putExtra("navigateToHome", true); // Add a flag to indicate navigation to HomeFragment
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New Post Alert!")
                .setContentText(userName + " has posted a new post")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public void onNotificationClick(String postId) {
        navigateToHomeFragment(postId);
    }

    private void navigateToHomeFragment(String postId) {
        // Navigate to HomeFragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment()); // Replace with your container ID
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (postsListener != null) {
            postsListener.remove();
        }
    }
    private void updateEmptyStateVisibility() {
        if (notificationList.isEmpty()) {
            emptyStateTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}