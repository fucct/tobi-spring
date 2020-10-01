package com.fucct.tobispring.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fucct.tobispring.dao.UserDao;

public class UserService {

    private final UserLevelUpgradePolicy userLevelUpgradePolicy;
    private final UserDao userDao;
    private final PlatformTransactionManager transactionManager;

    public UserService(final UserLevelUpgradePolicy userLevelUpgradePolicy, final UserDao userDao,
        final PlatformTransactionManager transactionManager) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() throws SQLException {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            final List<User> users = userDao.getAll();

            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (Exception e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    private boolean canUpgradeLevel(final User user) {
        return userLevelUpgradePolicy.canUpgradeLevel(user);
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
