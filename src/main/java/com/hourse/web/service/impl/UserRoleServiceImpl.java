package com.hourse.web.service.impl;

import com.hourse.web.mapper.UserAuthMapper;
import com.hourse.web.mapper.UserRoleMapper;
import com.hourse.web.model.User;
import com.hourse.web.model.UserAuth;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Service
@Transactional
public class UserRoleServiceImpl implements IUserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    public List<UserRole> getUserRoleById(UserRole userRole) {
        return  userRoleMapper.getRoleInfo(userRole);
    }

    public UserRole getUserRoleByRoleId(int roleId) {
        return  userRoleMapper.getUserRoleByRoleId(roleId);
    }
    public List<UserRole> queryList(UserRole userRole) {
        return  userRoleMapper.queryList(userRole);
    }
    public int count(UserRole userRole) {
        return  userRoleMapper.count(userRole);
    }

    public int save(UserRole userRole) {
        return  userRoleMapper.save(userRole);
    }

    public int update(UserRole userRole) {
        return  userRoleMapper.update(userRole);
    }

    public int delete(String roleIds) {
        return  userRoleMapper.delete(roleIds);
    }
}
