package com.hourse.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hourse.web.http.HttpPostHandle;
import com.hourse.web.model.ActivityInfo;
import com.hourse.web.model.Hourse;
import com.hourse.web.model.ImageInfo;
import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IHourseService;
import com.hourse.web.service.IImageInfoService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.ExcelUtil;
import com.hourse.web.util.MapUtil;
import com.hourse.web.util.StringUtil;
import net.sf.json.JSONArray;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    @Autowired
    private IImageInfoService iImageInfoService;


    @RequestMapping("manager")
    public ModelAndView manager(String nowUserId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("nowUserId",nowUserId);
        modelAndView.setViewName("hourseManager");
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(Hourse hourse ,String nowUserId) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            User user = userService.query(Integer.valueOf(nowUserId));
//            if(null != user && StringUtils.isNotBlank(user.getAllowIds())){
//                hourse.setUserId(user.getAllowIds());
//            }
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
                if(null != user && !StringUtils.equals("0",user.getAllowIds()) && StringUtils.isNotBlank(user.getAllowIds())){
                    if(StringUtils.contains(user.getAllowIds(),h.getUserId()+",")){
                        resList.add(m);
                    }
                }else {
                    resList.add(m);
                }
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
    @RequestMapping("uploadImg")
    public Map<String,Object> uploadImg(Hourse hourse,String imageBases) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            ImageInfo info = new ImageInfo();
            if(StringUtils.isNotBlank(imageBases)){
                Hourse temp = hourseService.queryList(hourse).get(0);
                iImageInfoService.delete(String.valueOf(temp.getHourseId()));
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setHourseId(String.valueOf(temp.getHourseId()));
                info = iImageInfoService.insertImageInfo(imageBases,imageInfo);
            }

            if(info!=null && info.getImagePath() !=null && info.getImageUrl()!=null &&
                    StringUtils.isNotBlank(info.getImagePath()) && StringUtils.isNotBlank(info.getImageUrl()) ){
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
                    hourse.setUserId((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(0))));
                    String hourseAddr = ExcelUtil.formateCell(xssfRow.getCell(1));
                    hourse.setHourseAddr(hourseAddr);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("address", hourseAddr);
                    String jsonStr = HttpPostHandle.httpGetDirectionOfGaode(map);
                    net.sf.json.JSONObject cityJson = net.sf.json.JSONObject.fromObject(jsonStr);
                    JSONArray jsonArray = cityJson.optJSONArray("geocodes");
                    if(jsonArray != null && jsonArray.size()>0){
                        String[] location = jsonArray.getJSONObject(0).getString("location").split(",");
                        System.out.println(location[0]+"--"+location[1]);
                        hourse.setLongitude(location[0]);
                        hourse.setLatitude(location[1]);
                    }else{
                        hourse.setLongitude("0");
                        hourse.setLatitude("0");
                    }
                    hourse.setProvince(ExcelUtil.formateCell(xssfRow.getCell(2)));
                    hourse.setCity(ExcelUtil.formateCell(xssfRow.getCell(3)));
                    hourse.setArea(ExcelUtil.formateCell(xssfRow.getCell(4)));
                    hourse.setResidentialQuarters(ExcelUtil.formateCell(xssfRow.getCell(5)));
                    hourse.setRoomNum(ExcelUtil.formateCell(xssfRow.getCell(6)));
                    hourse.setToiletNum(ExcelUtil.formateCell(xssfRow.getCell(7)));
                    hourse.setHallNum(ExcelUtil.formateCell(xssfRow.getCell(8)));
                    hourse.setMonthly(ExcelUtil.formateCell(xssfRow.getCell(9)));
                    hourse.setRentingWay(ExcelUtil.formateCell(xssfRow.getCell(10)));
                    hourse.setLimitType(ExcelUtil.formateCell(xssfRow.getCell(11)));
                    hourse.setFixtureType(ExcelUtil.formateCell(xssfRow.getCell(12)));
                    String brokerMobile = ExcelUtil.formateCell(xssfRow.getCell(13));
                    if(StringUtils.isNotBlank(brokerMobile)){
                        hourse.setBrokerMobile(new DecimalFormat("#").format(Double.parseDouble(brokerMobile)));
                    }
                    hourse.setBrokerName(ExcelUtil.formateCell(xssfRow.getCell(14)));
                    hourse.setAreaCovered(ExcelUtil.formateCell(xssfRow.getCell(15)));
                    hourse.setSquarePrice(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(16)))));
                    hourse.setFurniture(ExcelUtil.formateCell(xssfRow.getCell(17)));
                    hourse.setNear(ExcelUtil.formateCell(xssfRow.getCell(18)));
                    hourse.setTraffic(ExcelUtil.formateCell(xssfRow.getCell(19)));
                    hourse.setOrientations(ExcelUtil.formateCell(xssfRow.getCell(20)));
                    hourse.setFloor(String.valueOf((int)Double.parseDouble(ExcelUtil.formateCell(xssfRow.getCell(21)))));
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

    @ResponseBody
    @RequestMapping("fileExport")
    public Map<String,Object> fileExport(String nowUserId,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        Map<String, Object> resMap = new HashMap<String, Object>();
         try{
             List<Hourse> excelList = new ArrayList<Hourse>();
             List<Hourse> list = hourseService.queryList(new Hourse());
             for(Hourse h : list) {
                 User user = userService.query(Integer.valueOf(nowUserId));
                 if (null != user && !StringUtils.equals("0", user.getAllowIds()) && StringUtils.isNotBlank(user.getAllowIds())) {
                     if (StringUtils.contains(user.getAllowIds(), h.getUserId() + ",")) {
                         excelList.add(h);
                     }
                 } else {
                     excelList.add(h);
                 }
             }
             Workbook wb= fillHourseDataWithTemplate(excelList,"export_hourse_temp.xlsx");
             response.setHeader("Content-Disposition", "attachment;filename="+new String("房屋信息.xlsx".getBytes("utf-8"),"iso8859-1"));
             response.setContentType("application/ynd.ms-excel;charset=UTF-8");
             OutputStream out=response.getOutputStream();
             wb.write(out);
             out.flush();
             out.close();
    }catch(Exception e){
        e.printStackTrace();
        resMap.put("success", false);
        resMap.put("errorMsg", "上传失败");
    }
        return resMap;
    }
    /**
     * 房屋excel
     * @param list
     * @param templateFileName
     * @return
     * @throws Exception
     */
    public Workbook fillHourseDataWithTemplate(List<Hourse> list ,
                                               String templateFileName) throws Exception {
        // TODO Auto-generated method stub
        String path = ExcelUtil.class.getClassLoader().getResource("/").getPath();
        path=path.replace("classes\\", ""); //去掉class\
        path=path.replace("classes/", ""); //去掉class\
        path=path.replace("WEB-INF\\", ""); //WEB-INF\
        path=path.replace("WEB-INF/", ""); //WEB-INF\
        InputStream inp=new FileInputStream(path+"template/"+templateFileName);
        XSSFWorkbook wb=new XSSFWorkbook(inp);
        Sheet sheet=wb.getSheetAt(0);
        int rowIndex=1;
        for(Hourse m : list){
            Row row=sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(m.getHourseId());
            row.createCell(1).setCellValue(m.getUserId());
            row.createCell(2).setCellValue(null == userService.query(m.getUserId())? "":userService.query(m.getUserId()).getUserName());
            row.createCell(3).setCellValue(m.getHourseAddr());
            row.createCell(4).setCellValue(m.getProvince());
            row.createCell(5).setCellValue(m.getCity());
            row.createCell(6).setCellValue(m.getArea());
            row.createCell(7).setCellValue(m.getResidentialQuarters());
            row.createCell(8).setCellValue(m.getRoomNum());
            row.createCell(9).setCellValue(m.getToiletNum());
            row.createCell(10).setCellValue(m.getHallNum());
            row.createCell(11).setCellValue(m.getKitchenNum());
            row.createCell(12).setCellValue(m.getMonthly());
            row.createCell(13).setCellValue(m.getPackingingLot());
            row.createCell(14).setCellValue(m.getRentingWay());
            row.createCell(15).setCellValue(m.getLimitType());
            row.createCell(16).setCellValue(m.getFixtureType());
            row.createCell(17).setCellValue(m.getBrokerMobile());
            row.createCell(18).setCellValue(m.getBrokerCode());
            row.createCell(19).setCellValue(m.getBrokerName());
            row.createCell(20).setCellValue(m.getAreaCovered());
            row.createCell(21).setCellValue(m.getSquarePrice());
            row.createCell(22).setCellValue(m.getFurniture());
            row.createCell(23).setCellValue(m.getNear());
            row.createCell(24).setCellValue(m.getTraffic());
            row.createCell(25).setCellValue(m.getDescription());
            row.createCell(26).setCellValue(m.getRecommend());
            row.createCell(27).setCellValue(m.getIsLend());
            row.createCell(28).setCellValue(m.getOrientations());
            row.createCell(29).setCellValue(m.getFloor());
            row.createCell(30).setCellValue(m.getPreLendUserMobile());
            row.createCell(31).setCellValue(m.getNowLendUserMobile());
            row.createCell(32).setCellValue(m.getStatus());
        }

        return wb;
    }

    @ResponseBody
    @RequestMapping("reAnalysis")
    public Map<String,Object> reAnalysis(String dealIds) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int dealNums = 0;
            String [] ids = dealIds.split(",");
            for(String s : ids){
                Hourse temp = hourseService.query(Integer.valueOf(s));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("address", temp.getHourseAddr());
                String jsonStr = HttpPostHandle.httpGetDirectionOfGaode(map);
                net.sf.json.JSONObject cityJson = net.sf.json.JSONObject.fromObject(jsonStr);
                JSONArray jsonArray = cityJson.optJSONArray("geocodes");
                if(jsonArray != null && jsonArray.size()>0){
                    String[] location = jsonArray.getJSONObject(0).getString("location").split(",");
                    System.out.println(location[0]+"--"+location[1]);
                    temp.setLongitude(location[0]);
                    temp.setLatitude(location[1]);
                }else{
                    temp.setLongitude("0");
                    temp.setLatitude("0");
                }
                dealNums += hourseService.update(temp);
            }
            if(dealNums>0){
                resMap.put("success", true);
                resMap.put("dealNums", dealNums);
            }else{
                resMap.put("success", false);
                resMap.put("errorMsg", "删除失败");
            }
        }catch (Exception e){

        }
        return resMap;
    }
}
