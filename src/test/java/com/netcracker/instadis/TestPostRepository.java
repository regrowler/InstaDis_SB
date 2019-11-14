package com.netcracker.instadis;

import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.model.Post;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;


class TestPostRepository {

    private EmbeddedDatabase embeddedDatabase;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @Before
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()// Добавляем скрипты schema.sql и data.sql
                .build();

        jdbcTemplate = new JdbcTemplate(embeddedDatabase);

    }

    @Test
    public void testFindAll() {
        Assert.assertNotNull(postRepository.findAll());
    }

    @Test
    public void testFindById() {
        Assert.assertNotNull(postRepository.findById((long)777));
    }

    @After
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

}
