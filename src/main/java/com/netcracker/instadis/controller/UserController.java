package com.netcracker.instadis.controller;

import com.netcracker.instadis.dao.repos.UserRepositoryImpl;
import com.netcracker.instadis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auth")
public class UserController {
    private final UserRepositoryImpl userRepository;

    @Autowired
    public UserController(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public User getOne(@PathVariable Integer id, @RequestParam String pass) {
        User user = userRepository.findOne(id);
        return user.getPassword().equals(pass) ? user : null;
    }

    @PostMapping("/sign_up")
    public String createUser(
            @RequestParam String name,
            @RequestParam String pass
    ) {
        User user = new User();
        user.setName(name);
        user.setPassword(pass);
        userRepository.createUser(user);
        return "success";
    }

    @PostMapping("/delete_user")
    public String deleteUser(@RequestParam Integer id) {
        userRepository.deleteUser(id);
        return "success";
    }

    @PostMapping("/update_user")
    public String updateUser(@RequestParam Integer id, @RequestParam String name, @RequestParam String pass) {
        User user = userRepository.findOne(id);
        if (user.getPassword().equals(pass)) {
            User user1 = new User();
            user1.setId(id);
            user1.setName(name);
            user1.setPassword(pass);
            userRepository.updateUser(user1);
            return "success";
        }
        return "fail";
    }


}
