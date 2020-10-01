package com.fucct.tobispring.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.fucct.tobispring.user.DefaultUserLevelUpgradePolicy;
import com.fucct.tobispring.user.UserLevelUpgradePolicy;
import com.fucct.tobispring.user.UserService;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource());
    }

    @Bean
    public JdbcContext jdbcContext() {
        return new JdbcContext(dataSource());
    }

    public AccountDao accountDao() {
        ConnectionMaker connectionMaker = connectionMaker();

        return new AccountDao(connectionMaker);
    }

    public MessageDao messageDao() {
        ConnectionMaker connectionMaker = connectionMaker();

        return new MessageDao(connectionMaker);
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new NConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/tobi?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("C940429kk!!");

        return dataSource;
    }

    @Bean
    public UserService userService() {
        return new UserService(userLevelUpgradePolicy(), userDao(), dataSource());
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        return new DefaultUserLevelUpgradePolicy(userDao());
    }
}
