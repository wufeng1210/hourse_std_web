package com.hourse.web.service.impl;

import com.hourse.web.mapper.ActivityMapper;
import com.hourse.web.model.ActivityInfo;
import com.hourse.web.service.IActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Service
@Transactional
public class ActivityServiceImpl implements IActivityService {
    @Resource
    private ActivityMapper activityMapper;

    public ActivityInfo query(int activityId) {
        return  activityMapper.query(activityId);
    }
    public List<ActivityInfo> queryList(ActivityInfo activityInfo) {
        return  activityMapper.queryList(activityInfo);
    }
    public int count(ActivityInfo activityInfo) {
        return  activityMapper.count(activityInfo);
    }

    public int save(ActivityInfo activityInfo) {
        return  activityMapper.save(activityInfo);
    }

    public int update(ActivityInfo activityInfo) {
        return  activityMapper.update(activityInfo);
    }

    public int delete(String activityIds) {
        return  activityMapper.delete(activityIds);
    }
}
