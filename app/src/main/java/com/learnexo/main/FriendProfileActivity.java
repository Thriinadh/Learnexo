package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.learnexo.fragments.ProfileActivityFragment;
import com.learnexo.fragments.ProfileAnswers;
import com.learnexo.fragments.ProfileChallengeFragment;
import com.learnexo.fragments.ProfileCracksFragment;
import com.learnexo.fragments.ProfileInterests;
import com.learnexo.fragments.ProfileQuestions;
import com.learnexo.fragments.UserPostsFragment;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendProfileActivity extends AppCompatActivity {

    public static final String EXTRA_PUBLISHER_NAMEE = "com.learnexo.publisher_namee";
    public static final String EXTRA_PUBLISHER_DPP = "com.learnexo.publisher_dpp";

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private CircleImageView profileImage;
    private TextView userName;
    private TextView description;

    private String mUserId;
    public static String sDpUrl;
    public static String sName;

    private String publisherId;
    private String postId;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        Intent intent=getIntent();
        publisherId=intent.getStringExtra("PUBLISHER_ID");
        postId = intent.getStringExtra("POST_ID");
        String publisherName = intent.getStringExtra(EXTRA_PUBLISHER_NAMEE);
        String publisherDP = intent.getStringExtra(EXTRA_PUBLISHER_DPP);

        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        description = findViewById(R.id.description);
        frameLayout = findViewById(R.id.fragment_container);

        userName.setText(publisherName);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(publisherDP).apply(requestOptions).into(profileImage);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Answers"));
        tabLayout.addTab(tabLayout.newTab().setText("Cracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Questions"));
        tabLayout.addTab(tabLayout.newTab().setText("Challenges"));
        tabLayout.addTab(tabLayout.newTab().setText("Interests"));
        tabLayout.addTab(tabLayout.newTab().setText("Activity"));

        final Bundle bundle = new Bundle();
        bundle.putString("OTHER_PROFILE_ID",publisherId);
        bundle.putString("OTHER_PROFILE_NAME",publisherName);
        bundle.putString("OTHER_PROFILE_DP",publisherDP);
        bundle.putBoolean("IS_OTHER_PROFILE",true);

        final Fragment userPostFragment= new UserPostsFragment();
        if (savedInstanceState == null) {
            userPostFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, userPostFragment)
                    .commit();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            Fragment fragment;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    //  tabLayout.setTabTextColors(Color.parseColor("#1da1f2"));
                    replaceFragment(userPostFragment);

                }else if(tabLayout.getSelectedTabPosition() == 1){

                    fragment = new ProfileAnswers();
                    replaceFragment(fragment);

                }else if(tabLayout.getSelectedTabPosition() == 2){

                    fragment = new ProfileCracksFragment();
                    replaceFragment(fragment);

                }else if(tabLayout.getSelectedTabPosition() == 3){

                    fragment = new ProfileQuestions();
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 4){

                    fragment = new ProfileChallengeFragment();
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 5){

                    fragment = new ProfileInterests();
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 6){

                    fragment = new ProfileActivityFragment();
                    replaceFragment(fragment);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public static Intent newIntent(Context context, User publisher, String postId) {

        Intent intent = new Intent(context, FriendProfileActivity.class);
        intent.putExtra(EXTRA_PUBLISHER_NAMEE, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DPP, publisher.getDpUrl());
        intent.putExtra("POST_ID", postId);
        intent.putExtra("PUBLISHER_ID",publisher.getUserId());
        return intent;

    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
