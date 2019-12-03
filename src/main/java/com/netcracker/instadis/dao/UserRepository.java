package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAll();
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPassword(String login, String password);
    Optional<User> findByToken(String token);
    User save(User user);
}
