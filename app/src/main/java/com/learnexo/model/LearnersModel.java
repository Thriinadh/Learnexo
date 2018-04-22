package com.learnexo.model;

public class LearnersModel {

    public static final int OPTION_TYPE = 0;
    public static final int LINE_TYPE = 1;

    private String mName;
    private int mType;
    private String mTargetActivity;
    private String imageName;

    public LearnersModel(String mName, String mTargetActivity, int mType, String imageName) {
        this.mName = mName;
        this.mType = mType;
        this.mTargetActivity = mTargetActivity;
        this.imageName = imageName;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmTargetActivity() {
        return mTargetActivity;
    }

    public void setmTargetActivity(String mTargetActivity) {
        this.mTargetActivity = mTargetActivity;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
