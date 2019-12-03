package com.netcracker.instadis.services;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.User;
import com.netcracker.instadis.model.CustomUserDetails;
import com.netcracker.instadis.requestBodies.AuthorizationRequestBody;
import com.netcracker.instadis.requestBodies.SubscriptionBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("userService")
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<CustomUserDetails> findByToken(String token){
        Optional<User> optionalCustomUser = userRepository.findByToken(token);
        if(optionalCustomUser.isPresent()){
            User user = optionalCustomUser.get();
            CustomUserDetails details = new CustomUserDetails(user);
            return Optional.of(details);
        }
        return  Optional.empty();
    }

    public boolean createUser(AuthorizationRequestBody body) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByLogin(body.getLogin());
        if (!optionalUser.isPresent()) {
            User user = new User(body.getLogin(), md5(body.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Optional<User> authUser(AuthorizationRequestBody body) throws NoSuchAlgorithmException {
        Optional<User> optionalUser = userRepository.findByLoginAndPassword(body.getLogin(), md5(body.getPassword()));
        if (optionalUser.isPresent()) {
            String token = UUID.randomUUID().toString();
            User user = optionalUser.get();
            user.setToken(token);
            userRepository.save(user);
        }
        return optionalUser;
    }

    public boolean subscribeToUser(SubscriptionBody body){
        Optional<User> optionalSubscribe = isUserRegistered(body.getSubscribe());
        Optional<User> optionalUser = userRepository.findByToken(body.getToken());
        if (optionalSubscribe.isPresent() && optionalUser.isPresent()) {
            User subscribe = optionalSubscribe.get();
            User user = optionalUser.get();
            user.getSubscriptions().add(subscribe);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean isSubscribed(SubscriptionBody body) {
        Optional<User> optionalSubscribe = isUserRegistered(body.getSubscribe());
        Optional<User> optionalUser = userRepository.findByToken(body.getToken());
        if(optionalSubscribe.isPresent() && optionalUser.isPresent()) {
            User subscribe = optionalSubscribe.get();
            User user = optionalUser.get();
            return user.getSubscriptions().contains(subscribe);
        }
        return false;
    }

    public Optional<Set<User>> getSubscribers(String username) {
        Optional<User> optionalUser = isUserRegistered(username);
        if (optionalUser.isPresent()){
            return optionalUser.map(User::getSubscriptions);
        }
        return Optional.empty();
    }

    private Optional<User> isUserRegistered(String login){ return userRepository.findByLogin(login); }

    private String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
    }
}
