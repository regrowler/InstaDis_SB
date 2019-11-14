package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post,Long> {

    Page<Post> findAll(Pageable page);
    Optional<Post> findById(Integer id);
    Optional<Post> findByTitle(String login);
    void deleteById(Long id);
    Post save(Post post);
}

