package com.fucct.tobispring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserDaoDeleteAll extends UserDao {
    public UserDaoDeleteAll(final DataSource dataSource) {
        super(dataSource);
    }
}
