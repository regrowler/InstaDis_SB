package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<UserPostLike, Long> {
    List<UserPostLike> findAll();
    UserPostLike save(UserPostLike like);
    Optional<UserPostLike> findByUserLoginAndPostId(String login, Long id);
    void deleteById(Long id);
}
