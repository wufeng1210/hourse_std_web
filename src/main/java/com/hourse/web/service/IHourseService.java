package com.hourse.web.service;

import com.hourse.web.model.Hourse;
import com.hourse.web.model.UserRole;

import java.util.List;

/**
 * Created by wufeng on 2017/4/16.
 */
public interface IHourseService {

    Hourse query(int hourseId);
    List<Hourse> queryList(Hourse Hourse);
    int count(Hourse Hourse);
    int save(Hourse Hourse);
    int update(Hourse Hourse);
    int delete(String houeseIds);
}
