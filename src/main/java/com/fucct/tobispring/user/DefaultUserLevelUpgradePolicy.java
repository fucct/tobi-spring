package com.fucct.tobispring.user;

import java.util.List;

import com.fucct.tobispring.dao.UserDao;

public class DefaultUserLevelUpgradePolicy implements UserLevelUpgradePolicy{
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    private final UserDao userDao;

    public DefaultUserLevelUpgradePolicy(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean canUpgradeLevel(final User user) {
        Level level = user.getLevel();
        switch (level) {
            case BASIC:
                return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER:
                return user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + level);
        }
    }

    @Override
    public void upgradeLevel(final User user) {
        if (canUpgradeLevel(user)) {
            user.upgradeLevel();
            userDao.update(user);
        }
    }
}
