package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post,Long> {

    Page<Post> findAll(Pageable page);
    void deleteById(Long id);
    Optional<Post> findById(Long id);
    Post save(Post post);
    Page<Post> findAllByUserLogin(String login, Pageable page);
    Optional<Post> findByUserLoginAndId(String login, Long id);
}

