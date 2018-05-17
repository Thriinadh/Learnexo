package com.learnexo.util;

import com.learnexo.model.connect.LearnerOptions;

import java.util.ArrayList;
import java.util.List;

public final class DummyData {

    public static List<LearnerOptions> getData() {
        List<LearnerOptions> list = new ArrayList<>();
        list.add(new LearnerOptions("Groups", "GroupActivity", LearnerOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new LearnerOptions("Find people you like", "GroupActivity", LearnerOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new LearnerOptions(null, null, LearnerOptions.LINE_TYPE, null));
        list.add(new LearnerOptions("Get career guidance", "GetGuidanceActivity", LearnerOptions.OPTION_TYPE, "ic_twotone_person_24px"));
        list.add(new LearnerOptions("Get Project guidance", "GetGuidanceActivity", LearnerOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new LearnerOptions("Get research guidance", "GetGuidanceActivity", LearnerOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
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
