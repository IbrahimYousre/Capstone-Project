package com.ibrahimyousre.ama.data.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    @Exclude
    protected String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
