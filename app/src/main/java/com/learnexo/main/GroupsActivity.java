package com.learnexo.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.learnexo.fragments.DiscoverFragment;
import com.learnexo.fragments.ManageGroupsFragment;
import com.learnexo.fragments.MyGroupsFragment;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        setupToolbar();
        setupTabPagerAdapter();
        setupTablayout();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Groups to Discuss");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupTablayout() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupTabPagerAdapter() {
        viewPager = findViewById(R.id.viewpager);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager());

        mAdapter.addFragment(new MyGroupsFragment(), "My Groups");
        mAdapter.addFragment(new DiscoverFragment(), "Discover");
        mAdapter.addFragment(new ManageGroupsFragment(), "Manage");

        viewPager.setAdapter(mAdapter);

        int limit = (mAdapter.getCount() > 1 ? mAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
    }

    class TabPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TabPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
