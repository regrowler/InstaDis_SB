package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.Post;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.util.List;

public interface PostRepository {

    public RowMapper<Post> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
            return new Post(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("image"));
    };

    public List<Post> findAll();

    public Post findOne(Integer id);

    public void createPost(Post post);

    public void deletePost(Integer id);

    public void updatePost(Post post);

}
