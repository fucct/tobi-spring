package com.fucct.tobispring.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.soap.MessageFactory;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.fucct.tobispring.user.DefaultUserLevelUpgradePolicy;
import com.fucct.tobispring.user.MessageFactoryBean;
import com.fucct.tobispring.user.NameMatchClassMethodPointcut;
import com.fucct.tobispring.user.TransactionAdvice;
import com.fucct.tobispring.user.TxProxyFactoryBean;
import com.fucct.tobispring.user.UserLevelUpgradePolicy;
import com.fucct.tobispring.user.UserService;
import com.fucct.tobispring.user.UserServiceImpl;
import com.fucct.tobispring.user.UserServiceTx;

@Configuration
@EnableTransactionManagement
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource(), sqlService());
    }

    @Bean
    public SqlService sqlService() {
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("userAdd",
            "insert into users(id, name, password, email, level, login, recommend) values(?,?,?,?,?,?,?)");
        sqlMap.put("userGet", "select * from users where id = ?");
        sqlMap.put("userGetAll", "select * from users order by id");
        sqlMap.put("userDeleteAll", "delete from users");
        sqlMap.put("userGetCount", "select count(*) from users");
        sqlMap.put("userUpdate", "update users set name = ?, password = ?, email = ?, level = ?, login = ?, recommend = ? where id = ?");

        return new SimpleSqlService(sqlMap);
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
    public UserService userService() {
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

    // @Bean
    // public TransactionAdvice transactionAdvice() {
    //     return new TransactionAdvice(transactionManager());
    // }

    // @Bean
    // public NameMatchMethodPointcut transactionPointcut() {
    //     final NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
    //     pointcut.setMappedClassName("*ServiceImpl");
    //     pointcut.setMappedName("upgrade*");
    //     return pointcut;
    // }

    // @Bean
    // public DefaultPointcutAdvisor transactionAdvisor() {
    //     return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
    // }
    //
    // @Bean
    // public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    //     return new DefaultAdvisorAutoProxyCreator();
    // }
    //
    //
    // @Bean
    // public AspectJExpressionPointcut transactionPointcut() {
    //     final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    //     pointcut.setExpression("bean(*Service)");
    //     return pointcut;
    // }
    //
    // @Bean
    // public TransactionInterceptor transactionAdvice() {
    //     final TransactionInterceptor advice = new TransactionInterceptor();
    //     final Properties transactionAttributes = new Properties();
    //     transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,readOnly,timeout_30");
    //     transactionAttributes.setProperty("upgrade*", "PROPAGATION_REQUIRES_NEW,ISOLATION_SERIALIZABLE");
    //     transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");
    //     advice.setTransactionAttributes(transactionAttributes);
    //     advice.setTransactionManager(transactionManager());
    //     return advice;
    // }

}
