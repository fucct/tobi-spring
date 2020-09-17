package com.fucct.tobispring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
    PreparedStatement makeStatement(final Connection connection) throws SQLException;
}
