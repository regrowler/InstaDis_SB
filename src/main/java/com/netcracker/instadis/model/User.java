package com.netcracker.instadis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long id;

    @Column(unique = true)
    private String login;
    @JsonIgnore
    private String password;
    @Version
    @Column(name = "VERSION")
    private long version;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Set<Post> posts;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> subscriptions = new HashSet<>();

    @ManyToMany(mappedBy = "subscriptions", cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> subscribers = new HashSet<>();


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

    public Set<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<User> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
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
}
