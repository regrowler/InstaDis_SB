package com.netcracker.instadis.controller;


import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.model.Post;
import com.netcracker.instadis.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    public Optional<Post> getOne(@PathVariable Integer id) {
        return postRepository.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public void createPost(HttpServletResponse response,
                           @RequestParam User user,
                           @RequestParam String title,
                           @RequestParam("file") MultipartFile file
    ) {
        Post post = new Post();
        post.setTitle(title);
        post.setUser(user);
        handlePost(response,post,file);
    }

    @DeleteMapping
    public void deletePost(HttpServletResponse response,
                           @RequestParam Long id
    ) {
        postRepository.deleteById(id);
    }

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
            handlePost(response,post,file);
       }
    }

    private static String encodeFileToBase64Binary(File file){
        String encodedFile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedFile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedFile;
    }

    private void handlePost(HttpServletResponse response,
                            @RequestParam Post post,
                            @RequestParam MultipartFile file
    ) {
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

}
