package com.ibrahimyousre.ama.data.model;

import android.support.annotation.NonNull;

public class Answer extends Entity{

    // forign keys
    String topicId;
    String questionId;
    String userId;

    // denormalized data
    String topicName;
    String questionBody;
    String userName;
    String userTitle;
    String userPhotoUrl;

    String text;
    int upvotesCount;

    public Answer() {
    }

    public Answer(@NonNull Question question, @NonNull User user, String text) {
        this.topicId = question.topicId;
        this.questionId = question.uid;
        this.userId = user.uid;

        this.topicName = question.topicName;
        this.questionBody = question.body;
        this.userName = user.name;
        this.userTitle = user.title;
        this.userPhotoUrl = user.photoUrl;

        this.text = text;
        upvotesCount = 0;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUpvotesCount() {
        return upvotesCount;
    }

    public void setUpvotesCount(int upvotesCount) {
        this.upvotesCount = upvotesCount;
    }
}
