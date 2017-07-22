package com.hourse.web.controller;

import com.hourse.web.model.Hourse;
import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.ExcelUtil;
import com.hourse.web.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;



    @RequestMapping("manager")
    public String manager() {
        return "userManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(User user) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            List<User> userList = userService.queryList(user);
            List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
            for(User u : userList){
                Map<String,Object> m = MapUtil.toMap(u);
                if(u.getRoleId() !=0) {
                    m.put("roleName", userRoleService.query(u.getRoleId()).getRoleName());
                }else{
                    m.put("roleName", "前台用户");
                }
                resList.add(m);
            }
            int total = userService.count(user);
            resMap.put("total", total);
            resMap.put("rows",resList);
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public Map<String,Object> saveOrUpdate(User user) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int saveNums = 0;
            user.setUserType("0");
            user.setSecretKey(user.getUserPassWord());
            if(null == user.getAllowIds() || StringUtils.isBlank(user.getAllowIds()) || StringUtils.equals("0",user.getAllowIds())){
                user.setAllowIds("0");
            }else if(!StringUtils.endsWith(user.getAllowIds(),",")){
                user.setAllowIds(user.getAllowIds()+",");
            }
            if( -1 != user.getUserId()){
                saveNums=userService.update(user);
            }else{
                saveNums=userService.save(user);
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
            delNums=userService.delete(delIds);
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
    @RequestMapping("modifyPassword")
    public Map<String,Object> modifyPassword(int userId,String oldPassword,String newPassword) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            User user=userService.query(userId);
            if(user == null || !user.getUserPassWord().equals(oldPassword)){
                resMap.put("success", "true");
                resMap.put("errorMsg", "账号不存在或原密码错误");
            }else {
                user.setUserPassWord(newPassword);
                int updateNum = userService.update(user);
                if (updateNum > 0) {
                    resMap.put("success", "true");
                } else {
                    resMap.put("success", "true");
                    resMap.put("errorMsg", "修改失败");
                }
            }
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("fileExport")
    public Map<String,Object> fileExport(HttpServletRequest request,
                                         HttpServletResponse response) throws ServletException,IOException {
        Map<String, Object> resMap = new HashMap<String, Object>();
        try{
            List<User> list = userService.queryList(new User());
            Workbook wb= fillUserDataWithTemplate(list,"export_user_temp.xlsx");
            response.setHeader("Content-Disposition", "attachment;filename="+new String("用户信息.xlsx".getBytes("utf-8"),"iso8859-1"));
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
     * 用户excel
     * @param list
     * @param templateFileName
     * @return
     * @throws Exception
     */
    public Workbook fillUserDataWithTemplate(List<User> list ,
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
        for(User m : list){
            Row row=sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(m.getUserId());
            row.createCell(1).setCellValue(m.getUserName());
            row.createCell(2).setCellValue(m.getUserPassWord());
            row.createCell(3).setCellValue(null == userRoleService.query(m.getRoleId())? "":userRoleService.query(m.getRoleId()).getRoleName());
            row.createCell(4).setCellValue(m.getMobile());
            row.createCell(5).setCellValue(m.getNAME());
            row.createCell(6).setCellValue(m.getQq());
            row.createCell(7).setCellValue(m.getWechat());
            row.createCell(8).setCellValue(m.getNickName());
        }

        return wb;
    }
}
