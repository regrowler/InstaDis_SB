
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
    public List<Post> list() {
        Pageable page = new PageRequest(0, 5,Direction.ASC,"timestampCreation");
        return postRepository.findAll(page).getContent();
    }


    @GetMapping("{login}/page/{numpage}")
    public Page<Post> getPostByID(@PathVariable String login, @PathVariable Integer numpage) {
        numpage = numpage - 1;
        Pageable page = new PageRequest(numpage, 2,Direction.DESC,"timestampCreation");
        return postRepository.findAllByUserLogin(login, page);
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        post.setTimestampCreation(timestamp);
        postRepository.save(post);
        response.setStatus(200);
    }



    @DeleteMapping("{id}")
    public void deletePost(HttpServletResponse response,
                           @PathVariable Long id
    ) {
        postRepository.deleteById(id);
    }

    //todo: izmenit zapros
    @PutMapping
    public void updatePost(HttpServletResponse response, @RequestParam Integer id, @RequestParam String title, @RequestParam String fileBase64) {
        Optional<Post> postOptional = postRepository.findById((long)id);
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
    }

}