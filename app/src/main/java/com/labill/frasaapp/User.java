package com.labill.frasaapp;

import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {

    String name, bio, email, id;
    Map<String, Object> following, total;
    List<String> bookmark, stories;
    //CircleImageView photo;

    public User() {

    }

    public User(String name, String bio, String email, String id, List<String> bookmark, Map<String, Object> following, List<String> stories, Map<String, Object> total) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.id = id;
        this.bookmark = bookmark;
        this.following = following;
        this.stories = stories;
        this.total = total;
    }

    // Partial for testing
    public User(String name, String id, Map<String, Object> following, Map<String, Object> total) {
        this.name = name;
        this.id = id;
        this.following = following;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getFollowing() {
        return following;
    }

    public void setTotal(Map<String, Object> total) {
        this.total = total;
    }

    public Object getTotalParam(String param) {
        return total.get(param);
    }


}
