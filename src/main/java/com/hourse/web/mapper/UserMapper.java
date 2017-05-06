package com.hourse.web.mapper;

import com.hourse.web.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Repository
public interface UserMapper {
    @Select("select * from user_info where userName = #{userName} and userPassWord = #{userPassWord}")
    List<User> getUserInfo(User user);

    @Select("select * from user_info")
    List<User> find(User user);
    @Select("select count(1) from user_info")
    int count(User user);
}
