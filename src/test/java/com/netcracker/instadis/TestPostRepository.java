package com.netcracker.instadis;

import com.netcracker.instadis.dao.PostRepository;
import com.netcracker.instadis.dao.repos.PostRepositoryImpl;
import com.netcracker.instadis.model.Post;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;


class TestPostRepository {

    private EmbeddedDatabase embeddedDatabase;

    private JdbcTemplate jdbcTemplate;

    private PostRepository postRepository;

    @Before
    public void setUp() {
        // Создадим базу данных для тестирования
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()// Добавляем скрипты schema.sql и data.sql
                .build();

        // Создадим JdbcTemplate
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        // Создадим PersonRepository
        postRepository = new PostRepositoryImpl(jdbcTemplate);
    }

    @Test
    public void testFindAll() {
        Assert.assertNotNull(postRepository.findAll());
    }

    @Test
    public void testFindOne() {
        Assert.assertNotNull(postRepository.findOne(777));
    }

    public void testCreatePost() {
        Post post = postRepository.createPost(new Post(100, "title","image","2019-08-17 05:37:45.000000"));

        Assert.assertNotNull(post);
        Assert.assertNotNull(post.getId());
        Assert.assertEquals("title", post.getTitle());
    }

    @Test
    public void testUpdatePost() {
        Post post = jdbcTemplate.queryForObject("SELECT * FROM POSTS WHERE ID = 777", PostRepositoryImpl.ROW_MAPPER);
        post.setTitle("Title");

        post = postRepository.createPost(post);
        Assert.assertNotNull(post);
        Assert.assertNotNull(post.getId());
        Assert.assertEquals("Title", post.getTitle());
    }

    @Test
    public void testDeletePost() {
        Assert.assertEquals(1, postRepository.deletePost(777));
        Assert.assertEquals(0, postRepository.deletePost(-1));
    }

    @After
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

}
