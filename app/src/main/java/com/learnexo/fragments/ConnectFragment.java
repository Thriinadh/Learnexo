package com.learnexo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.learnexo.main.R;
import com.learnexo.util.MyBounceInterpolator;

public class ConnectFragment extends Fragment implements View.OnClickListener {

    private TextView learnersTView;
    private TextView mentorsTView;
    private FrameLayout frameLayout;

    public ConnectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        learnersTView = view.findViewById(R.id.learnersTView);
        learnersTView.setTextColor(Color.RED);

        mentorsTView = view.findViewById(R.id.mentorsTView);
        frameLayout = view.findViewById(R.id.fragment_container);

        learnersTView.setOnClickListener(this);
        mentorsTView.setOnClickListener(this);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LearnersFragment())
                    .commit();
        }

        return view;
    }


    @Override
    public void onClick(View view) {
        Fragment fragment;

        final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 5);
        myAnim.setInterpolator(interpolator);
       // int DefaultTextColor = learnersTView.getTextColors().getDefaultColor();

        switch (view.getId()){
            case R.id.learnersTView:
                fragment = new LearnersFragment();
                replaceFragment(fragment);
                learnersTView.startAnimation(myAnim);
                learnersTView.setTextColor(Color.RED);
                mentorsTView.setTextColor(getResources().getColor(R.color.light_black));
                break;

            case R.id.mentorsTView:
                fragment = new MentorsFragment();
                replaceFragment(fragment);
                mentorsTView.startAnimation(myAnim);
                mentorsTView.setTextColor(Color.RED);
                learnersTView.setTextColor(getResources().getColor(R.color.light_black));
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
