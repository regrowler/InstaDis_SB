package com.netcracker.instadis.controller;


import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.model.Post;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Post> list() {
        return postRepository.findAll();

    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    public Optional<Post> getOne(@PathVariable Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post;
    }

    @PostMapping
    public void createPost(HttpServletResponse response,
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
                response.setStatus(500);
            }
        } else {
            response.setStatus(500);
        }
        postRepository.save(post);
        response.setStatus(200);
    }

    @DeleteMapping
    public void deletePost(HttpServletResponse response, @RequestParam Long id) {
        postRepository.deleteById(id);
        return;
    }

    @PutMapping
    public void updatePost(HttpServletResponse response, @RequestParam Integer id, @RequestParam String title, @RequestParam("file") MultipartFile file) {
        Post post = new Post();
        post.setTitle(title);
        post.setId(id);
        if (!file.isEmpty()) {
            try {
                String encodedImage = encodeFileToBase64Binary((File) file);
                post.setImage(encodedImage);
            } catch (Exception e) {
                response.setStatus(500);
            }
        } else {
            response.setStatus(500);
        }
        postRepository.save(post);
        response.setStatus(200);
        return;
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
