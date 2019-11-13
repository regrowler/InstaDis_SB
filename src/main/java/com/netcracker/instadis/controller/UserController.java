package com.netcracker.instadis.controller;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.requestBodies.loginAndPasswordRequestBody;
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


    //todo: remove this query?
    @GetMapping("{login}")
    public Optional<User> getOne(HttpServletResponse response,
                                 @PathVariable String login,
                                 @RequestBody String password
    ) {
        if(isUserRegistered(response, login, password)){
            return userRepository.findByLogin(login);
        }
        return Optional.empty();
    }


    //Registration
    @PostMapping(value = "/sign-up")
    public void createUser(HttpServletResponse response,
                             @RequestBody loginAndPasswordRequestBody body) {
        if (!isUserRegistered(response, body.getLogin())) {
            User user = new User(body.getLogin(),body.getPassword());
            userRepository.save(user);
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
    }

    //Authorization
    @PostMapping(value = "/sign-in")
    public Optional<User> authUser(HttpServletResponse response,
                           @RequestBody loginAndPasswordRequestBody body) {
        if (isUserRegistered(response, body.getLogin(), body.getPassword())) {
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
        return userRepository.findByLogin(body.getLogin());
    }

    @DeleteMapping()
    public void deleteUser(HttpServletResponse response,
                           @RequestBody loginAndPasswordRequestBody body) {
        if (isUserRegistered(response, body.getLogin(), body.getPassword())) {
            userRepository.deleteByLogin(body.getLogin());
        }
    }

    @PatchMapping("{id}")
    public void updateUser(HttpServletResponse response,
                           @PathVariable Long id,
                           @RequestParam String login,
                           @RequestParam String oldPassword,
                           @RequestParam String password) {
        if (isUserRegistered(response, id, oldPassword)) {
            User user = userRepository.findById(id).get();
            user.setId(id);
            user.setLogin(login);
            user.setPassword(password);
            user.updateVersion();
            userRepository.save(user);
            response.setStatus(200);
        }
    }

    private boolean isUserRegistered(HttpServletResponse response, String login) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
        return user.isPresent();
    }

    private boolean isUserRegistered(HttpServletResponse response, Long id, String password) {
        Optional<User> user = userRepository.findById(id);
        return isUserRegistered(response, user,password);
    }

    private boolean isUserRegistered(HttpServletResponse response, String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);
        return isUserRegistered(response,user,password);
    }

    private boolean isUserRegistered(HttpServletResponse response, Optional<User> user, String pass){
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
