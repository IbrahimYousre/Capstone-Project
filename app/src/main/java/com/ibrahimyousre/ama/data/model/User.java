package com.ibrahimyousre.ama.data.model;

public class User extends Entity {

    String name;
    String title;
    String photoUrl;

    public User() {
    }

    public User(User user) {
        this.uid = user.uid;
        this.name = user.name;
        this.title = user.title;
        this.photoUrl = user.photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
