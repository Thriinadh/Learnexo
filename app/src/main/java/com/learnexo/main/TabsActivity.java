package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.fragments.ConnectFragment;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.HotQFragment;
import com.learnexo.fragments.ProfileFragment;
import com.learnexo.fragments.VideoFragment;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class TabsActivity extends AppCompatActivity {

    public static final String EXTRA_SHARE_POST_TO_FEED = "com.learnexo.main.extra_share";

    private Toolbar toolbar;
    private TextView mToolbarTitle;
    private TabLayout tabLayout;

    private ViewPager viewPager;
    private ViewPagerAdapter mAdapter;

    private int[] tabIconIDArray = {
            R.drawable.home_icon,
            R.drawable.video_icon,
            R.drawable.hot_icon,
            R.drawable.connect_icon,
            R.drawable.profile_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        setupToolbar();
        setupViewPager();
        setupTablayout();
        tabSelectListener();

        checkIfUserExistsAndHandle();

    }

    private void checkIfUserExistsAndHandle() {
        // Check if user is signed in (non-null) and update UI accordingly.
        if (FirebaseUtil.doesUserExist()) {
            checkIfUserProvidedInterests(FirebaseUtil.getCurrentUserId());
        } else {
            gotoLoginPage();
        }
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
                startActivity(setupIntent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkIfUserProvidedInterests(final String userId) {
        new FirebaseUtil().mFirestore.collection("Users").document(userId).collection("Interests")
                         .document("Inter").get().addOnCompleteListener(
                                 new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    takeUserInterests(task, userId);
                } else {
                    toastErrorMsg(task);
                }
            }

            private void toastErrorMsg(@NonNull Task<DocumentSnapshot> task) {
                String errorMessage = task.getException().getMessage();
                Toast.makeText(TabsActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
            }

            private void takeUserInterests(@NonNull Task<DocumentSnapshot> task, String userId) {
                if (!task.getResult().exists()) {
                    Intent intent = new Intent(TabsActivity.this, InterestsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    checkIfUserhadSetup(userId);

                }
            }

                                     private void checkIfUserhadSetup(String userId) {
                                         new FirebaseUtil().mFirestore.collection("Users").document(userId).
                                                 collection("Setup Details").document("Setup Fields").get()
                                                 .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                 if(task.isSuccessful()) {
                                                     if(!task.getResult().exists()) {
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

    @Override
    protected void onResume() {
        super.onResume();
        handleIntentFromPublishActivity();
    }

    private void handleIntentFromPublishActivity() {
        TabLayout.Tab tab = tabLayout.getTabAt(getIntent().getIntExtra(EXTRA_SHARE_POST_TO_FEED, 0));
        tab.select();
    }

    private void tabSelectListener() {
        tabLayout.addOnTabSelectedListener (
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                        tab.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        hideCardviewInFeedFragmentWhenUsergoesToOtherTabs();
                        setToolbarTitle(tab);
                    }


                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        tab.getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }

                    private void setToolbarTitle(TabLayout.Tab tab) {
                        switch (tab.getPosition()) {

                            case 0:
                                viewPager.setCurrentItem(0);
                                mToolbarTitle.setText("Feed");
                                break;

                            case 1:
                                viewPager.setCurrentItem(1);
                                mToolbarTitle.setText("Videos");
                                break;

                            case 2:
                                viewPager.setCurrentItem(2);
                                mToolbarTitle.setText("Hot Q's");
                                break;

                            case 3:
                                viewPager.setCurrentItem(3);
                                mToolbarTitle.setText("Connect");
                                break;

                            case 4:
                                viewPager.setCurrentItem(4);
                                mToolbarTitle.setText("Profile");
                                break;

                        }
                    }

                    private void hideCardviewInFeedFragmentWhenUsergoesToOtherTabs() {
                        Fragment feedFragment = mAdapter.getItem(0);
                        int pos = viewPager.getCurrentItem();
                        if(pos == 1 || pos == 2 || pos == 3 || pos == 4)
                            ((FeedFragment)feedFragment).hideCardview();
                    }


                });
    }

    private void setupTablayout() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        mToolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.learnexo);
        }
        mToolbarTitle.setText(R.string.feed);
    }

    public static Intent newIntent(Context context, int tabNo) {
        Intent feedIntent = new Intent(context, TabsActivity.class);
        feedIntent.putExtra(EXTRA_SHARE_POST_TO_FEED, tabNo);
        return feedIntent;
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
        tabLayout.getTabAt(0).setIcon(tabIconIDArray[0]);
        tabLayout.getTabAt(1).setIcon(tabIconIDArray[1]);
        tabLayout.getTabAt(2).setIcon(tabIconIDArray[2]);
        tabLayout.getTabAt(3).setIcon(tabIconIDArray[3]);
        tabLayout.getTabAt(4).setIcon(tabIconIDArray[4]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.viewpager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mAdapter.addFragment(new FeedFragment(), "ONE");
        mAdapter.addFragment(new VideoFragment(), "TWO");
        mAdapter.addFragment(new HotQFragment(), "THREE");
        mAdapter.addFragment(new ConnectFragment(), "Four");
        mAdapter.addFragment(new ProfileFragment(), "Five");

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
    }


    //inner custom class
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
