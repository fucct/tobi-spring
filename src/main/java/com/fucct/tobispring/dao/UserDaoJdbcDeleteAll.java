package com.fucct.tobispring.dao;

import javax.sql.DataSource;

public class UserDaoJdbcDeleteAll extends UserDaoJdbc {
    public UserDaoJdbcDeleteAll(final DataSource dataSource) {
        super(dataSource);
    }
}
