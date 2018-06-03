package com.learnexo.util;

import com.learnexo.model.connect.ConnectOptions;

import java.util.ArrayList;
import java.util.List;

public final class DummyData {

    public static List<ConnectOptions> getLearnerOptions() {
        List<ConnectOptions> list = new ArrayList<>();
        list.add(new ConnectOptions("Groups", "GroupActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions("Find People You Like", "FindPeopleYouLikeActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Get Career Guidance", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_24px"));
        list.add(new ConnectOptions("Get Project Guidance", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions("Get Research Guidance", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Get Referred", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_24px"));
        list.add(new ConnectOptions("Do Industry Work", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Become Freelancer", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions("Do Research", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions("Apply For Internship", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Get Project Funding", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_24px"));
        list.add(new ConnectOptions("Get Research Funding", "GetGuidanceActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));

        return list;
    }

    public static List<ConnectOptions> getMentorOptions() {
        List<ConnectOptions> list = new ArrayList<>();
        list.add(new ConnectOptions("Guide Career", "GuideActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions("Guide Project", "GuideActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions("Guide Research", "GuideActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Give Industry Work", "MentorsGiveActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_24px"));
        list.add(new ConnectOptions("Give Your Reference", "MentorsGiveActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Find Freelancers", "MentorsFindActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions("Find Research Students", "MentorsFindActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_24px"));
        list.add(new ConnectOptions("Find Interns", "MentorsFindActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Fund Project", "FundActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions("Fund Research", "FundActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_people_24px"));
        list.add(new ConnectOptions(null, null, ConnectOptions.LINE_TYPE, null));

        list.add(new ConnectOptions("Groups", "GroupActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_add_24px"));
        list.add(new ConnectOptions("Find People You Like", "FindPeopleYouLikeActivity", ConnectOptions.OPTION_TYPE, "ic_twotone_person_24px"));


        return list;
    }

}
