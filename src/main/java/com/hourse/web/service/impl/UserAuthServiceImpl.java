package com.hourse.web.service.impl;

import com.hourse.web.mapper.UserAuthMapper;
import com.hourse.web.mapper.UserMapper;
import com.hourse.web.model.User;
import com.hourse.web.model.UserAuth;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Service
@Transactional
public class UserAuthServiceImpl implements IUserAuthService {

    @Resource
    private UserAuthMapper userAuthMapper;

    public List<UserAuth> getUserAuthById(UserAuth userAuth) {
        return  userAuthMapper.getAuthInfo(userAuth);
    }

    public List<UserAuth> getAuthsByParentId(String authIds) {
        return  userAuthMapper.getAuthsByParentId(authIds);
    }
}
