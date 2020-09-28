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

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        this.jdbcTemplate.update(
            "insert into accounts(id, name, password, level, login, recommend) values(?,?,?, ?, ?,?)", user.getId(),
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from accounts where accounts.id = ?",
            new Object[] {id},
            userRowMapper);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from accounts ", userRowMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update(connection -> connection.prepareStatement("delete from accounts"));
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from accounts", Integer.class);
    }

    @Override
    public void update(final User user) {
        this.jdbcTemplate.update(
            "update accounts set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ? ",
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
            user.getId());
    }
}
