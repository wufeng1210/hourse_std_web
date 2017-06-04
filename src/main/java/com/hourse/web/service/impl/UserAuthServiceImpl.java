package com.hourse.web.service.impl;

import com.hourse.web.mapper.UserAuthMapper;
import com.hourse.web.mapper.UserMapper;
import com.hourse.web.model.Hourse;
import com.hourse.web.model.User;
import com.hourse.web.model.UserAuth;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Map<String,Object>> getAuthsByParentId(String parentId, String authIds) {
        List<Map<String,Object>> list = this.getAuthByParentId(parentId, authIds);
        for(Map<String,Object> m : list){
            if("open".equals(m.get("state"))){
                continue;
            }else{
                m.put("children", getAuthsByParentId(m.get("id").toString(),authIds));
            }
        }
        return list;
    }

    public List<Map<String,Object>> getAuthByParentId( String parentId, String authIds){
        List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
        List<UserAuth> list = userAuthMapper.getAuthByParentId(parentId,authIds);
        for(UserAuth u : list){
            Map<String,Object> temp = new HashMap<String, Object>();
            temp.put("id", u.getAuthId());
            temp.put("text", u.getAuthName());
            temp.put("state", u.getState());
            temp.put("iconCls", "");
            Map<String,Object> attributeTemp = new HashMap<String, Object>();
            attributeTemp.put("authPath", u.getAuthPath());
            temp.put("attributes", attributeTemp);
            resList.add(temp);
        }
        return resList;
    }

    public UserAuth query(int authId) {
        return  userAuthMapper.query(authId);
    }
    public List<UserAuth> queryList(UserAuth userAuth) {
        return  userAuthMapper.queryList(userAuth);
    }
    public int count(UserAuth userAuth) {
        return  userAuthMapper.count(userAuth);
    }

    public int save(UserAuth userAuth) {
        return  userAuthMapper.save(userAuth);
    }

    public int update(UserAuth userAuth) {
        return  userAuthMapper.update(userAuth);
    }

    public int delete(String authIds) {
        return  userAuthMapper.delete(authIds);
    }
}
