package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.Post;
import com.netcracker.instadis.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public interface UserRepository {
    public RowMapper<User> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new User(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("password"));
    };

    public List<User> findAll();

    public User findOne(Integer id);

    public void createUser(User post);

    public void deleteUser(Integer id);

    public void updateUser(User post);
}
