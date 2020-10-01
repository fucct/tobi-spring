package com.fucct.tobispring.user;

import static com.fucct.tobispring.user.DefaultUserLevelUpgradePolicy.*;
import static com.fucct.tobispring.user.UserService.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.dao.UserDao;

@SpringJUnitConfig
@Import({UserService.class, DaoFactory.class})
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    private DataSource dataSource;

    List<User> users;



    static class TestUserService extends UserService {
        private String id;

        public TestUserService(final UserLevelUpgradePolicy userLevelUpgradePolicy,
            final UserDao userDao, final DataSource dataSource, final String id) {
            super(userLevelUpgradePolicy, userDao, dataSource);
            this.id = id;
        }

        public void upgradeLevel(User user) throws SQLException {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
        }
    }


    @BeforeEach
    void setUp() {
        users = Arrays.asList(new User("TT", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
            new User("DD", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("LULU", "박범진", "p1", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER, MIN_RECCOMEND_FOR_GOLD-1),
            new User("LALA", "박범진", "p1", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER, MIN_RECCOMEND_FOR_GOLD),
            new User("TOTO", "박범진", "p1", Level.GOLD, MIN_LOGCOUNT_FOR_SILVER, MIN_RECCOMEND_FOR_GOLD));
    }

    @Test
    void bean() {
        assertThat(this.userService).isNotNull();
    }

    @Test
    void upgradeLevels() throws SQLException {
        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        assertAll(
            () -> checkLevelUpgraded(users.get(0), false),
            () -> checkLevelUpgraded(users.get(1), true),
            () -> checkLevelUpgraded(users.get(2), false),
            () -> checkLevelUpgraded(users.get(3), true),
            () -> checkLevelUpgraded(users.get(4), false)
        );
    }

    @Test
    void add() {
        userDao.deleteAll();

        final User userWithLevel = users.get(4);
        final User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        final User userWithLevelRead = userDao.get(userWithLevel.getId());
        final User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        checkLevelUpgraded(userWithLevelRead, false);
        checkLevelUpgraded(userWithoutLevelRead, false);
    }

    void checkLevelUpgraded(User user, boolean expected) {
        final User userUpdate = userDao.get(user.getId());
        if (expected) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else{
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    @Test
    void upgradeOrNothing() {
        TestUserService testUserService = new TestUserService(userLevelUpgradePolicy, userDao, dataSource, users.get(3).getId());
        userDao.deleteAll();
        users.forEach(userDao::add);

        try {
            testUserService.upgradeLevels();
        } catch (TestUserServiceException | SQLException e) {
        }
        checkLevelUpgraded(users.get(1), false);
    }
}
