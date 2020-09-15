package com.fucct.tobispring;

import java.sql.SQLException;

import com.fucct.tobispring.dao.ConnectionMaker;
import com.fucct.tobispring.dao.DaoFactory;
import com.fucct.tobispring.dao.NConnectionMaker;
import com.fucct.tobispring.dao.UserDao;
import com.fucct.tobispring.user.User;

public class TobiSpringApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new NConnectionMaker();
        UserDao dao = new DaoFactory().userDao();

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
