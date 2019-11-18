package com.netcracker.instadis.model;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;

    @Length(max = 512000)
    private String image;
    private String description;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Post(long id, String title, String image, Timestamp timestampCreation, User user) {
        this.user = user;
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

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
    
}
