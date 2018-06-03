package com.learnexo.model.connect;

public class ConnectOptions {
    private String mOption;
    private int mOptionType;//line or option
    private String mOptionTarget;
    private String mOptionIcon;

    public static final int OPTION_TYPE = 0;
    public static final int LINE_TYPE = 1;

    public ConnectOptions(String mOption, String mOptionTarget, int mOptionType, String mOptionIcon) {
        this.mOption = mOption;
        this.mOptionType = mOptionType;
        this.mOptionTarget = mOptionTarget;
        this.mOptionIcon = mOptionIcon;
    }

    public int getmOptionType() {
        return mOptionType;
    }

    public void setmOptionType(int mOptionType) {
        this.mOptionType = mOptionType;
    }

    public String getmOptionTarget() {
        return mOptionTarget;
    }

    public void setmOptionTarget(String mOptionTarget) {
        this.mOptionTarget = mOptionTarget;
    }

    public String getmOption() {
        return mOption;
    }

    public void setmOption(String mOption) {
        this.mOption = mOption;
    }

    public String getmOptionIcon() {
        return mOptionIcon;
    }

    public void setmOptionIcon(String mOptionIcon) {
        this.mOptionIcon = mOptionIcon;
    }
}
