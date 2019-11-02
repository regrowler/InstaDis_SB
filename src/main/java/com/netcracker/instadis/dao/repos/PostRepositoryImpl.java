package com.netcracker.instadis.dao.repos;

import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.model.Post;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {


    private final JdbcTemplate jdbcTemplate;

    private static String SQL;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Post> findAll() {
        SQL = "SELECT * FROM POSTS";
        return jdbcTemplate.query(SQL, ROW_MAPPER);
    }

    @Override
    public Post findOne(Integer id) {
        Post post = null;
        SQL = "SELECT * FROM POSTS WHERE id = ?";
        try {
            post = jdbcTemplate.queryForObject(SQL, new Object[]{id}, ROW_MAPPER);
            return post;
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Post createPost(Post post) {
        SQL = "INSERT INTO POSTS (title, image) VALUES (?,?)";
        jdbcTemplate.update(SQL, post.getTitle(), post.getImage());
        Integer idPost = Math.toIntExact(post.getId());
        return findOne(idPost);
    }

    @Override
    public int deletePost(Integer id) {
        SQL = "DELETE FROM POSTS WHERE id = ?";
        return jdbcTemplate.update(SQL, id);
    }

    @Override
    public int updatePost(Post post) {
        SQL = "UPDATE POSTS SET title = ?, image = ? WHERE id = ?";
        return jdbcTemplate.update(SQL, post.getTitle(), post.getImage(), post.getId());
    }
}
