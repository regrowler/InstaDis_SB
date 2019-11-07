package com.netcracker.instadis.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String login;
    private String password;
    @Version
    @Column(name = "VERSION")
    private long version;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    public Set<Post> getPosts() { return posts; }

    public void setPosts(Set<Post> posts) { this.posts = posts; }

    public void addPost(Post post) { this.posts.add(post);}
}
