package com.hourse.web.service;

import com.hourse.web.model.User;
import com.hourse.web.model.UserAuth;
import com.hourse.web.model.UserRole;

import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IUserAuthService {
    public List<UserAuth> getUserAuthById(UserAuth userAuth);
    public List<Map<String,Object>> getAuthsByParentId(String parentId, String authIds);

    UserAuth query(int authId);
    List<UserAuth> queryList(UserAuth UserAuth);
    int count(UserAuth UserAuth);
    int save(UserAuth UserAuth);
    int update(UserAuth UserAuth);
    int delete(String authIds);
}
