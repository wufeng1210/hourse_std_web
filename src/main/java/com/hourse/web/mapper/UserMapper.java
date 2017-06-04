package com.hourse.web.mapper;

import com.hourse.web.model.User;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Repository
public interface UserMapper {
    @Select("select * from user_info where userName = #{userName} and userPassWord = #{userPassWord}")
    List<User> getUserInfo(User user);

    @Select("select * from user_info where userId = #{userId}")
    User query(int userId);

    @SelectProvider(method = "queryList", type = UserProvider.class)
    List<User> queryList(User user);
    @SelectProvider(method = "count", type = UserProvider.class)
    int count(User user);

    @InsertProvider(method = "save", type = UserProvider.class)
    public int save(User user);

    @UpdateProvider(method = "update", type = UserProvider.class)
    public int update(User user);

    @DeleteProvider(method= "delete", type= UserProvider.class)
    public int delete(String userIds);
}
