package com.learnexo.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
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


        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String searchText = searchUser.getText().toString();
                firebaseUserSearch(searchText);

                if(!TextUtils.isEmpty(searchText))
                    adapter.startListening();

                if(TextUtils.isEmpty(searchText))
                    adapter.stopListening();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void firebaseUserSearch(final String searchText) {

        Query query = FirebaseFirestore.getInstance().collection("Users").orderBy("firstName").startAt(searchText).endAt(searchText+ "\uf8ff");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, UsersViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull User model) {
                int i=searchText.length();
                holder.setDetails(getApplicationContext(), model.getFirstName(), model.getDpUrl(), i);
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

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

        public void setDetails(Context context, String userNamee, String userImage, int i) {

            userName = mView.findViewById(R.id.userName);
            profileImage = mView.findViewById(R.id.profile_image);

            SpannableStringBuilder str = new SpannableStringBuilder(userNamee);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            userName.setText(str);
            Glide.with(context).load(userImage).into(profileImage);
        }
    }

}
