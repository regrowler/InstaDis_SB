package com.netcracker.instadis.controller;


import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.model.Post;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.requestBodies.createPostForUserRequestBody;
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
    public Optional<Post> getPostByID(@PathVariable Long id) {
        return postRepository.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createPost(HttpServletResponse response,
                           @RequestBody createPostForUserRequestBody body
    ) {
        Post post = new Post();
        post.setTitle(body.getTitle());
        post.setUser(body.getUser());
        post.setDescription(body.getDescription());
        post.setImage(body.getFile());
        post.setTimestamp_creation("");
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
                            Post post,
                            MultipartFile file
    ) {
        System.out.println(file.getName());
        if (!file.isEmpty()) {
            try {
                String encodedImage = encodeFileToBase64Binary((File) file);
                post.setImage(encodedImage);
                postRepository.save(post);
                response.setStatus(200);
            } catch (Exception e) {
                System.out.println("this is number 500 because of catch");
                response.setStatus(500);
            }
        } else {
            System.out.println("this is number 500 because of file");
            response.setStatus(500);
        }
    }
}
