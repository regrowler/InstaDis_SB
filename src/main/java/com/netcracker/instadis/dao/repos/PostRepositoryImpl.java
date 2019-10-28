package com.netcracker.instadis.dao.repos;

import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {


    private final JdbcTemplate jdbcTemplate;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Post> findAll() {
        String SQL = "SELECT * FROM POSTS";
        return jdbcTemplate.query(SQL, ROW_MAPPER);
    }

    @Override
    public Post findOne(Integer id) {
        Post post = null;
        String SQL = "SELECT * FROM POSTS WHERE id = ?";
        post = jdbcTemplate.queryForObject(SQL, new Object[]{id}, ROW_MAPPER);
        return post;
    }

    @Override
    public void createPost(Post post) {
        String SQL = "INSERT INTO POSTS (title, image) VALUES (?,?)";
        jdbcTemplate.update(SQL, post.getTitle(), post.getImage());
    }

    @Override
    public void deletePost(Integer id) {
        String SQL = "DELETE FROM POSTS WHERE id = ?";
        jdbcTemplate.update(SQL, id);
    }

    @Override
    public void updatePost(Post post) {
        String SQL = "UPDATE POSTS SET title = ?, image = ? WHERE id = ?";
        jdbcTemplate.update(SQL, post.getTitle(), post.getImage(), post.getId());
    }
}
