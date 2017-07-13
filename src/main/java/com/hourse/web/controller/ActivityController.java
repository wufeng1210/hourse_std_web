package com.hourse.web.controller;

import com.hourse.web.model.ActivityInfo;
import com.hourse.web.model.Hourse;
import com.hourse.web.model.ImageInfo;
import com.hourse.web.service.IActivityService;
import com.hourse.web.service.IHourseService;
import com.hourse.web.service.IImageInfoService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.ImageBase64Util;
import com.hourse.web.util.MapUtil;
import com.hourse.web.util.PropertiesUtils;
import com.hourse.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("activity")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private IActivityService activityService;
    @Autowired
    private IImageInfoService iImageInfoService;



    @RequestMapping("manager")
    public String manager() {
        return "activityManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(ActivityInfo activityInfo) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            List<ActivityInfo> activityList = activityService.queryList(activityInfo);
            List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
            for(ActivityInfo a:activityList){
                Map<String,Object> m = MapUtil.toMap(a);
                m.put("statusStr", StringUtil.valid_invalid_map.get(a.getSTATUS()));
                resList.add(m);
            }
            int total = activityService.count(activityInfo);
            resMap.put("total", total);
            resMap.put("rows",resList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public Map<String,Object> saveOrUpdate(ActivityInfo activityInfo,String imageBases) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int saveNums = 0;
            if( -1 != activityInfo.getActivityId()){
                saveNums=activityService.update(activityInfo);
            }else{
                saveNums=activityService.save(activityInfo);
            }
            ActivityInfo temp = activityService.queryList(activityInfo).get(0);
            iImageInfoService.delete(String.valueOf(temp.getActivityId()));
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setHourseId(String.valueOf(temp.getActivityId()));
            String path = iImageInfoService.insertImageInfo(imageBases,imageInfo);
            temp.setActivityImagePath(path);
            if(saveNums>0){
                resMap.put("success", true);
                activityService.update(temp);
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
            delNums=activityService.delete(delIds);
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
