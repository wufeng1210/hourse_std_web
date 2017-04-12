package com.hourse.web.service.impl;

import com.hourse.web.mapper.UserMapper;
import com.hourse.web.model.User;
import com.hourse.web.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by dell on 2017/4/11.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public List getUserById() {

        return  userMapper.getUserInfo();
    }
}
