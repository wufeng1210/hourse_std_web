package com.hourse.web.mapper;

import com.hourse.web.model.ActivityInfo;
import com.hourse.web.model.Hourse;
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
public interface ActivityMapper {

    @Select("select * from activity_info where activityId = #{activityId}")
    ActivityInfo query(int ActivityId);

    @SelectProvider(method = "queryList", type = ActivityProvider.class)
    List<ActivityInfo> queryList(ActivityInfo ActivityInfo);
    @SelectProvider(method = "count", type = ActivityProvider.class)
    int count(ActivityInfo ActivityInfo);

    @InsertProvider(method = "save", type = ActivityProvider.class)
    public int save(ActivityInfo ActivityInfo);

    @UpdateProvider(method = "update", type = ActivityProvider.class)
    public int update(ActivityInfo ActivityInfo);

    @DeleteProvider(method= "delete", type= ActivityProvider.class)
    public int delete(String activityIds);
}
