package com.fucct.tobispring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import com.fucct.tobispring.user.User;

public class UserDao {
    private final DataSource dataSource;

    public UserDao(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
            "insert into accounts(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c
            .prepareStatement("select * from accounts where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (Objects.isNull(user)) {
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException {
        final Connection connection = dataSource.getConnection();

        final PreparedStatement ps = connection.prepareStatement("delete from accounts");
        ps.executeUpdate();

        ps.close();
        connection.close();
    }

    public int getCount() throws SQLException {
        final Connection connection = dataSource.getConnection();

        final PreparedStatement ps = connection.prepareStatement("select count(*) from accounts");

        final ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        connection.close();

        return count;
    }
}
