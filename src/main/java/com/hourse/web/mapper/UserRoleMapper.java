package com.hourse.web.mapper;

import com.hourse.web.model.UserRole;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
@Repository
public interface UserRoleMapper {

    @Select("select * from user_info where roleId = #{roleId}")
    List<UserRole> getRoleInfo(UserRole userRole);
}
