package com.learnexo.tabfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learnexo.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentorsFragment extends Fragment {


    public MentorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mentors, container, false);
        // Inflate the layout for this fragment
        return view;
    }

}
