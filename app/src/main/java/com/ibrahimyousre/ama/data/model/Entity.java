package com.ibrahimyousre.ama.data.model;

import com.google.firebase.database.Exclude;

public abstract class Entity {

    @Exclude
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
