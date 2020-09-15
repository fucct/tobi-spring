package com.fucct.tobispring.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountDao {

    private final ConnectionMaker connectionMaker;

    public AccountDao(final ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
