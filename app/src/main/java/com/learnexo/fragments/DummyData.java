package com.learnexo.fragments;

import com.learnexo.model.connect.LearnerOptions;

import java.util.ArrayList;
import java.util.List;

public final class DummyData {

    public static List<LearnerOptions> getData() {
        List<LearnerOptions> list = new ArrayList<>();
        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE, "connect_icon"));
        list.add(new LearnerOptions("Find people you like", "GroupActivity", LearnerOptions.OPTION_TYPE, "edit_nick_name"));
        list.add(new LearnerOptions(null, null, LearnerOptions.LINE_TYPE, null));
        list.add(new LearnerOptions("Get career guidance", "GetGuidanceActivity", LearnerOptions.OPTION_TYPE, "home_icon"));
        list.add(new LearnerOptions("Get Project guidance", "GetGuidanceActivity", LearnerOptions.OPTION_TYPE, "hot_icon"));
        list.add(new LearnerOptions("Get research guidance", "GetGuidanceActivity", LearnerOptions.OPTION_TYPE, "check_black"));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
//        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE));
        return list;
    }
}
