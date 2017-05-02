package com.hourse.web.service;

import com.hourse.web.model.UserAuth;

import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IUserAuthService {
    public List<UserAuth> getUserAuthById(UserAuth userAuth);
    public List<Map<String,Object>> getAuthsByParentId(String parentId, String authIds);
}
