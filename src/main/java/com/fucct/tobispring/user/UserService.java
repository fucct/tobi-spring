package com.fucct.tobispring.user;

import java.util.List;

import com.fucct.tobispring.dao.UserDao;

public class UserService {

    private final UserLevelUpgradePolicy userLevelUpgradePolicy;
    private final UserDao userDao;

    public UserService(final UserLevelUpgradePolicy userLevelUpgradePolicy, final UserDao userDao) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        final List<User> users = userDao.getAll();

        for (User user : users) {
            userLevelUpgradePolicy.upgradeLevel(user);
        }
    }

    public void upgradeLevel(User user) {
        userLevelUpgradePolicy.upgradeLevel(user);
    }

    public void add(final User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
