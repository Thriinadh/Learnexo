package com.learnexo.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.learnexo.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TextFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    public TextFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_q, container, false);

        // get the listview
        expListView = view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

//        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v,
//                                        int groupPosition, long id) {
//                setListViewHeight(parent, groupPosition);
//                return false;
//            }
//        });

        Display newDisplay = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width-50, width);
        } else {
            expListView.setIndicatorBoundsRelative(width-50, width);
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Fundamentals");
        listDataHeader.add("Programming");
        listDataHeader.add("Databases");
        listDataHeader.add("Networking");
        listDataHeader.add("Artificial");

        // Adding child data
        List<String> fundamentals = new ArrayList<>();
        fundamentals.add("Operating Systems");
        fundamentals.add("Compiler Design");
        fundamentals.add("MFCS");
        fundamentals.add("Microprocessors");
        fundamentals.add("Microcontrollers");
        fundamentals.add("Computer Basics");

        List<String> programming = new ArrayList<>();
        programming.add("Java");
        programming.add("Python");
        programming.add("Android");
        programming.add("Ruby on Rails");
        programming.add("Design Patterns");
        programming.add("Scala");

        List<String> databases = new ArrayList<>();
        databases.add("Firebase");
        databases.add("NoSQL");
        databases.add("Relational Databases");
        databases.add("Oracle Database");
        databases.add("Microsoft Azure");

        List<String> networking = new ArrayList<>();
        networking.add("Wireless Networking");
        networking.add("CCNA Networking");
        networking.add("Firewalls protection");
        networking.add("Ethical Hacking");
        networking.add("Safe Networks");

        List<String> artificial = new ArrayList<>();
        artificial.add("Machine Learning");
        artificial.add("Game Theory");
        artificial.add("Speech Recognition");
        artificial.add("Robotics");
        artificial.add("Machine Algorithms");

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), fundamentals);
        listDataChild.put(listDataHeader.get(1), programming);
        listDataChild.put(listDataHeader.get(2), databases);
        listDataChild.put(listDataHeader.get(3), networking);
        listDataChild.put(listDataHeader.get(4), artificial);
    }

//    private void setListViewHeight(ExpandableListView listView,
//                                   int group) {
//        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
//        int totalHeight = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
//                View.MeasureSpec.EXACTLY);
//        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
//            View groupItem = listAdapter.getGroupView(i, false, null, listView);
//            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//
//            totalHeight += groupItem.getMeasuredHeight();
//
//            if (((listView.isGroupExpanded(i)) && (i != group))
//                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
//                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
//                    View listItem = listAdapter.getChildView(i, j, false, null,
//                            listView);
//                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//
//                    totalHeight += listItem.getMeasuredHeight();
//
//                }
//            }
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        int height = totalHeight
//                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
//        if (height < 10)
//            height = 200;
//        params.height = height;
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }

}
