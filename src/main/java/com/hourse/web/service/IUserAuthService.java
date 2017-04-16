package com.hourse.web.service;

import com.hourse.web.model.UserAuth;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IUserAuthService {
    public List<UserAuth> getUserAuthById(UserAuth userAuth);
}
