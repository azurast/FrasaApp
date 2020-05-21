package com.labill.frasaapp;

import android.widget.ImageView;

import java.util.List;

public class User {

    String name, bio, email;
    List<String> bookmark, follow, stories;
    ImageView photo;

    public User(String name, String bio, String email, List<String> bookmark, List<String> follow, ImageView photo, List<String> stories) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.bookmark = bookmark;
        this.follow = follow;
        this.photo = photo;
        this.stories = stories;
    }

    // Partial for testing

    public User(String name, String bio, List<String> stories) {
        this.name = name;
        this.bio = bio;
        this.stories = stories;
    }
}
