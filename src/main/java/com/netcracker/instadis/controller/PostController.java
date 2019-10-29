package com.netcracker.instadis.controller;


import com.netcracker.instadis.dao.repos.PostRepositoryImpl;
import com.netcracker.instadis.model.Post;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@ControllerAdvice
@RestController
@RequestMapping("posts")
public class PostController {

    private final PostRepositoryImpl postRepositoryImpl;

    @Autowired
    public PostController(PostRepositoryImpl postRepositoryImpl){
        this.postRepositoryImpl = postRepositoryImpl;
    }

    @GetMapping
    public List<Post> list(){
        return postRepositoryImpl.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    public Post getOne(@PathVariable Integer id) {
        Post post = postRepositoryImpl.findOne(id);
        return post;
    }

    @PostMapping
    public String createPost(
            @RequestParam String title,
            @RequestParam("file") MultipartFile file
    ) {
        Post post = new Post();
        post.setTitle(title);
        if (!file.isEmpty()) {
            try {
            String encodedImage = encodeFileToBase64Binary((File) file);
            post.setImage(encodedImage);
            } catch (Exception e) {
                return "You were unable to upload the file: " + " => " + e.getMessage();
            }
        } else {
            return "You were unable to load: file is empty.";
        }
        postRepositoryImpl.createPost(post);
        return "Success";
    }

    @DeleteMapping
    public String deletePost(@RequestParam Integer id){
        postRepositoryImpl.deletePost(id);
        return "success";
    }

    @PutMapping
    public String updatePost(@RequestParam Integer id, @RequestParam String title, @RequestParam("file") MultipartFile file){
        Post post = new Post();
        int resultPost = 0;
        post.setTitle(title);
        post.setId(id);
        if (!file.isEmpty()) {
            try {
                String encodedImage = encodeFileToBase64Binary((File) file);
                post.setImage(encodedImage);
            } catch (Exception e) {
                return "You were unable to upload the file: " + " => " + e.getMessage();
            }
        } else {
            return "You were unable to load: file is empty.";
        }

        resultPost = postRepositoryImpl.updatePost(post);
        return "success";
    }

    private static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedfile;
    }

}
