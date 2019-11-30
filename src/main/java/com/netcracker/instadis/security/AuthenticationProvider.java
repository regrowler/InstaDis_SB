package com.netcracker.instadis.security;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.CustomUserDetails;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationProvider  extends AbstractUserDetailsAuthenticationProvider {

    private UserService userService;

    @Autowired
    public AuthenticationProvider(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        Object token = usernamePasswordAuthenticationToken.getCredentials();
        return Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(userService::findByToken)
                .orElseThrow(() -> new UsernameNotFoundException("Can not find user with authentication token = " + token));
    }
}
