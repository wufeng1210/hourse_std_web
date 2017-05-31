package com.hourse.web.service;

import com.hourse.web.model.Hourse;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IHourseService {

    List<Hourse> queryList(Hourse Hourse);
    int count(Hourse Hourse);
    int save(Hourse Hourse);
    int update(Hourse Hourse);
    int delete(String roleIds);
}
