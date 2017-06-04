package com.hourse.web.service;

import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IUserRoleService {
    public List<UserRole> getUserRoleById(UserRole userRole);
    public UserRole getUserRoleByRoleId(int roleId);

    UserRole query(int roleId);
    List<UserRole> queryList(UserRole userRole);
    int count(UserRole userRole);
    int save(UserRole userRole);
    int update(UserRole userRole);
    int delete(String roleIds);
}
