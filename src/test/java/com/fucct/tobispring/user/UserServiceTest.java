package com.fucct.tobispring.user;

import static com.fucct.tobispring.user.DefaultUserLevelUpgradePolicy.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.dao.UserDao;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig
@Import({DaoFactory.class, TestConfig.class})
class UserServiceTest {

    @Autowired ApplicationContext context;

    @Autowired
    UserService userService;

    @Autowired
    @Qualifier("testUserService")
    UserService testUserSerivce;

    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    private PlatformTransactionManager transactionManager;

    List<User> users;

    static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "LALA";

        public TestUserServiceImpl(final UserLevelUpgradePolicy userLevelUpgradePolicy, final UserDao userDao) {
            super(userLevelUpgradePolicy, userDao);
        }

        @Override
        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }
            return null;
        }

        public void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    @BeforeEach
    void setUp() {
        users = Arrays.asList(new User("TT", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            new User("DD", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("LULU", "박범진", "p1", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER, MIN_RECCOMEND_FOR_GOLD - 1),
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
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    @Test
    void upgradeOrNothing() throws Exception {

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserSerivce.upgradeLevels();
        } catch (Exception e) {
            System.out.println("Zzz");
        }
        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    void advisorAutoProxyCreator() {
        assertThat(testUserSerivce).isInstanceOf(Proxy.class);
    }

    @Test
    void readOnlyTransaction() {
        assertThatThrownBy(() -> testUserSerivce.getAll())
            .isInstanceOf(TransientDataAccessResourceException.class);
    }
}
