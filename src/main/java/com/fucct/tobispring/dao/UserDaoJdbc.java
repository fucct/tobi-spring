package com.fucct.tobispring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fucct.tobispring.user.Level;
import com.fucct.tobispring.user.User;

public class UserDaoJdbc implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SqlService sqlService;
    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));

        return user;
    });

    public UserDaoJdbc(DataSource dataSource, SqlService sqlService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sqlService = sqlService;
    }

    public void add(User user) {
        this.jdbcTemplate.update(
            sqlService.getSql("userAdd"), user.getId(),
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject(sqlService.getSql("userGet"),
            new Object[] {id},
            userRowMapper);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(sqlService.getSql("userGetAll"), userRowMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update(connection -> connection.prepareStatement(sqlService.getSql("userDeleteAll")));
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject(sqlService.getSql("userGetCount"), Integer.class);
    }

    @Override
    public void update(final User user) {
        this.jdbcTemplate.update(
            sqlService.getSql("userUpdate"),
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
            user.getId());
    }
}
