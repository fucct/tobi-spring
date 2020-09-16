package com.fucct.tobispring.dao;

import static org.assertj.core.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fucct.tobispring.user.User;

@SpringJUnitConfig
@Import(DaoFactory.class)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = new User("dd", "김태헌", "no1");
        user2 = new User("kyle", "김시영", "no2");
        user3 = new User("bumblebee", "김범준", "no3");
    }

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {

        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        User getUser1 = userDao.get(user1.getId());
        User getUser2 = userDao.get(user2.getId());
        assertThat(getUser1).isEqualToComparingFieldByField(user1);
        assertThat(getUser2).isEqualToComparingFieldByField(user2);
    }

    @Test
    void getUserFailure() {
        assertThatThrownBy(() -> userDao.get("unknown ID"))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void getCount() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(user1);
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(user3);
        assertThat(userDao.getCount()).isEqualTo(3);
    }
}
