package com.netcracker.instadis.dao;

import com.netcracker.instadis.model.Post;
import com.netcracker.instadis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAll();
    void deleteById(Long id);
    Optional<User> findById(Integer id);
    Optional<User> findByName(String name);
}
