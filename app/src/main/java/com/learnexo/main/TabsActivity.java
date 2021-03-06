package com.learnexo.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.fragments.ConnectFragment;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.ProfileFragment;
import com.learnexo.fragments.TextFragment;
import com.learnexo.fragments.VideoFragment;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

import static com.learnexo.main.SetupActivity.EXTRA_IS_SKIPPED;

public class TabsActivity extends AppCompatActivity {

    public static final String EXTRA_PUBLISH_TYPE ="com.learnexo.main.TabsActivity.EXTRA_PUBLISH_TYPE";

    private boolean save_profile;


    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView mToolbarTitle;
    private TabLayout tabLayout;
    private CardView mCardView;
    boolean flag = true;
    private FloatingActionButton mFloatingBtn;
    private Button mShareInfoBtn;
    private Button mAskBtn;
    private Button mChallengeBtn;
    private LinearLayout FAB_Layout;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    int currentTab;

    private ViewPager viewPager;
    private TabPagerAdapter mAdapter;


    private int[] tabIcons = {
            R.drawable.ic_tab_home,
            R.drawable.ic_tab_video,
            R.drawable.ic_tab_text,
            R.drawable.ic_tab_connect,
            R.drawable.ic_tab_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        wireViews();

        setupToolbar();
        setupTabPagerAdapter();
        setupTablayout();
        tabSelectListener();

        save_profile = getIntent().getBooleanExtra("SAVE_PROFILE_FROM_PROFILE",false);

        floatingBtnListener();
        shareInfoBtnListener();
        askQuestionBtnListener();
        postChallengeBtnListener();

        gotoFeedtab();

    }
    @Override
    public void onBackPressed() {

           int pos = viewPager.getCurrentItem();
           if(pos == 1 || pos == 2 || pos == 3 || pos == 4){
            viewPager.setCurrentItem(0);
            mToolbarTitle.setText(R.string.feedTabTitle);
        } else if(mCardView != null && !flag) {
            mCardView.setVisibility(View.INVISIBLE);
            flag = true;
        }
        else
            new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void wireViews() {
        appBarLayout = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolbar);
       // searchLearnexo = findViewById(R.id.searchLearnexo);
        FAB_Layout = findViewById(R.id.FAB_Layout);
        mCardView = findViewById(R.id.cardView);

        mFloatingBtn = findViewById(R.id.floatBtn);
        mShareInfoBtn = findViewById(R.id.shareBtn);
        mAskBtn = findViewById(R.id.askBtn);
        mChallengeBtn = findViewById(R.id.challengeBtn);
    }

    public void startPublishActivity(String publish_type) {
        Intent shareIntent = new Intent(TabsActivity.this, PublishActivity.class);
        shareIntent.putExtra(EXTRA_PUBLISH_TYPE, publish_type);
        startActivity(shareIntent);
    }

