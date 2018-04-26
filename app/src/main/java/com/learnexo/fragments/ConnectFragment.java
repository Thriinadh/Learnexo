package com.learnexo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import com.learnexo.main.MyBounceInterpolator;
import com.learnexo.main.R;

public class ConnectFragment extends Fragment implements View.OnClickListener {
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
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
