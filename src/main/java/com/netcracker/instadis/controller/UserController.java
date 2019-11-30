package com.netcracker.instadis.controller;

import com.netcracker.instadis.ApiPaths;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.requestBodies.SubscriptionBody;
import com.netcracker.instadis.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(ApiPaths.PROTECTED_PATH + ApiPaths.USER_PATH)
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @PostMapping(value = ApiPaths.SUBSCRIBE_PATH)
    public void subscribeToUser(HttpServletResponse response,
                                @RequestBody SubscriptionBody body) throws IOException {
        if(userService.subscribeToUser(body)){
            response.setStatus(200);
        }
        else
        {
            response.sendError(404,"User was not found");
        }
    }

    @PostMapping(value = ApiPaths.IS_SUBSCRIBED_PATH)
    public boolean isSubscribed(@RequestBody SubscriptionBody body) {
        return userService.isSubscribed(body);
    }

    @GetMapping(value = ApiPaths.GET_SUBSCRIBERS_PATH + "{username}")
    public Optional<Set<User>> getSubscribers(HttpServletResponse response,
                               @PathVariable String username) throws IOException {
        Optional<Set<User>> subscribers = userService.getSubscribers(username);
        if(subscribers.isPresent()){
            response.setStatus(200);
        }
        else{
            response.sendError(404,"User was not found");
        }
        return subscribers;
    }

}
