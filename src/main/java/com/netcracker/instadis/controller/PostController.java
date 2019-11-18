package com.netcracker.instadis.controller;


import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.model.Post;
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

@ControllerAdvice
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Post> list() {
        return postRepository.findAll();
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



    @DeleteMapping("{id}")
    public void deletePost(HttpServletResponse response,
                           @PathVariable Long id
    ) {
        postRepository.deleteById(id);
    }

    //todo: izmenit zapros
    @PutMapping
    public void updatePost(HttpServletResponse response,
                           @RequestParam Long id,
                           @RequestParam String title,
                           @RequestParam("file") MultipartFile file
    ) {
        Optional<Post> postById = postRepository.findById(id);
        if(postById.isPresent())
        {
            Post post =  postById.get();
            post.setTitle(title);
            //todo: gucci something
       }
    }
}
