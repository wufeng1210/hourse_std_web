package com.hourse.web.mapper;

import com.hourse.web.model.UserAuth;
import com.hourse.web.model.UserAuth;
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
public interface UserAuthMapper {

    @Select("select * from user_auth where authId in #{authIds}")
    List<UserAuth> getAuthInfo(UserAuth userAuth);

    @SelectProvider(method = "getAuthByParentId", type = UserAuthProvider.class)
    public List<UserAuth> getAuthByParentId(@Param("parentId")String parentId, @Param("authIds") String authIds);

    @Select("select * from user_auth where authId = #{authId}")
    UserAuth query(int authId);

    @SelectProvider(method = "queryList", type = UserAuthProvider.class)
    List<UserAuth> queryList(UserAuth UserAuth);
    @SelectProvider(method = "count", type = UserAuthProvider.class)
    int count(UserAuth UserAuth);

    @InsertProvider(method = "save", type = UserAuthProvider.class)
    public int save(UserAuth UserAuth);

    @UpdateProvider(method = "update", type = UserAuthProvider.class)
    public int update(UserAuth UserAuth);

    @DeleteProvider(method= "delete", type= UserAuthProvider.class)
    public int delete(String authIds);
}
