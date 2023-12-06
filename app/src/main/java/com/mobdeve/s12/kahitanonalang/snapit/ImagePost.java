package com.mobdeve.s12.kahitanonalang.snapit;

import java.util.Date;
import com.google.firebase.firestore.ServerTimestamp;


public class ImagePost{
    private String title;
    private String image;

    private  @ServerTimestamp Date timestamp;
    private String description;

    public ImagePost(){

    }

    public ImagePost(String title, String image, Date timestamp, String description) {
        this.title = title;
        this.image = image;
        this.timestamp = timestamp;
        this.description = description;
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
}