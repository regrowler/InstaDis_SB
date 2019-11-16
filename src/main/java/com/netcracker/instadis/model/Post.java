package com.netcracker.instadis.model;
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
    private String timestamp_creation;
    private String description;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Post(long id, String title, String image, String timestamp_creation, User user) {
        this.user = user;
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

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
    
}
