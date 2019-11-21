package com.netcracker.instadis.controller;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.requestBodies.AuthorizationRequestBody;
import com.netcracker.instadis.requestBodies.SubscriptionBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //for tests only
    @GetMapping
    public List<User> list(HttpServletResponse response) {
        response.setStatus(200);
        return userRepository.findAll();
    }

    //Registration
    @PostMapping(value = "/sign-up")
    public void createUser(HttpServletResponse response,
                             @RequestBody AuthorizationRequestBody body) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByLogin(body.getLogin());
        if (!optionalUser.isPresent()) {
            User user = new User(body.getLogin(),hashMDA5(body.getPassword()));
            userRepository.save(user);
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
    }

    @PostMapping(value = "/sign-in")
    public Optional<User> authUser(HttpServletResponse response,
                           @RequestBody AuthorizationRequestBody body) throws  NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByLoginAndPassword(body.getLogin(),hashMDA5(body.getPassword()));
        if (optionalUser.isPresent()) {
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
        return optionalUser;
    }


    @PostMapping(value = "/subscribe")
    public void subscribeToUser(HttpServletResponse response,
                                @RequestBody SubscriptionBody body) {
        Optional<User> optionalSubscribe = isUserRegistered(body.getSubscribe());
        Optional<User> optionalUser = isUserRegistered(body.getUsername());
        if (optionalSubscribe.isPresent() && optionalUser.isPresent()) {
            User subscribe = optionalSubscribe.get();
            User user = optionalUser.get();
            user.getSubscriptions().add(subscribe);
            userRepository.save(user);
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
    }

    @PostMapping(value = "/subscribe/is")
    public boolean isSubscribed(HttpServletResponse response,
                                @RequestBody SubscriptionBody body){
        Optional<User> optionalSubscribe = isUserRegistered(body.getSubscribe());
        Optional<User> optionalUser = isUserRegistered(body.getUsername());
        if(optionalSubscribe.isPresent() && optionalUser.isPresent()) {
            User subscribe = optionalSubscribe.get();
            User user = optionalUser.get();
            return user.getSubscriptions().contains(subscribe);
        }
        else{
            response.setStatus(404);
            return false;
        }
    }

    @GetMapping(value = "/subscribe/{username}")
    public Optional<Set<User>> getSubscribers(HttpServletResponse response,
                               @PathVariable String username){
        Optional<User> optionalUser = isUserRegistered(username);
        if (optionalUser.isPresent()){
            response.setStatus(200);
            return optionalUser.map(User::getSubscriptions);
        }
        else {
            response.setStatus(404);
            return Optional.empty();
        }
    }




    @DeleteMapping()
    public void deleteUser(HttpServletResponse response,
                           @RequestBody AuthorizationRequestBody body) throws  NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByLoginAndPassword(body.getLogin(),hashMDA5(body.getPassword()));
        if (optionalUser.isPresent()) {
            response.setStatus(200);
            userRepository.deleteByLogin(body.getLogin());
        }
        else{
            response.setStatus(404);
        }
    }

    @PatchMapping("{id}")
    public void updateUser(HttpServletResponse response,
                           @PathVariable Long id,
                           @RequestParam String login,
                           @RequestParam String oldPassword,
                           @RequestParam String password) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByLoginAndPassword(login,hashMDA5(oldPassword));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLogin(login);
            user.setPassword(password);
            userRepository.save(user);
            response.setStatus(200);
        }
    }

    private Optional<User> isUserRegistered(String login){
        return userRepository.findByLogin(login);
    }


    private String hashMDA5(String input) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
    }

}
