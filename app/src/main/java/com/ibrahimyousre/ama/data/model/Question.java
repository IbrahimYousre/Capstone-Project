package com.ibrahimyousre.ama.data.model;

import android.support.annotation.NonNull;

public class Question extends Entity {

    // forign keys
    String topicId;

    // denormalized data
    String topicName;

    String body;

    public Question() {
    }

    public Question(@NonNull Topic topic, String body) {
        this.topicId = topic.uid;
        this.topicName = topic.name;
        this.body = body;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
