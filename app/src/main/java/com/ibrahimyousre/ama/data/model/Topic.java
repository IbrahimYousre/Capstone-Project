package com.ibrahimyousre.ama.data.model;

public class Topic extends Entity {

    String name;

    public Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }

    public Topic(Answer answer) {
        this.name = answer.topicName;
        this.uid = answer.topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
