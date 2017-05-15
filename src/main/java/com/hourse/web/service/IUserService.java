package com.hourse.web.service;

import com.hourse.web.model.Hourse;
import com.hourse.web.model.User;

import java.util.List;

public interface IUserService {
    List<User> getUserById(User user);

    List<User> queryList(User user);
    int count(User user);
    int save(User user);
    int update(User user);
    int delete(String userIds);
}
