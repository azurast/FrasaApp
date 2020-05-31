package com.labill.frasaapp;

import android.widget.ImageView;

public class Comments {
    // author is id
    String idStory, idAuthor, comment;

    //Empty
    public Comments() {
    }

    // Full
    public Comments(String idAuthor, String idStory, String comment) {
        this.idAuthor = idAuthor;
        this.idStory = idStory;
        this.comment =comment;
    }

    public Comments(String idAuthor, String comment) {
        this.idAuthor = idAuthor;
        this.comment =comment;
    }

    public String getIdAuthor() {
        return idAuthor;
    }

    public String getIdStory() {
        return idStory;
    }

    public String getComment() {return comment;}

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setIdStory(String idStory) {
        this.idStory = idStory;
    }

    public void setIdAuthor(String idAuthor) {
        this.idAuthor = idAuthor;
    }
}
