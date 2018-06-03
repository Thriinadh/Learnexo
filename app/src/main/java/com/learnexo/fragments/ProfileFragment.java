package com.learnexo.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.main.EduDetailsActivity;
import com.learnexo.main.R;
import com.learnexo.main.SetupActivity;
import com.learnexo.util.FirebaseUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private CircleImageView profileImage;
    private TextView userName;
    private TextView description;
    private TextView editProfile;
    private TextView eduDetails;
    private ImageView fullProfileImage;
    private Activity mActivity;

    private String mUserId;
    public static String sDpUrl;
    public static String sName;

    private ImageView eduPlus;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        wireViews(view);
        setUpTabLayout(view);
        getDPandUserName();
        bindEduDetails();
        setUserPostsFragment(savedInstanceState);

        onTabSelectedListener();
        profileImageListener();
        eduDetailsListener();
        editProfileBtnListener();

        return view;
    }

    private void editProfileBtnListener() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetupActivity.class);
                intent.putExtra("IS_EDIT_NAME_CLICKED", true);
                startActivity(intent);
            }
        });
    }

    private void onTabSelectedListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            Fragment fragment;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                  //  tabLayout.setTabTextColors(Color.parseColor("#1da1f2"));
                    fragment = new UserPostsFragment();
                    replaceFragment(fragment);

                }else if(tabLayout.getSelectedTabPosition() == 1){

                    fragment = new UserAnswersFragment();
                    replaceFragment(fragment);

                }else if(tabLayout.getSelectedTabPosition() == 2){

                    fragment = new UserCracksFragment();
                    replaceFragment(fragment);

                }else if(tabLayout.getSelectedTabPosition() == 3){

                    fragment = new UserQuestionsFragment();
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 4){

                    fragment = new UserChallengesFragment();
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 5){

                    fragment = new UserInterestsFragment();
                    replaceFragment(fragment);

                }
                else if(tabLayout.getSelectedTabPosition() == 6){

                    fragment = new UserActivityFragment();
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

    private void bindEduDetails() {
        mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String studiedAt = (String) documentSnapshot.get("studiedAt");

                StringBuilder fullDetails = studiedAt!=null? new StringBuilder(studiedAt):new StringBuilder();
                fullDetails = fullDetails.length() >0 ? fullDetails.append(", "): fullDetails.append("");

                String firstCon = (String) documentSnapshot.get("firstCon");
                if(firstCon!=null) fullDetails = fullDetails.append(firstCon).append(", ");

                String secondCon = (String) documentSnapshot.get("secondCon");
                if(secondCon!=null) fullDetails.append(secondCon).append(", ");

                String degreeType = (String) documentSnapshot.get("degreeType");
                if(degreeType!=null) fullDetails.append(degreeType).append(".");

                String endYear = (String) documentSnapshot.get("endYear");
                if(endYear!=null) fullDetails.append(". Graduated in ").append(endYear).append(".");

                if(fullDetails.length()>0) {
                    eduDetails.setText(fullDetails.toString());
                    eduDetails.setTextColor(Color.BLACK);
                    eduDetails.setEnabled(false);

                    Drawable drawable=null;
                    mActivity=getActivity();
                    if(mActivity!=null)
                        drawable = ContextCompat.getDrawable(mActivity, R.drawable.ic_baseline_school_24px);
                    if(drawable!=null) {
                        eduPlus.setImageDrawable(drawable);
                        drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_black), PorterDuff.Mode.SRC_IN));
                    }
                }

            }
        });
    }

    private void setUserPostsFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UserPostsFragment())
                    .commit();
        }
    }

    private void setUpTabLayout(View view) {
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Answers"));
        tabLayout.addTab(tabLayout.newTab().setText("Cracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Questions"));
        tabLayout.addTab(tabLayout.newTab().setText("Challenges"));
        tabLayout.addTab(tabLayout.newTab().setText("Interests"));
        tabLayout.addTab(tabLayout.newTab().setText("Activity"));
    }

    private void eduDetailsListener() {
        eduDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EduDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void profileImageListener() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void wireViews(View view) {
        editProfile = view.findViewById(R.id.edit_profile);
        profileImage = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.user_name);
        description = view.findViewById(R.id.description);
        eduDetails = view.findViewById(R.id.eduDetails);
        frameLayout = view.findViewById(R.id.fragment_container);
        fullProfileImage = view.findViewById(R.id.fullProfileImage);
        eduPlus = view.findViewById(R.id.imageView7);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getDPandUserName() {
        mUserId = FirebaseUtil.getCurrentUserId();
        mFirebaseUtil.mFirestore.collection("users").document(mUserId).
                collection("reg_details").document("doc").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()) {
                        String firstName = snapshot.getString("firstName");
                        String lastName = snapshot.getString("lastName");

                        sName =firstName.concat(" "+lastName);
                        userName.setText(sName);

                        String descriptionn = snapshot.getString("description");
                        description.setText(descriptionn);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.empty_profilee);

                        sDpUrl =  snapshot.getString("dpUrl");

                        if (sDpUrl !=null&&null!=getActivity()) {
                            Glide.with(getActivity().getApplicationContext()).load(sDpUrl).apply(placeholderRequest).into(profileImage);
                        }
                    }
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

}
