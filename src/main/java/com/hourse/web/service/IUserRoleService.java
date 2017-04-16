package com.hourse.web.service;

import com.hourse.web.model.UserRole;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IUserRoleService {
    public List<UserRole> getUserRoleById(UserRole userRole);
}
