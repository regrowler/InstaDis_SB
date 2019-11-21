package com.netcracker.instadis.controller;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.requestBodies.AuthorizationRequestBody;
import com.netcracker.instadis.requestBodies.SubscriptionBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                             @RequestBody AuthorizationRequestBody body) throws IOException {
        if (!isUserRegistered(body.getLogin())) {
            User user = new User(body.getLogin(),body.getPassword());
            userRepository.save(user);
            response.setStatus(200);
        } else {
            response.sendError(403,"User is signed up already");
        }
    }

    //Authorization
    @PostMapping(value = "/sign-in")
    public Optional<User> authUser(HttpServletResponse response,
                           @RequestBody AuthorizationRequestBody body) throws IOException {
        if (isUserRegistered(body.getLogin(), body.getPassword())) {
            response.setStatus(200);
        } else {
            response.sendError(401,"Login or password are incorrect");
        }
        return userRepository.findByLogin(body.getLogin());
    }

    //Subscribing user
    @PostMapping(value = "/subscribe")
    public void subscribeToUser(HttpServletResponse response,
                                @RequestBody SubscriptionBody body) throws IOException {
        if (isUserRegistered(body.getUsername()) && isUserRegistered(body.getSubscribe())) {
            User subscribe = userRepository.findByLogin(body.getSubscribe()).get();
            User user = userRepository.findByLogin(body.getUsername()).get();
            user.getSubscriptions().add(subscribe);
            userRepository.save(user);
            response.setStatus(200);
        } else {
            response.sendError(500, "Unable to subscribe");
        }
    }

    @PostMapping(value = "/subscribe/is")
    public boolean isSubscribed(HttpServletResponse response,
                                @RequestBody SubscriptionBody body) throws IOException {
        if(isUserRegistered(body.getUsername()) && isUserRegistered(body.getSubscribe())) {
            User subscribe = userRepository.findByLogin(body.getSubscribe()).get();
            User user = userRepository.findByLogin(body.getUsername()).get();
            return user.getSubscriptions().contains(subscribe);
        }
        else{
            response.setStatus(500);
            return false;
        }
    }

    @GetMapping(value = "/subscribe/{username}")
    public Optional<Set<User>> getSubscribers(HttpServletResponse response,
                               @PathVariable String username) throws IOException {
        if (isUserRegistered(username)){
            response.setStatus(200);
            return userRepository.findByLogin(username).map(User::getSubscriptions);
        }
        else {
            response.sendError(500, "User was not found");
            return Optional.empty();
        }
    }




    @DeleteMapping()
    public void deleteUser(HttpServletResponse response,
                           @RequestBody AuthorizationRequestBody body) {
        if (isUserRegistered(body.getLogin(), body.getPassword())) {
            response.setStatus(200);
            userRepository.deleteByLogin(body.getLogin());
        }
    }

    @PatchMapping("{id}")
    public void updateUser(HttpServletResponse response,
                           @PathVariable Long id,
                           @RequestParam String login,
                           @RequestParam String oldPassword,
                           @RequestParam String password){
        if (isUserRegistered(id, oldPassword)) {
            User user = userRepository.findById(id).get();
            user.setId(id);
            user.setLogin(login);
            user.setPassword(password);
            user.updateVersion();
            userRepository.save(user);
            response.setStatus(200);
        }
    }

    private boolean isUserRegistered(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    private boolean isUserRegistered(Long id, String password){
        return isUserRegistered(userRepository.findById(id),password);
    }

    private boolean isUserRegistered(String login, String password){
        return isUserRegistered(userRepository.findByLogin(login),password);
    }

    private boolean isUserRegistered(Optional<User> user, String pass) {
        return user.map(value -> value.getPassword().equals(pass)).orElse(false);
    }
}
