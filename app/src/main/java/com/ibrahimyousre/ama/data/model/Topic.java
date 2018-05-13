package com.ibrahimyousre.ama.data.model;

import com.google.firebase.database.Exclude;

public class Topic {

    @Exclude
    String uid;

    String name;
    Question lastAnsweredQuestion;

    public Topic() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Question getLastAnsweredQuestion() {
        return lastAnsweredQuestion;
    }

    public void setLastAnsweredQuestion(Question lastAnsweredQuestion) {
        this.lastAnsweredQuestion = lastAnsweredQuestion;
    }
}
