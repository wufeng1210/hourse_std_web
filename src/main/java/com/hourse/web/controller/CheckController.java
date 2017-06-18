package com.hourse.web.controller;

import com.hourse.web.model.Hourse;
import com.hourse.web.service.IHourseService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.MapUtil;
import com.hourse.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("check")
public class CheckController {

    private static final Logger logger = LoggerFactory.getLogger(CheckController.class);

    @Autowired
    private IHourseService hourseService;
    @Autowired
    private IUserService userService;



    @RequestMapping("manager")
    public String manager() {
        return "checkManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(Hourse hourse) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            hourse.setStatus("0");
            List<Hourse> hourseList = hourseService.queryList(hourse);
            List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
            for(Hourse h:hourseList){
                Map<String,Object> m = MapUtil.toMap(h);
                m.put("userName",null == userService.query(h.getUserId())? "":userService.query(h.getUserId()).getUserName());
                m.put("statusStr", StringUtil.translateStatus(h.getStatus()));
                resList.add(m);
            }
            int total = hourseService.count(hourse);
            resMap.put("total", total);
            resMap.put("rows",resList);
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public Map<String,Object> saveOrUpdate(Hourse hourse) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int saveNums = 0;
            if( -1 != hourse.getHourseId()){
                saveNums=hourseService.update(hourse);
            }else{
                saveNums=hourseService.save(hourse);
            }
            if(saveNums>0){
                resMap.put("success", true);

            }else{
                resMap.put("success", false);
                resMap.put("errorMsg", "保存失败");
            }
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("delete")
    public Map<String,Object> delete(String delIds) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int delNums = 0;
            delNums=hourseService.delete(delIds);
            if(delNums>0){
                resMap.put("success", true);
                resMap.put("delNums", delNums);
            }else{
                resMap.put("success", false);
                resMap.put("errorMsg", "删除失败");
            }
        }catch (Exception e){

        }
        return resMap;
    }
}
