package com.labill.frasaapp;

import android.widget.ImageView;

public class Stories {

    // author is id
    String author, title, content, genre;
    boolean published;
    int layout, likes;
    ImageView photo;

    //Empty
    public Stories() {
    }

    // Full
    public Stories(String id, String author, String title, String content, String genre, String name, boolean published, int layout, int likes, ImageView photo) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.genre = genre;
        this.published = published;
        this.layout = layout;
        this.likes = likes;
        this.photo = photo;
    }

    // Partial for testing
    public Stories(String author, String title, String content, String name) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
