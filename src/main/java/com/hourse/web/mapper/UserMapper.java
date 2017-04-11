package com.hourse.web.mapper;

import com.hourse.web.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dell on 2017/4/11.
 */
@Repository(value = "menuMapper")
public interface UserMapper {
    @Select(value = "${sql}")
    @Results(value = { @Result(id = true, property = "id", column = "id"),
            @Result(property = "parentId", column = "c_parent_id"),
            @Result(property = "url", column = "c_url"),
            @Result(property = "isShowLeft", column = "c_is_show_left"),
            @Result(property = "name", column = "c_name") })
    List operateReturnBeans(@Param(value = "sql") String sql);

    User get();
}
