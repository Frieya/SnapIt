package com.mobdeve.s12.kahitanonalang.snapit;

import java.util.Date;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;


public class ImagePost{
    private @DocumentId String id;
    private String title;
    private String image;

    private  @ServerTimestamp Date timestamp;
    private String description;

    private String user;

    public ImagePost(){

    }

    public ImagePost(String id, String title, String image, Date timestamp, String description, String user) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.timestamp = timestamp;
        this.description = description;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }
}