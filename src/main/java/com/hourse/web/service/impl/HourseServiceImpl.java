package com.hourse.web.service.impl;

import com.hourse.web.mapper.HourseMapper;
import com.hourse.web.model.Hourse;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IHourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Service
@Transactional
public class HourseServiceImpl implements IHourseService {

    @Resource
    private HourseMapper HourseMapper;

    public Hourse query(int hourseId) {
        return  HourseMapper.query(hourseId);
    }
    public List<Hourse> queryList(Hourse Hourse) {
        return  HourseMapper.queryList(Hourse);
    }
    public int count(Hourse Hourse) {
        return  HourseMapper.count(Hourse);
    }

    public int save(Hourse Hourse) {
        return  HourseMapper.save(Hourse);
    }

    public int update(Hourse Hourse) {
        return  HourseMapper.update(Hourse);
    }

    public int delete(String hourseIds) {
        return  HourseMapper.delete(hourseIds);
    }
}
