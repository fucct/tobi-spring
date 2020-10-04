package com.fucct.tobispring.user;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fucct.tobispring.dao.UserDao;

@Transactional
public class UserServiceImpl implements UserService{

    private final UserLevelUpgradePolicy userLevelUpgradePolicy;
    private final UserDao userDao;

    public UserServiceImpl(final UserLevelUpgradePolicy userLevelUpgradePolicy, final UserDao userDao) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
        this.userDao = userDao;
    }

    public User get(String id) {
        return userDao.get(id);
    }

    public void upgradeLevels() {
        final List<User> users = userDao.getAll();

        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private boolean canUpgradeLevel(final User user) {
        return userLevelUpgradePolicy.canUpgradeLevel(user);
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

    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    protected void update(final User user) {
        userDao.update(user);
    }
}
