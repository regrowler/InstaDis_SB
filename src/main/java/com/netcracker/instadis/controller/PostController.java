
package com.netcracker.instadis.controller;

import com.netcracker.instadis.model.Post;
import com.netcracker.instadis.requestBodies.UpdatePostRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.netcracker.instadis.dao.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import com.netcracker.instadis.requestBodies.CreatePostRequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

@ControllerAdvice
@RestController
@RequestMapping("/posts")
public class PostController {
    private PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> list() {
        return postRepository.findAll( PageRequest.of(0,5,Direction.DESC,"timestampCreation")).getContent();
    }


    @GetMapping("{login}/page/{numpage}")
    public Page<Post> getPostByID(@PathVariable String login, @PathVariable Integer numpage) {
        numpage = numpage - 1;
        Pageable page = PageRequest.of(numpage,2,Direction.DESC,"timestampCreation");
        return postRepository.findAllByUserLogin(login, page);
    }

    @GetMapping("{login}/{id}")
    public Optional<Post> getUserPostById(@PathVariable String login,
                                          @PathVariable Long id){
        return postRepository.findByUserLoginAndId(login,id);
    }

    @PostMapping()
    public void createPost(HttpServletResponse response,
                           @RequestBody CreatePostRequestBody body
    ) throws IOException {
        if(body.getFile() != null){
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
        else{
            response.sendError(406,"No image"); // No Acceptable? or Forbidden(403)
        }
    }


    @DeleteMapping("{id}")
    public void deletePost(HttpServletResponse response,
                           @PathVariable Long id
    ) {
        response.setStatus(200);
        postRepository.deleteById(id);
    }


    @PutMapping
    public void updatePost(HttpServletResponse response,
                           @RequestBody UpdatePostRequestBody body) throws IOException {
        Optional<Post> postOptional = postRepository.findById(body.getId());
        if(!postOptional.isPresent()) {
            response.sendError(404,"Post was not found");
        }
        else
        {
            Post post = postOptional.get();
            post.setTitle(body.getTitle());
            post.setDescription(body.getDescription());
            postRepository.save(post);
            response.setStatus(200);
        }
    }

}