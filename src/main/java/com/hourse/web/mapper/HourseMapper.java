package com.hourse.web.mapper;

import com.hourse.web.model.Hourse;
import com.hourse.web.model.UserAuth;
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
public interface HourseMapper {
    @Select("select * from hourse_info where hourseId = #{hourseId}")
    Hourse query(int hourseId);
    @SelectProvider(method = "queryList", type = HourseProvider.class)
    List<Hourse> queryList(Hourse Hourse);
    @SelectProvider(method = "count", type = HourseProvider.class)
    int count(Hourse Hourse);

    @InsertProvider(method = "save", type = HourseProvider.class)
    public int save(Hourse Hourse);

    @UpdateProvider(method = "update", type = HourseProvider.class)
    public int update(Hourse Hourse);

    @DeleteProvider(method= "delete", type= HourseProvider.class)
    public int delete(String hourseIds);
}
