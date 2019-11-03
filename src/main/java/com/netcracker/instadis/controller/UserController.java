package com.netcracker.instadis.controller;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("auth")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //for tests only
    @GetMapping
    public List<User> list(HttpServletResponse response) {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public void getOne(HttpServletResponse response, @PathVariable Long id, @RequestHeader String pass) {
        isUserRegistered(response, id, pass);
    }

    @PostMapping()
    public void createUser(HttpServletResponse response,
                             @RequestHeader String login,
                             @RequestHeader String pass) {
        if (!isUserRegistered(response, login)) {
            User user = new User();
            user.setLogin(login);
            user.setPassword(pass);
            userRepository.save(user);
            response.setStatus(200);
        }else {
            response.setStatus(403);
        }
    }

    @DeleteMapping("{id}")
    public void deleteUser(HttpServletResponse response,
                           @PathVariable Long id,
                           @RequestHeader String pass) {
        if (isUserRegistered(response, id, pass)) {
            userRepository.deleteById(id);
        }
        return;
    }

    @PatchMapping("{id}")
    public void updateUser(HttpServletResponse response,
                           @PathVariable Long id,
                           @RequestHeader String login,
                           @RequestHeader String oldpass,
                           @RequestHeader String pass) {
        if (isUserRegistered(response, id, oldpass)) {
            User user = userRepository.findById(id).get();
            user.setId(id);
            user.setLogin(login);
            user.setPassword(pass);
            user.updateVersion();
            userRepository.save(user);
            response.setStatus(200);
            return;
        }
        return;
    }

    boolean isUserRegistered(HttpServletResponse response,
                             @RequestHeader String login) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
        return user.isPresent();
    }

    boolean isUserRegistered(HttpServletResponse response,
                             @RequestHeader Long id,
                             @RequestHeader String pass) {
        Optional<User> user = userRepository.findById(id);
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
