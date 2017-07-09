package com.hourse.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hourse.web.model.Hourse;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IHourseService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.ExcelUtil;
import com.hourse.web.util.MapUtil;
import com.hourse.web.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("hourse")
public class HourseController {

    private static final Logger logger = LoggerFactory.getLogger(HourseController.class);

    @Autowired
    private IHourseService hourseService;
    @Autowired
    private IUserService userService;


    @RequestMapping("manager")
    public String manager() {
        return "hourseManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(Hourse hourse) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            hourse.setStatus("1");
            List<Hourse> hourseList = hourseService.queryList(hourse);
            List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
            for(Hourse h:hourseList){
                Map<String,Object> m = MapUtil.toMap(h);
                m.put("userName",null == userService.query(h.getUserId())? "":userService.query(h.getUserId()).getUserName());
                m.put("statusStr", StringUtil.translateStatus(h.getStatus()));//状态
                m.put("packingingLotStr", StringUtil.yes_no_map.get(""+h.getPackingingLot()));//是否有车位
                m.put("recommendStr", StringUtil.yes_no_map.get(h.getRecommend()));//是否推荐
                m.put("isLendStr", StringUtil.yes_no_map.get(h.getIsLend()));//是否已出租
                String preLendUserMobile = null== h.getPreLendUserMobile()?"":h.getPreLendUserMobile();
                m.put("preLendUserMobile",preLendUserMobile);
                String nowLendUserMobile = null== h.getNowLendUserMobile()?"":h.getNowLendUserMobile();
                m.put("nowLendUserMobile",nowLendUserMobile);
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

    @ResponseBody
    @RequestMapping("fileupload")
    public Map<String,Object> fileupload(HttpServletRequest request,
                                         HttpServletResponse response) throws ServletException,IOException {
        Map<String,Object> resMap = new HashMap<String, Object>();
        String fileName=null;
        File tempPathFile = null;
        String uploadPath = null;
        try{

            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(40960);
            factory.setRepository(tempPathFile);//设置缓冲区目录
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(41943040);//设置最大文件尺寸

            @SuppressWarnings("unchecked")
            List<FileItem> items=upload.parseRequest(request);
            Iterator<FileItem> i = items.iterator();
            FileItem fi =null;
            while(i.hasNext()){
                fi=(FileItem)i.next();
                fileName=fi.getName();
                if(fileName!=null){
                    File fullFile=new File(fi.getName());
                    File savedFile =new File(uploadPath,fullFile.getName());
                    fi.write(savedFile);

                }

            }
            XSSFWorkbook wb=new XSSFWorkbook(fi.getInputStream());
            XSSFSheet xssfsheet=wb.getSheetAt(0);
            Hourse hourse=new Hourse();
            if(xssfsheet!=null){
                int successNums = 0;
                for(int rowNum=1;rowNum<=xssfsheet.getLastRowNum();rowNum++){
                    XSSFRow xssfRow=xssfsheet.getRow(rowNum);
                    if(xssfRow==null){
                        continue;
                    }
                    //设置对象数据
                    hourse.setUserId((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(1))));
                    hourse.setHourseAddr(ExcelUtil.formateCell(xssfRow.getCell(2)));
                    hourse.setLongitude(ExcelUtil.formateCell(xssfRow.getCell(3)));
                    hourse.setLatitude(ExcelUtil.formateCell(xssfRow.getCell(4)));
                    hourse.setProvince(ExcelUtil.formateCell(xssfRow.getCell(5)));
                    hourse.setCity(ExcelUtil.formateCell(xssfRow.getCell(6)));
                    hourse.setArea(ExcelUtil.formateCell(xssfRow.getCell(7)));
                    hourse.setResidentialQuarters(ExcelUtil.formateCell(xssfRow.getCell(8)));
                    hourse.setRoomNum((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(9))));
                    hourse.setToiletNum((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(10))));
                    hourse.setHallNum((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(11))));
                    hourse.setKitchenNum((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(12))));
                    hourse.setMonthly((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(13))));
                    hourse.setPackingingLot((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(14))));
                    hourse.setRentingWay(ExcelUtil.formateCell(xssfRow.getCell(15)));
                    hourse.setLimitType(ExcelUtil.formateCell(xssfRow.getCell(16)));
                    hourse.setFixtureType(ExcelUtil.formateCell(xssfRow.getCell(17)));
                    String brokerMobile = ExcelUtil.formateCell(xssfRow.getCell(18));
                    if(StringUtils.isNotBlank(brokerMobile)){
                        hourse.setBrokerMobile(new DecimalFormat("#").format(Double.parseDouble(brokerMobile)));
                    }
                    hourse.setBrokerCode(ExcelUtil.formateCell(xssfRow.getCell(19)));
                    hourse.setBrokerName(ExcelUtil.formateCell(xssfRow.getCell(20)));
                    hourse.setAreaCovered(ExcelUtil.formateCell(xssfRow.getCell(21)));
                    hourse.setSquarePrice(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(22)))));
                    hourse.setFurniture(ExcelUtil.formateCell(xssfRow.getCell(23)));
                    hourse.setNear(ExcelUtil.formateCell(xssfRow.getCell(24)));
                    hourse.setTraffic(ExcelUtil.formateCell(xssfRow.getCell(25)));
                    hourse.setStatus(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(26)))));
                    hourse.setDescription(ExcelUtil.formateCell(xssfRow.getCell(27)));
                    hourse.setRecommend(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(28)))));
                    hourse.setIsLend(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(29)))));
                    hourse.setOrientations(ExcelUtil.formateCell(xssfRow.getCell(30)));
                    hourse.setFloor(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(31)))));
                    hourse.setCollect(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(32)))));
                    String preLendUserMobile = ExcelUtil.formateCell(xssfRow.getCell(33));
                    if(StringUtils.isNotBlank(preLendUserMobile)){
                        hourse.setPreLendUserMobile(new DecimalFormat("#").format(Double.parseDouble(preLendUserMobile)));
                    }
                    String nowBrokerMobile = ExcelUtil.formateCell(xssfRow.getCell(34));
                    if(StringUtils.isNotBlank(nowBrokerMobile)){
                        hourse.setNowLendUserMobile(new DecimalFormat("#").format(Double.parseDouble(nowBrokerMobile)));
                    }
                    int saveNums = 0;
                    saveNums=hourseService.save(hourse);
                    successNums += saveNums+1;
                    resMap.put("success", true);
                    resMap.put("successNums", successNums);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            resMap.put("success", false);
            resMap.put("errorMsg", "上传失败");
        }
        return resMap;
    }

}
