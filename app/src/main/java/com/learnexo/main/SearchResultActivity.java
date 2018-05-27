package com.learnexo.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.learnexo.model.user.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView searchRecycler;
    private EditText searchUser;
    private ImageView searchBtn;
    private FirestoreRecyclerAdapter<User, UsersViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchUser = findViewById(R.id.searchLearnexo);
        searchBtn = findViewById(R.id.searchBtn);

        searchRecycler = findViewById(R.id.searchRecycler);
        searchRecycler.setHasFixedSize(true);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));

        String searchText = searchUser.getText().toString();
        firebaseUserSearch(searchText);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(SearchResultActivity.this, "Search", Toast.LENGTH_SHORT).show();
//                String searchText = searchUser.getText().toString();
//                firebaseUserSearch(searchText);
            }
        });

    }

    private void firebaseUserSearch(String searchText) {

        Query query = FirebaseFirestore.getInstance().collection("Users").orderBy("firstName");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, UsersViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull User model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setDetails(getApplicationContext(), model.getFirstName(), model.getDpUrl());
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.search_profile, group, false);

                return new UsersViewHolder(view);
            }

        };

        searchRecycler.setAdapter(adapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private CircleImageView profileImage;
        private TextView userName;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setDetails(Context context, String userNamee, String userImage) {

            userName = mView.findViewById(R.id.userName);
            profileImage = mView.findViewById(R.id.profile_image);

            userName.setText(userNamee);
            Glide.with(context).load(userImage).into(profileImage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

}
