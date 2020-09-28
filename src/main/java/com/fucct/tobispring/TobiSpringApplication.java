package com.fucct.tobispring;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.dao.UserDaoJdbc;
import com.fucct.tobispring.user.User;

public class TobiSpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDaoJdbc userDaoJdbc1 = context.getBean("userDao", UserDaoJdbc.class);
        final UserDaoJdbc userDaoJdbc2 = context.getBean("userDao", UserDaoJdbc.class);

        User user = new User();
        user.setId("DD");
        user.setName("김태헌");
        user.setPassword("password");
        userDaoJdbc1.add(user);


        // User user2 = userDaoJdbc1.get(user.getId());
    }

}
