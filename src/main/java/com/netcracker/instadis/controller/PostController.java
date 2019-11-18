
package com.netcracker.instadis.controller;

import com.netcracker.instadis.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import com.netcracker.instadis.dao.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import com.netcracker.instadis.requestBodies.createPostForUserRequestBody;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

@ControllerAdvice
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public Page<Post> list() {
        Pageable page = new PageRequest(0, 5,Direction.ASC,"timestampCreation");
        return postRepository.findAll(page);
    }

    @GetMapping("hard_code")
    public void setPostsTest() {
        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        post1.setId(1);
        post1.setTitle("Title1");
        post1.setText("Text1");
        post1.setImage("Image1");
        post1.setTimestampCreation(timestamp);
        post2.setId(2);
        post2.setTitle("Title2");
        post2.setText("Text2");
        post2.setImage("Image2");
        post2.setTimestampCreation(timestamp);
        post3.setId(3);
        post3.setTitle("Title3");
        post3.setText("Text3");
        post3.setImage("Image3");
        post3.setTimestampCreation(timestamp);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @GetMapping("{id}")
    public Post getOne(HttpServletResponse response, @PathVariable Integer id) {
        Optional<Post> postOptional = postRepository.findById((long)id);
        if(postOptional == null) { 
            response.setStatus(404);
            return null;
        }else{
        Post post = postOptional.orElse(new Post());
        return post;
    }


    @GetMapping("{login}")
    public List<Post> getPostByID(@PathVariable String login) {
        return postRepository.findAllByUserLogin(login);
    }

    @GetMapping("{login}/{id}")
    public Optional<Post> getUserPostById(@PathVariable String login,
                                          @PathVariable Long id){
        return postRepository.findByUserLoginAndId(login,id);
    }

    @PostMapping()
    public void createPost(HttpServletResponse response,
                           @RequestBody createPostForUserRequestBody body
    ) {
        Post post = new Post();
        post.setTitle(body.getTitle());
        post.setUser(body.getUser());
        post.setDescription(body.getDescription());
        post.setImage(body.getFile());
        post.setTimestamp_creation(body.getDate());
        postRepository.save(post);
        response.setStatus(200);
    }



    @DeleteMapping
    public void deletePost(HttpServletResponse response,
                           @RequestParam Long id
    ) {
        postRepository.deleteById(id);
    }

    //todo: izmenit zapros
    @PutMapping
    public void updatePost(HttpServletResponse response, @RequestParam Integer id, @RequestParam String title, @RequestParam String fileBase64) {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional == null) { 
            response.setStatus(500);
        }else{
        Post post = postOptional.orElse(new Post());
        post.setTitle(title);
        post.setId(id);
        post.setImage(fileBase64);
        postRepository.save(post);
        response.setStatus(200);
        }
        return;
