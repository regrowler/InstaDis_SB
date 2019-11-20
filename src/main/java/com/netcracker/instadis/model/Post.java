package com.netcracker.instadis.model;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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

    private Timestamp timestampCreation;

    @ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<UserPostLike> likes = new HashSet<>();

    public Post(String title, String image, String description, Timestamp timestampCreation, User user) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.timestampCreation = timestampCreation;
        this.user = user;
    }

    public Post() {
    }

    public Set<UserPostLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<UserPostLike> likes) {
        this.likes = likes;
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
