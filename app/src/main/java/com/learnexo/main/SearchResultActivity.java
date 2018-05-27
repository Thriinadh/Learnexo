package com.learnexo.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    FirestoreRecyclerOptions<User> options;
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

        final String searchText = searchUser.getText().toString();

        noResultAdapter();


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = FirebaseFirestore.getInstance().collection("Users").orderBy("firstName").startAt(searchText).endAt(searchText +"\uf8ff");
                options = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                createAdapter(options);

                searchRecycler.setAdapter(adapter);
            }
        });

    }

    private void noResultAdapter() {
        Query query = FirebaseFirestore.getInstance().collection("subjects");
        options = new FirestoreRecyclerOptions.Builder<User>().
                setQuery(query, User.class).
                build();
        createAdapter(options);
    }

    private void createAdapter(final FirestoreRecyclerOptions<User> options) {
        adapter = new FirestoreRecyclerAdapter<User, UsersViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull User model) {
                if(model!=null)
                    holder.setDetails(getApplicationContext(), model.getFirstName(), model.getDpUrl());
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.search_profile, group, false);

                return new UsersViewHolder(view);
            }

        };
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
