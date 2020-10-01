package com.fucct.tobispring.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fucct.tobispring.dao.UserDao;

public class UserService {

    private final UserLevelUpgradePolicy userLevelUpgradePolicy;
    private final UserDao userDao;
    private final DataSource dataSource;

    public UserService(final UserLevelUpgradePolicy userLevelUpgradePolicy, final UserDao userDao,
        final DataSource dataSource) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
        this.userDao = userDao;
        this.dataSource = dataSource;
    }

    public void upgradeLevels() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            final List<User> users = userDao.getAll();

            for (User user : users) {
                upgradeLevel(user);
            }
            c.commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    public void upgradeLevel(User user) throws SQLException {
        userLevelUpgradePolicy.upgradeLevel(user);
    }

    public void add(final User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
