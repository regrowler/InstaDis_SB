package com.netcracker.instadis.controller;


import com.netcracker.instadis.ApiPaths;
import com.netcracker.instadis.requestBodies.AuthorizationRequestBody;
import com.netcracker.instadis.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(ApiPaths.TOKEN_PATH)
public class AuthorizationController {

    private UserService userService;

    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = ApiPaths.SIGN_IN_PATH)
    public String getToken(HttpServletResponse response,
                           @RequestBody AuthorizationRequestBody body) throws NoSuchAlgorithmException, IOException {
        String token = userService.authUser(body);
        if(StringUtils.isEmpty(token)){
            response.sendError(404,"User was not found");
            return "User was not found";
        }
        return token;
    }

    @PostMapping(value = ApiPaths.SIGN_UP_PATH)
    public void createUser(HttpServletResponse response,
                           @RequestBody AuthorizationRequestBody body) throws NoSuchAlgorithmException, IOException {
        if(userService.createUser(body))
        {
            response.setStatus(200);
        }
        else
        {
            response.sendError(403,"Username is already taken");
        }
    }
}
