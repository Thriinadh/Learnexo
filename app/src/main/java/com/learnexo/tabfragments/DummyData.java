package com.learnexo.tabfragments;

import com.learnexo.model.connect.LearnersModel;

import java.util.ArrayList;
import java.util.List;

public final class DummyData {

    public static List<LearnersModel> getData() {
        List<LearnersModel> list = new ArrayList<>();
        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE, "connect_icon"));
        list.add(new LearnersModel("Find people you like", "GroupActivity", LearnersModel.OPTION_TYPE, "edit_nick_name"));
        list.add(new LearnersModel(null, null, LearnersModel.LINE_TYPE, null));
        list.add(new LearnersModel("Get career guidance", "GetGuidanceActivity", LearnersModel.OPTION_TYPE, "home_icon"));
        list.add(new LearnersModel("Get Project guidance", "GetGuidanceActivity", LearnersModel.OPTION_TYPE, "hot_icon"));
        list.add(new LearnersModel("Get research guidance", "GetGuidanceActivity", LearnersModel.OPTION_TYPE, "check_black"));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
//        list.add(new LearnersModel("Groups", "GroupActivity", LearnersModel.OPTION_TYPE));
        return list;
    }
}
