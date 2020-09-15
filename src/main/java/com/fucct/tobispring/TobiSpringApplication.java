package com.fucct.tobispring;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fucct.tobispring.dao.ConnectionMaker;
import com.fucct.tobispring.dao.CountingConnectionMaker;
import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.dao.NConnectionMaker;
import com.fucct.tobispring.dao.UserDao;
import com.fucct.tobispring.user.User;

public class TobiSpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao userDao1 = context.getBean("userDao", UserDao.class);
        final UserDao userDao2 = context.getBean("userDao", UserDao.class);
        final CountingConnectionMaker c = context.getBean("connectionMaker",
            CountingConnectionMaker.class);

        System.out.println(userDao1 == userDao2);
        System.out.println(c.getCount());
        User user = new User();
        user.setId("DD");
        user.setName("김태헌");
        user.setPassword("password");
        userDao1.add(user);

        System.out.println(user.getId() + "등록 성공");

        System.out.println(c.getCount());

        User user2 = userDao1.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
        System.out.println(c.getCount());
    }

}
