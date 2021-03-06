package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAll();
    void deleteById(Long id);
    Optional<Post> findById(Integer id);
    void save(Post post);
}

