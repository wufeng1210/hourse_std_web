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
@Repository(value = "userMapper")
public interface UserMapper {
    @Select(value = "${sql}")
    @Results(value = { @Result(id = true, property = "hourseId", column = "hourseId")})
    List operateReturnBeans(@Param(value = "sql") String sql);

}
