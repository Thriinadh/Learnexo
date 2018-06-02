package com.learnexo.model.video.chapter;

import java.io.Serializable;

class ChapterTestQuestion implements Serializable{
    private String question;
    private String answer;
    private String[] options;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
