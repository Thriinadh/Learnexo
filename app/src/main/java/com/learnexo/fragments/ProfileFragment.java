package com.learnexo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

    private String mUserId;
    public static String sDpUrl;
    public static String sName;

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

        editProfile = view.findViewById(R.id.edit_profile);
        profileImage = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.user_name);
        description = view.findViewById(R.id.description);
        eduDetails = view.findViewById(R.id.eduDetails);
        frameLayout = view.findViewById(R.id.fragment_container);
        fullProfileImage = view.findViewById(R.id.fullProfileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        eduDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EduDetailsActivity.class);
                startActivity(intent);
            }
        });

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Answers"));
        tabLayout.addTab(tabLayout.newTab().setText("Cracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Questions"));
        tabLayout.addTab(tabLayout.newTab().setText("Challenges"));
        tabLayout.addTab(tabLayout.newTab().setText("Interests"));
        tabLayout.addTab(tabLayout.newTab().setText("Activity"));

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UserPostsFragment())
                    .commit();
        }

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

        getDPandUserName();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetupActivity.class);
                intent.putExtra("IS_EDIT_NAME_CLICKED", true);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
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
