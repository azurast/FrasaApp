package com.labill.frasaapp.database.constructorClass;

import android.widget.ImageView;

import java.io.Serializable;

public class User implements Serializable {
    private String name, email, bio, photo, follow, bookmark;

    public User(){}

    public User(String name, String email, String bio,
                String photo, String follow, String bookmark)
    {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.photo = photo;
        this.follow = follow;
        this.bookmark = bookmark;
    }

    public User(String name, String photo){
        this.name = name;
        this.photo = photo;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getBio()
    {
        return bio;
    }

    public String getPhoto()
    {
        return photo;
    }

    public String getFollow()
    {
        return follow;
    }

    public void setName()
    {
        this.name = name;
    }

    public void setEmail()
    {
        this.email = email;
    }

    public void setPhoto()
    {
        this.photo = photo;
    }

    public void setFollow()
    {
        this.follow = follow;
    }
}