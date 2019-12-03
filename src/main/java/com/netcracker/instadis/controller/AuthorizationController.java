package com.netcracker.instadis.controller;


import com.netcracker.instadis.utils.ApiPaths;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.requestBodies.AuthorizationRequestBody;
import com.netcracker.instadis.services.UserService;
import com.netcracker.instadis.utils.ResponseByCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping(ApiPaths.TOKEN_PATH)
public class AuthorizationController {

    private UserService userService;

    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = ApiPaths.SIGN_IN_PATH)
    public Optional<User> authorization(HttpServletResponse response,
                                        @RequestBody AuthorizationRequestBody body) throws NoSuchAlgorithmException, IOException {
        Optional<User> optionalUser = userService.authUser(body);
        if(optionalUser.isPresent()) {
            if (StringUtils.isEmpty(optionalUser.get().getToken())) {
                response.sendError(404, "User was not found");
            }
            else {
                return optionalUser;
            }
        }
        return Optional.empty();
    }

    @PostMapping(value = ApiPaths.SIGN_UP_PATH)
    public void registration(HttpServletResponse response,
                             @RequestBody AuthorizationRequestBody body) throws NoSuchAlgorithmException, IOException {
        ResponseByCondition.response(response,userService.createUser(body),404,"User was not found");
    }
}
