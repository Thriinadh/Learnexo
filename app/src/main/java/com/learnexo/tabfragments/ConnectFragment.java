package com.learnexo.tabfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import com.learnexo.main.InterestsActivity;
import com.learnexo.main.MyBounceInterpolator;
import com.learnexo.main.R;

import java.util.ArrayList;
import java.util.List;

public class ConnectFragment extends Fragment implements View.OnClickListener {

//    private ViewPager viewPager;
//    private ViewPagerAdapter adapter;
    private Button learnersBtn;
    private Button mentorsBtn;
    private FrameLayout frameLayout;

    public ConnectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        learnersBtn = view.findViewById(R.id.learnersBtn);
        mentorsBtn = view.findViewById(R.id.mentorsBtn);
        frameLayout = view.findViewById(R.id.fragment_container);

        learnersBtn.setOnClickListener(this);
        mentorsBtn.setOnClickListener(this);

//        viewPager = view.findViewById(R.id.viewPager);
//        setupViewPager(viewPager);

        // Inflate the layout for this fragment

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LearnersFragment())
                    .commit();
        }

        return view;
    }

//    private void setupViewPager(ViewPager viewPager) {
//        adapter = new ViewPagerAdapter(getFragmentManager());
//
//        adapter.addFragment(new LearnersFragment(), "ONE");
//        adapter.addFragment(new MentorsFragment(), "TWO");
//
//        viewPager.setAdapter(adapter);
//    }

    @Override
    public void onClick(View view) {
        Fragment fragment;

        final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 5);
        myAnim.setInterpolator(interpolator);

        switch (view.getId()){
            case R.id.learnersBtn:
                fragment = new LearnersFragment();
                replaceFragment(fragment);
                learnersBtn.startAnimation(myAnim);
                break;
            case R.id.mentorsBtn:
                fragment = new MentorsFragment();
                replaceFragment(fragment);
                mentorsBtn.startAnimation(myAnim);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            // return null to display only the icon
//            return null;
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//    }

}
