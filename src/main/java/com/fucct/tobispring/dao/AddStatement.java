package com.fucct.tobispring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fucct.tobispring.user.User;

public class AddStatement implements StatementStrategy{
    private final User user;

    public AddStatement(final User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makeStatement(final Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "insert into accounts(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        return ps;
    }
}
