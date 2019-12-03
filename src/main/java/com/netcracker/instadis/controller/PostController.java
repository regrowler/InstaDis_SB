
package com.netcracker.instadis.controller;

import com.netcracker.instadis.model.CustomUserDetails;
import com.netcracker.instadis.services.UserService;
import com.netcracker.instadis.utils.ApiPaths;
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
@RequestMapping(ApiPaths.PROTECTED_PATH + ApiPaths.POST_PATH)
public class PostController {
    private PostRepository postRepository;
    private UserService userService;

    @Autowired
    public PostController(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @GetMapping
    public List<Post> list() {
        return postRepository.findAll( PageRequest.of(0,5,Direction.DESC,"timestampCreation")).getContent();
    }


    @GetMapping("{login}/page/{numpage}")
    public Page<Post> getPostByID(@PathVariable String login, @PathVariable Integer numpage) {
        numpage = numpage - 1;
        Pageable page = PageRequest.of(numpage,5,Direction.DESC,"timestampCreation");
        return postRepository.findAllByUserLogin(login, page);
    }


    @GetMapping("{token}/{id}")
    public Optional<Post> getUserPostById(@PathVariable String token,
                                          @PathVariable Long id){
        return postRepository.findByUserTokenAndId(token,id);
    }


    @PostMapping()
    public void createPost(HttpServletResponse response,
                           @RequestBody CreatePostRequestBody body
    ) throws IOException {
        if(body.getFile() != null){
            Optional<CustomUserDetails> customUserDetails = userService.findByToken(body.getToken());
            if(customUserDetails.isPresent()) {
                Post post = new Post(body.getTitle(),body.getFile(),body.getDescription(),
                                     new Timestamp(System.currentTimeMillis()),customUserDetails.get().getUser());
                postRepository.save(post);
                response.setStatus(200);
            }
            else{
                response.sendError(404,"User was not found");
            }
        }
        else{
            response.sendError(406,"No image"); // No Acceptable? or Forbidden(403)
        }
    }


    @DeleteMapping("{token}/{id}")
    public void deletePost(HttpServletResponse response,
                           @PathVariable String token,
                           @PathVariable Long id
    ) throws IOException {
        Optional<Post> byUserTokenAndId = postRepository.findByUserTokenAndId(token, id);
        if(byUserTokenAndId.isPresent()) {
            response.setStatus(200);
            postRepository.deleteById(id);
        }
        else {
            response.sendError(404,"User doesnt have such post");
        }
    }


    @PutMapping
    public void updatePost(HttpServletResponse response,
                           @RequestBody UpdatePostRequestBody body) throws IOException {
        Optional<Post> postOptional = postRepository.findByUserTokenAndId(body.getToken(),body.getId());
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