package com.example.skillswap;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<User, UserViewHolder> adapter;
    private CollectionReference usersRef;
    private TextView emptyStateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyStateTextView = view.findViewById(R.id.emptyStateTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        usersRef = FirebaseFirestore.getInstance().collection("users");

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase().trim();
                searchUsers(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this implementation
            }
        });

        return view;
    }

    private void searchUsers(String searchText) {
        Query baseQuery = usersRef.orderBy("searchName");
        Query query;

        if (searchText.isEmpty()) {
            query = baseQuery; // Show all users if search text is empty
        } else {
            query = baseQuery.startAt(searchText).endAt(searchText + "\uf8ff");
        }

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                updateEmptyStateVisibility();
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.e(TAG, "Firestore Error: " + e.getMessage());
                Toast.makeText(getContext(), "Error fetching users", Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView occupationTextView;
        private ImageView profileImageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            occupationTextView = itemView.findViewById(R.id.occupationTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }

        public void bind(User user) {
            nameTextView.setText(user.getName());
            occupationTextView.setText(user.getOccupation());

            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Glide.with(getContext())
                        .load(user.getImage())
                        .circleCrop()
                        .into(profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.ic_profile);
            }
        }
    }

    private void updateEmptyStateVisibility() {
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && querySnapshot.isEmpty()) {
                    emptyStateTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyStateTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}