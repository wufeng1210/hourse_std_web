package com.hourse.web.mapper;

import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
@Repository
public interface UserRoleMapper {

    @Select("select * from user_info where roleId = #{roleId}")
    List<UserRole> getRoleInfo(UserRole userRole);

    @Select("select * from user_role where roleId = #{roleId}")
    UserRole getUserRoleByRoleId(@Param("roleId")int  roleId);

    @SelectProvider(method = "queryList", type = UserRoleProvider.class)
    List<UserRole> queryList(UserRole userRole);
    @SelectProvider(method = "count", type = UserRoleProvider.class)
    int count(UserRole userRole);

    @InsertProvider(method = "save", type = UserRoleProvider.class)
    public int save(UserRole userRole);

    @UpdateProvider(method = "update", type = UserRoleProvider.class)
    public int update(UserRole userRole);

    @DeleteProvider(method= "delete", type= UserRoleProvider.class)
    public int delete(String roleIds);
}
