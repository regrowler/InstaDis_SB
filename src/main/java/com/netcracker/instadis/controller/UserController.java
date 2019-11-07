package com.netcracker.instadis.controller;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //for tests only
    @GetMapping
    public List<User> list(HttpServletResponse response) {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public Optional<User> getOne(HttpServletResponse response,
                                 @PathVariable Long id,
                                 @RequestParam String pass
    ) {
        if(isUserRegistered(response, id, pass)){
            return userRepository.findById(id);
        }
        return Optional.empty();
    }


    //Registration
    @RequestMapping(method = RequestMethod.POST, value = "/sign-up")
    public void createUser(HttpServletResponse response,
                             @RequestParam String login,
                             @RequestParam String pass) {
        if (!isUserRegistered(response, login)) {
            User user = new User(login,pass);
            userRepository.save(user);
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
    }

    //Authorization
    @RequestMapping(method = RequestMethod.POST, value = "/sign-in")
    public Optional<User> authUser(HttpServletResponse response,
                           @RequestParam String login,
                           @RequestParam String pass) {
        if (!isUserRegistered(response, login, pass)) {
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
        return userRepository.findByLogin(login);
    }

    @DeleteMapping("{id}")
    public void deleteUser(HttpServletResponse response,
                           @PathVariable Long id,
                           @RequestParam String pass) {
        if (isUserRegistered(response, id, pass)) {
            userRepository.deleteById(id);
        }
    }

    @PatchMapping("{id}")
    public void updateUser(HttpServletResponse response,
                           @PathVariable Long id,
                           @RequestParam String login,
                           @RequestParam String oldPass,
                           @RequestParam String pass) {
        if (isUserRegistered(response, id, oldPass)) {
            User user = userRepository.findById(id).get();
            user.setId(id);
            user.setLogin(login);
            user.setPassword(pass);
            user.updateVersion();
            userRepository.save(user);
            response.setStatus(200);
        }
    }

    boolean isUserRegistered(HttpServletResponse response,
                             @RequestParam String login) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
        return user.isPresent();
    }

    boolean isUserRegistered(HttpServletResponse response,
                             @RequestParam Long id,
                             @RequestParam String pass) {
        Optional<User> user = userRepository.findById(id);
        return isUserRegistered(response, user,pass);
    }

    boolean isUserRegistered(HttpServletResponse response,
                             @RequestParam String login,
                             @RequestParam String pass) {
        Optional<User> user = userRepository.findByLogin(login);
        return isUserRegistered(response,user,pass);
    }

    private boolean isUserRegistered(HttpServletResponse response,
                                     @RequestParam Optional<User> user,
                                     @RequestParam String pass){
        if (user.isPresent()) {
            if (user.get().getPassword().equals(pass)) {
                response.setStatus(200);
                return true;
            } else {
                response.setStatus(401);
                return false;
            }
        } else {
            response.setStatus(404);
            return false;
        }
    }
}
