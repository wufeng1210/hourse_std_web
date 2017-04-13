package com.hourse.web.mapper;

import com.hourse.web.model.Hourse;
import com.hourse.web.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dell on 2017/4/11.
 */
@Repository
public interface UserMapper {
    @Select("select hourseId from hourse_info where hourseId = #{hourseId}")
    List<Hourse> getUserInfo(Hourse hourse);

}
