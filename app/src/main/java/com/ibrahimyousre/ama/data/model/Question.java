package com.ibrahimyousre.ama.data.model;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class Question {

    @Exclude
    String uid;

    String topic;
    String topicId;
    String body;
    Answer topAnswer;
    Map<String, Boolean> followers;

    public Question() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Answer getTopAnswer() {
        return topAnswer;
    }

    public void setTopAnswer(Answer topAnswer) {
        this.topAnswer = topAnswer;
    }

    public Map<String, Boolean> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.followers = followers;
    }
}
