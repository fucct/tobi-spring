package com.fucct.tobispring.dao;

import javax.sql.DataSource;
import javax.xml.soap.MessageFactory;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.fucct.tobispring.user.DefaultUserLevelUpgradePolicy;
import com.fucct.tobispring.user.MessageFactoryBean;
import com.fucct.tobispring.user.TransactionAdvice;
import com.fucct.tobispring.user.TxProxyFactoryBean;
import com.fucct.tobispring.user.UserLevelUpgradePolicy;
import com.fucct.tobispring.user.UserService;
import com.fucct.tobispring.user.UserServiceImpl;
import com.fucct.tobispring.user.UserServiceTx;

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
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public ProxyFactoryBean userService() {
        final ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(userServiceImpl());
        pfBean.addAdvisor(transactionAdvisor());
        return pfBean;
    }

    @Bean
    public UserService userServiceImpl() {
        return new UserServiceImpl(userLevelUpgradePolicy(), userDao());
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        return new DefaultUserLevelUpgradePolicy(userDao());
    }

    @Bean
    public MessageFactoryBean message() {
        return new MessageFactoryBean("Factory Bean");
    }

    @Bean
    public TransactionAdvice transactionAdvice() {
        return new TransactionAdvice(transactionManager());
    }

    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        final NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut();
        nameMatchMethodPointcut.setMappedName("upgrade*");
        return nameMatchMethodPointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
    }

}
