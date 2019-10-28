package com.netcracker.instadis.dao.repos;

import com.netcracker.instadis.dao.UserRepository;
import com.netcracker.instadis.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<User> findAll() {
        String SQL = "SELECT * FROM USERS";
        return jdbcTemplate.query(SQL, ROW_MAPPER);
    }

    @Override
    public User findOne(Integer id) {
        User user = null;
        String SQL = "SELECT * FROM USERS WHERE id = ?";
        user = jdbcTemplate.queryForObject(SQL, new Object[]{id}, ROW_MAPPER);
        return user;
    }

    @Override
    public void createUser(User posuser) {
        String SQL = "INSERT INTO USERS (title, image) VALUES (?,?)";
        jdbcTemplate.update(SQL, posuser.getName(), posuser.getPassword());
    }

    @Override
    public void deleteUser(Integer id) {
        String SQL = "DELETE FROM USERS WHERE id = ?";
        jdbcTemplate.update(SQL, id);
    }

    @Override
    public void updateUser(User user) {
        String SQL = "UPDATE USERS SET title = ?, image = ? WHERE id = ?";
        jdbcTemplate.update(SQL, user.getName(), user.getPassword(), user.getId());
    }
}
