package com.netcracker.instadis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long id;

    @Column(unique = true)
    private String login;
    private String password;
    @Version
    @Column(name = "VERSION")
    private long version;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Set<Post> posts;

    public User() {
    }
    public long updateVersion(){
        return ++version;
    }
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Post> getPosts() { return Collections.unmodifiableSet(posts); }

    public void setPosts(Set<Post> posts) { this.posts = posts; }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setUser(this);
    }

    public void removePost(Post post){
        if(this.posts.contains(post)){
            this.posts.remove(post);
            post.setUser(null);
        }
    }
}
