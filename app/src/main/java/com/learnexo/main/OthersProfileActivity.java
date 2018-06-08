package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.fragments.UserActivityFragment;
import com.learnexo.fragments.UserAnswersFragment;
import com.learnexo.fragments.UserChallengesFragment;
import com.learnexo.fragments.UserCracksFragment;
import com.learnexo.fragments.UserInterestsFragment;
import com.learnexo.fragments.UserQuestionsFragment;
import com.learnexo.fragments.UserPostsFragment;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OthersProfileActivity extends AppCompatActivity {

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

    private TextView eduDetails;
    private TextView empDetails;
    private TextView livingPlace;
    private ImageView eduPlus;
    private ImageView empPlus;
    private ImageView locPlus;

    private TextView following;
    private TextView followers;

    private RelativeLayout eduRelative;
    private RelativeLayout empRelative;
    private RelativeLayout locationRelative;

    private String publisherId;
   // private String postId;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        Intent intent=getIntent();
        publisherId=intent.getStringExtra("PUBLISHER_ID");
      //  postId = intent.getStringExtra("POST_ID");
        final String publisherName = intent.getStringExtra(EXTRA_PUBLISHER_NAMEE);
        String publisherDP = intent.getStringExtra(EXTRA_PUBLISHER_DPP);

        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        description = findViewById(R.id.description);
        frameLayout = findViewById(R.id.fragment_container);
        eduDetails = findViewById(R.id.eduDetails);
        empDetails = findViewById(R.id.empDetails);
        livingPlace = findViewById(R.id.livingPlace);
        eduRelative = findViewById(R.id.eduRelative);
        empRelative = findViewById(R.id.empRelative);
        locationRelative = findViewById(R.id.locationRelative);
        eduPlus = findViewById(R.id.imageView7);
        empPlus = findViewById(R.id.imageView8);
        locPlus = findViewById(R.id.imageView9);
        following = findViewById(R.id.following);
        followers = findViewById(R.id.followers);

        mFirebaseUtil.mFirestore.collection("users").document(publisherId)
                .collection("following").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                int size = documents.size();
                String docSize = Integer.toString(size);
                int length = docSize.length();
                String total = docSize + " Following";

                SpannableStringBuilder sb = new SpannableStringBuilder(total);
                sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                sb.setSpan(new ForegroundColorSpan(Color.BLACK), 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                following.setText(sb);
            }
        });

        mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("followers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            String total;
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                int size = documents.size();
                String docSize = Integer.toString(size);
                int length = docSize.length();
                if (size>1)
                    total = docSize + " Followers";
                else
                    total = docSize + " Follower";

                SpannableStringBuilder sb = new SpannableStringBuilder(total);
                sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                sb.setSpan(new ForegroundColorSpan(Color.BLACK), 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                followers.setText(sb);
            }
        });


        mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                if(documents.size() == 0) {

                    eduRelative.setVisibility(View.INVISIBLE);
                    locationRelative.setVisibility(View.INVISIBLE);
                    empPlus.setVisibility(View.INVISIBLE);
                    empDetails.setText(publisherName + " has not created their details");
                    empDetails.setTextSize(16);
                    empDetails.setTextColor(getResources().getColor(R.color.light_black));

                }

                for (DocumentSnapshot documentSnapshot : documents){
                    String id = documentSnapshot.getId();

                    if(id.equals("eduDetails")){

                        String studiedAt = (String) documentSnapshot.get("studiedAt");

                        StringBuilder fullDetails = studiedAt!=null? new StringBuilder(studiedAt):new StringBuilder();
                        if(fullDetails.length()>0) fullDetails.append(". ");

                        String firstCon = (String) documentSnapshot.get("firstCon");
                        if(firstCon!=null) fullDetails = fullDetails.append(firstCon).append(". ");

                        String secondCon = (String) documentSnapshot.get("secondCon");
                        if(secondCon!=null) fullDetails.append(secondCon).append(". ");

                        String degreeType = (String) documentSnapshot.get("degreeType");
                        if(degreeType!=null) fullDetails.append(degreeType).append(".");

                        String endYear = (String) documentSnapshot.get("endYear");
                        if(endYear!=null) fullDetails.append(" Graduated in ").append(endYear).append(".");

                        if(fullDetails.length()>0) {
                            eduDetails.setText(fullDetails.toString());
                            eduDetails.setTextColor(Color.BLACK);
                            eduDetails.setEnabled(false);

                            Drawable drawable;

                                drawable = ContextCompat.getDrawable(OthersProfileActivity.this, R.drawable.ic_baseline_school_24px);
                            if(drawable!=null) {
                                eduPlus.setImageDrawable(drawable);
                                drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_black), PorterDuff.Mode.SRC_IN));
                            }
                        }

                    }

                    if (id.equals("empDetails")) {

                        String position = (String) documentSnapshot.get("position");

                        StringBuilder fullDetails = position!=null? new StringBuilder(position):new StringBuilder();
                        if(fullDetails.length()>0) fullDetails.append(". ");

                        String company = (String) documentSnapshot.get("company");
                        if(company!=null) fullDetails = fullDetails.append(company).append(". ");

                        if(fullDetails.length()>0) {
                            empDetails.setText(fullDetails.toString());
                            empDetails.setTextColor(Color.BLACK);
                            empDetails.setEnabled(false);

                            Drawable drawable;

                                drawable = ContextCompat.getDrawable(OthersProfileActivity.this, R.drawable.ic_baseline_work_24px);
                            if(drawable!=null) {
                                empPlus.setImageDrawable(drawable);
                                drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_black), PorterDuff.Mode.SRC_IN));
                            }
                        }

                    }

                    if (id.equals("locationDetails")) {

                        String location = (String) documentSnapshot.get("location");
                        boolean check = (boolean) documentSnapshot.get("currentStatus");

                        StringBuilder fullDetails = (location != null && check) ? new StringBuilder("Lives in "+location) : new StringBuilder();
                        if (location != null && !check) fullDetails = new StringBuilder("Lived in "+location);
                        if(fullDetails.length()>0) fullDetails.append(". ");

                        long startYear = (long) documentSnapshot.get("startYear");
                        if(startYear != 0) fullDetails = fullDetails.append("From ").append(startYear).append(". ");

                        if(fullDetails.length()>0) {
                            livingPlace.setText(fullDetails.toString());
                            livingPlace.setTextColor(Color.BLACK);
                            livingPlace.setEnabled(false);

                            Drawable drawable;

                                drawable = ContextCompat.getDrawable(OthersProfileActivity.this, R.drawable.ic_baseline_location_on_24px);
                            if(drawable!=null) {
                                locPlus.setImageDrawable(drawable);
                                drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_black), PorterDuff.Mode.SRC_IN));
                            }
                        }

                    }

                }

            }
        });


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
        userPostFragment.setArguments(bundle);

        if (savedInstanceState == null) {
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

                    replaceFragment(userPostFragment);

                }else if(tabLayout.getSelectedTabPosition() == 1){

                    fragment = new UserAnswersFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);

                }else if(tabLayout.getSelectedTabPosition() == 2){
                    fragment = new UserCracksFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);

                }else if(tabLayout.getSelectedTabPosition() == 3){

                    fragment = new UserQuestionsFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 4){

                    fragment = new UserChallengesFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 5){

                    fragment = new UserInterestsFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 6){

                    fragment = new UserActivityFragment();
                    fragment.setArguments(bundle);
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

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }

    public static Intent newIntent(Context context, User publisher) {

        Intent intent = new Intent(context, OthersProfileActivity.class);
        intent.putExtra(EXTRA_PUBLISHER_NAMEE, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DPP, publisher.getDpUrl());
       // intent.putExtra("POST_ID", postId);
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
