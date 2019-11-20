package com.netcracker.instadis.controller;


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
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RestController
@RequestMapping("/like")
public class LikeController {

        @Autowired
        LikeRepository likeRepository;

        @Autowired
        PostRepository postRepository;

        @Autowired
        UserRepository userRepository;

        @GetMapping
        public List<UserPostLike> all(HttpServletResponse response){
            return likeRepository.findAll();
        }

        @PostMapping
        public void like(HttpServletResponse response,
                         @RequestBody UserPostLikeRequestBody body){
            Optional<UserPostLike> optionalLike = likeRepository.findByUserLoginAndPostId(body.getUsername(), body.getPostId());
            if(!optionalLike.isPresent())
            {
                Post post = postRepository.findById(body.getPostId()).get();
                User user = userRepository.findByLogin(body.getUsername()).get();
                UserPostLike like = new UserPostLike(user,post);
                like.setLike(body.getIsLike());
                response.setStatus(200);
                likeRepository.save(like);
            }
            else {
                if (userRepository.findByLogin(body.getUsername()).isPresent()) {
                    UserPostLike like = optionalLike.get();
                    if (body.getIsLike() == like.isLike()) {
                        likeRepository.deleteById(like.getId());
                    } else {
                        like.setLike(body.getIsLike());
                        likeRepository.save(like);
                    }
                }
            }
    }
}




















