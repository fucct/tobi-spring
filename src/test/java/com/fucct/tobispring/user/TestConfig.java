package com.fucct.tobispring.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.dao.UserDao;

@TestConfiguration
@SpringJUnitConfig
@Import(DaoFactory.class)
public class TestConfig {

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    UserDao userDao;

    @Bean
    @Qualifier("testUserService")
    public UserService testUserService() {
        return new UserServiceTest.TestUserServiceImpl(userLevelUpgradePolicy, userDao);
    }
}
