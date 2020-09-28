package com.fucct.tobispring.dao;

import java.util.List;

import com.fucct.tobispring.user.User;

public interface UserDao {
    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user);
}