    private void shareInfoBtnListener() {
        mShareInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity(getString(R.string.share_info));
            }
        });
    }

    private void postChallengeBtnListener() {
        mChallengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity(getString(R.string.postChallenge));
            }
        });
    }

    private void askQuestionBtnListener() {
        mAskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity(getString(R.string.askYourQuestion));
            }
        });
    }

    private void floatingBtnListener() {
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag) {

                    // Check if the runtime version is at least Lollipop
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // get the center for the clipping circle
                        // int cx = mCardView.getWidth() / 2;
                        // int cy = mCardView.getHeight() / 2;

                        // get the center for the clipping circle
                        int cx = (view.getLeft() + view.getRight()) / 2;
                        int cy = (view.getTop() + view.getBottom()) / 2;

                        int startRadius = 0;
                        // get the final radius for the clipping circle
                        int endRadius = Math.max(view.getWidth(), view.getHeight());

                        // get the final radius for the clipping circle
                        // float finalRadius = (float) Math.hypot(cx, cy);

                        // create the animator for this view (the start radius is zero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(mCardView, cx, cy, startRadius, endRadius);

                        // make the view visible and start the animation
                        mCardView.setVisibility(View.VISIBLE);
                        anim.start();
                    } else {
                        // set the view to visible without a circular reveal animation below Lollipop
                        mCardView.setVisibility(View.VISIBLE);
                    }

                    flag = false;
                } else {

                    // Check if the runtime version is at least Lollipop
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // get the center for the clipping circle
                        // int cx = mCardView.getWidth() / 2;
                        // int cy = mCardView.getHeight() / 2;

                        // get the center for the clipping circle
                        int cx = (view.getLeft() + view.getRight()) / 2;
                        int cy = (view.getTop() + view.getBottom()) / 2;

                        // get the initial radius for the clipping circle
                        // float initialRadius = (float) Math.hypot(cx, cy);

                        int endRadius = 0;
                        // get the final radius for the clipping circle
                        int startRadius = Math.max(view.getWidth(), view.getHeight());

                        // create the animation (the final radius is zero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(mCardView, cx, cy, startRadius, endRadius);

                        // make the view invisible when the animation is done
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mCardView.setVisibility(View.INVISIBLE);
                            }
                        });

                        // start the animation
                        anim.start();
                    } else {
                        // set the view to visible without a circular reveal animation below Lollipop
                        mCardView.setVisibility(View.INVISIBLE);
                    }
                    flag = true;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(save_profile)
            viewPager.setCurrentItem(4);

        TabLayout.Tab tab = tabLayout.getTabAt(currentTab);
        tab.select();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCardView != null)
            mCardView.setVisibility(View.INVISIBLE);
            flag = true;
    }

    private void tabSelectListener() {
        tabLayout.addOnTabSelectedListener (
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                            tab.getIcon().setColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN);

                        hideCardviewInFeedFragmentWhenUsergoesToOtherTabs();
                        setToolbarTitle(tab);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);

                            tab.getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }

                    private void setToolbarTitle(TabLayout.Tab tab) {
                        switch (tab.getPosition()) {

                            case 0:
                                viewPager.setCurrentItem(0);
                                mToolbarTitle.setText(R.string.feedTabTitle);
                                currentTab=0;
                                break;

                            case 1:
                                viewPager.setCurrentItem(1);
                                mToolbarTitle.setText(R.string.videosTabTitle);
                                currentTab=1;
                                break;

                            case 2:
                                viewPager.setCurrentItem(2);
                                mToolbarTitle.setText(R.string.ivqsTabTitle);
                                currentTab=2;
                                break;

                            case 3:
                                viewPager.setCurrentItem(3);
                                mToolbarTitle.setText(R.string.connectTabTitle);
                                currentTab=3;
                                break;

                            case 4:
                                viewPager.setCurrentItem(4);
                                mToolbarTitle.setText(R.string.profileTabTitle);
                                currentTab=4;
                                break;

                        }
                    }

                    private void hideCardviewInFeedFragmentWhenUsergoesToOtherTabs() {
                        int pos = viewPager.getCurrentItem();
                        if(pos == 1 || pos == 2 || pos == 3 || pos == 4)
                            hideCardAndFABView();
                        else
                            FAB_Layout.setVisibility(View.VISIBLE);
                    }

                });
    }

        private void hideCardAndFABView() {
        if(FAB_Layout != null && mCardView != null) {
            mCardView.setVisibility(View.INVISIBLE);
            FAB_Layout.setVisibility(View.INVISIBLE);
            flag = true;
        }
    }

    public void hideCardView() {
        if(mCardView != null)
            mCardView.setVisibility(View.INVISIBLE);
        flag = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.log_out:
                logout();
                return true;
            case R.id.profile_setup:
                Intent setupIntent = new Intent(TabsActivity.this, SetupActivity.class);
                setupIntent.putExtra("IS_PROFILE_EDIT",true);
                startActivity(setupIntent);
                return  true;
            case R.id.search:
                Intent intent = new Intent(TabsActivity.this, SearchResultActivity.class);
                startActivity(intent);
                return true;
            case R.id.bookmarks:
                Intent bookMarkIntent = new Intent(TabsActivity.this, BookmarksActivity.class);
                startActivity(bookMarkIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void gotoFeedtab() {
        // Check if user is signed in (non-null) and update UI accordingly.
        if (FirebaseUtil.doesUserExist()) {
            checkInterestsAndProfile(FirebaseUtil.getCurrentUserId());
        } else {
            gotoLoginPage();
        }
    }

    private void checkInterestsAndProfile(final String userId) {
        mFirebaseUtil.mFirestore.collection("users").document(userId).collection("interests").document("doc1").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    gotoInterestsActivity(task, userId);
                } else {
                    toastErrorMsg(task);
                }
            }

            private void toastErrorMsg(@NonNull Task<DocumentSnapshot> task) {
                String errorMessage = task.getException().getMessage();
                Toast.makeText(TabsActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
            }

            private void gotoInterestsActivity(@NonNull Task<DocumentSnapshot> task, String userId) {
                if (!task.getResult().exists()) {
                    Intent intent = new Intent(TabsActivity.this, InterestsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    checkifSkippedProfile(userId);
                }
            }
                                     private void checkifSkippedProfile(final String userId) {
                                        //check if skip is there - if skip he is second time user and do not want to complete his profile, then do not ckeck the below one
                                         mFirebaseUtil.mFirestore.collection("users").document(userId).get()
                                                 .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                         if(task.isSuccessful()) {
                                                             Boolean isSkipped= (Boolean) task.getResult().get("IS_SKIPPED_PROFILE");
                                                             if(isSkipped==null) {
                                                                    checkProfileCompletion(userId);
                                                                }

                                                         } else {
                                                             toastErrorMsg(task);
                                                         }
                                                     }
                                                 });

                                     }

                                     private void checkProfileCompletion(String userId) {
                                         // - if skip is not available then check the below path if both are not available then he is first time user
                                         //so send him to setup activity

                                         mFirebaseUtil.mFirestore.collection("users").document(userId).
                                                 collection("reg_details").document("doc").get()
                                                 .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                 if(task.isSuccessful()) {
                                                     boolean is_skipped_profile_completion=getIntent().getBooleanExtra(EXTRA_IS_SKIPPED,false);
                                                     DocumentSnapshot snapshot = task.getResult();

                                                     //intent can only be useful if he did not remove the app from memory.
                                                     if(!snapshot.contains("dpUrl")&&!is_skipped_profile_completion) {
                                                         Intent intent = new Intent(TabsActivity.this, SetupActivity.class);
                                                         startActivity(intent);
                                                         finish();
                                                     }

                                                 } else {
                                                     toastErrorMsg(task);

                                                 }
                                             }
                                         });
                                     }
                                 });
    }

    private void setupTablayout() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mToolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.learnexo);
        }
        mToolbarTitle.setText(R.string.feed);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TabsActivity.class);
        return intent;
    }

    private void logout() {
        FirebaseUtil.sAuthSingOut();
        LoginManager.getInstance().logOut();
        gotoLoginPage();
    }

    private void gotoLoginPage() {
        Intent accountIntent = new Intent(TabsActivity.this, MainActivity.class);
        startActivity(accountIntent);
        finish();
    }

    private void setupTabIcons() {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);
            tabLayout.getTabAt(4).setIcon(tabIcons[4]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);
    }

    private void setupTabPagerAdapter() {
        viewPager = findViewById(R.id.viewpager);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager());

        mAdapter.addFragment(new FeedFragment());
        mAdapter.addFragment(new VideoFragment());
        mAdapter.addFragment(new TextFragment());
        mAdapter.addFragment(new ConnectFragment());
        mAdapter.addFragment(new ProfileFragment());

        viewPager.setAdapter(mAdapter);

        int limit = (mAdapter.getCount() > 1 ? mAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
    }


    //inner custom class
    class TabPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        TabPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return null to display only the icon
            return null;
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);

        }
    }

}
