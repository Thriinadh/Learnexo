package com.learnexo.model.core;

import java.util.List;

public class SubBranch {

    private String name;
    private List<Topic> topicList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }
}
