package com.netcracker.instadis.components;

import com.netcracker.instadis.dao.UserRepository;
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

    private UserRepository userRepository;

    @Autowired
    public AuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        Object token = usernamePasswordAuthenticationToken.getCredentials();
        return (UserDetails) Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(userRepository::findByToken)
                .orElseThrow( () -> new UsernameNotFoundException("Can not find user with authentication token = " + token));
    }
}
