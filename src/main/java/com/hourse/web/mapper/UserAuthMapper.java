package com.hourse.web.mapper;

import com.hourse.web.model.UserAuth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
@Repository
public interface UserAuthMapper {

    @Select("select * from user_auth where authId in #{authIds}")
    List<UserAuth> getAuthInfo(UserAuth userAuth);

    @SelectProvider(method = "getAuthByParentId", type = UserAuthProvider.class)
    public List<UserAuth> getAuthByParentId(@Param("parentId")String parentId, @Param("authIds") String authIds);
}
