package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.Post;
import com.netcracker.instadis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAll();
    void deleteById(Long id);
    void deleteByLogin(String login);
    Optional<User> findById(Integer id);
    Optional<User> findByLogin(String login);
    User save(User user);
}
