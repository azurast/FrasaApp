package com.labill.frasaapp;

import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {

    String name, bio, email, id;
    List<Map> following;
    List<String> bookmark, stories;
    //CircleImageView photo;

    public User(){

    }

    public User(String name, String bio, String email, String id, List<String> bookmark, List<Map> following, List<String> stories) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.id = id;
        this.bookmark = bookmark;
        this.following = following;
        this.stories = stories;
    }

    // Partial for testing
    public User(String name, String id, List<Map> following) {
        this.name = name;
        this.id = id;
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public String getId(){
        return id;
    }

    public List<Map> getFollowing() {
        return following;
    }
}
