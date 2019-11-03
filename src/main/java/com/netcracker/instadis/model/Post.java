package com.netcracker.instadis.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Post {

    @Id
    private long id;
    private String title;
    private String image;
    private String timestamp_creation;

    public Post(long id, String title, String image, String timestamp_creation) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.timestamp_creation = timestamp_creation;
    }

    public Post() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp_creation() {
        return timestamp_creation;
    }

    public void setTimestamp_creation(String timestamp_creation) {
        this.timestamp_creation = timestamp_creation;
    }
    
}
