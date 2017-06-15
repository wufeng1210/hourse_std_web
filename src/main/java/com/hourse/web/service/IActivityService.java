package com.hourse.web.service;


import com.hourse.web.model.ActivityInfo;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IActivityService {

    ActivityInfo query(int ActivityId);
    List<ActivityInfo> queryList(ActivityInfo activityInfo);
    int count(ActivityInfo activityInfo);
    int save(ActivityInfo activityInfo);
    int update(ActivityInfo activityInfo);
    int delete(String activityIds);
}
