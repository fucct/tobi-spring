package com.fucct.tobispring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy{

    @Override
    public PreparedStatement makeStatement(final Connection connection) throws SQLException {
        return connection.prepareStatement("delete from accounts");
    }
}
