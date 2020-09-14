package com.fucct.tobispring;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fucct.tobispring.dao.NUserDao;
import com.fucct.tobispring.dao.UserDao;
import com.fucct.tobispring.user.User;

public class TobiSpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new NUserDao();

        User user = new User();
        user.setId("DD");
        user.setName("김태헌");
        user.setPassword("password");
        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }

}
