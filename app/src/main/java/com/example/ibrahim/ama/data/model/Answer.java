package com.example.ibrahim.ama.data.model;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class Answer {

    @Exclude
    String uid;

    String question;
    String questionId;
    String user;
    String userId;
    String body;
    int upvotesCount;
    Map<String, Boolean> upvoters;

    public Answer() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUpvotesCount() {
        return upvotesCount;
    }

    public void setUpvotesCount(int upvotesCount) {
        this.upvotesCount = upvotesCount;
    }

    public Map<String, Boolean> getUpvoters() {
        return upvoters;
    }

    public void setUpvoters(Map<String, Boolean> upvoters) {
        this.upvoters = upvoters;
    }
}
