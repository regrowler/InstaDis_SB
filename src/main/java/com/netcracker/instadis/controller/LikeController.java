package com.netcracker.instadis.controller;


import com.netcracker.instadis.utils.ApiPaths;
import com.netcracker.instadis.dao.LikeRepository;
import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.Post;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.model.UserPostLike;
import com.netcracker.instadis.requestBodies.UserPostLikeRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@ControllerAdvice
@RestController
@RequestMapping(ApiPaths.PROTECTED_PATH + ApiPaths.LIKES_PATH)
public class LikeController {

    private LikeRepository likeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public LikeController(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Integer like(HttpServletResponse response,
                        @RequestBody UserPostLikeRequestBody body)
    {
        Optional<UserPostLike> optionalLike = likeRepository.findByUserLoginAndPostId(body.getUsername(), body.getPostId());
        /*
        (0) -> Like++
        (1) -> Dislike++;
        (2) -> Like--;
        (3) -> Dislike--
        (4) -> Dislike--, Like++
        (5) -> Dislike++, Like--
         */
        if(!optionalLike.isPresent())
        {
            Post post = postRepository.findById(body.getPostId()).get();
            User user = userRepository.findByLogin(body.getUsername()).get();
            UserPostLike like = new UserPostLike(user,post);
            like.setLike(body.getIsLike());
            likeRepository.save(like);
            if(body.getIsLike()){
                return 0;
            }
            else{
                return 1;
            }
        }
        else {
            if (userRepository.findByLogin(body.getUsername()).isPresent()) {
                UserPostLike like = optionalLike.get();
                if (body.getIsLike() == like.isLike()) {
                    likeRepository.deleteById(like.getId());

                    if(body.getIsLike()){
                        return 2;
                    }
                    else{
                        return 3;
                    }

                } else {
                    like.setLike(body.getIsLike());
                    likeRepository.save(like);

                    if(body.getIsLike()){
                        return 4;
                    }
                    else{
                        return 5;
                    }
                }

            }
        }
        response.setStatus(404);
        return -1;
    }
}




















