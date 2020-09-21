package com.fucct.tobispring.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fucct.tobispring.user.User;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private RowMapper<User> userRowMapper;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        this.jdbcTemplate.update("insert into accounts(id, name, password) values(?,?,?)", user.getId(), user.getName(),
            user.getPassword());
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        return this.jdbcTemplate.queryForObject("select * from accounts where accounts.id = ?",
            new Object[] {id},
            userRowMapper);
     }

    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update(connection -> connection.prepareStatement("delete from accounts"));
    }

    public Integer getCount() throws SQLException {
        return this.jdbcTemplate.queryForObject("select count(*) from accounts", Integer.class);
    }
}
