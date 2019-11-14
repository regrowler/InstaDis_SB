package com.netcracker.instadis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;



@Entity
public class Post {

    @Id
    private long id;
    private String title;
    private String text;
    private String image;
    private Timestamp timestampCreation;

    public Post(long id, String title, String text, String image, Timestamp timestampCreation) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image = image;
        this.timestampCreation = timestampCreation;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getTimestampCreation() {
        return timestampCreation;
    }

    public void setTimestampCreation(Timestamp timestampCreation) {
        this.timestampCreation = timestampCreation;
    }
    
}
