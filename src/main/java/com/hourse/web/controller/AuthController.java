package com.hourse.web.controller;

import com.hourse.web.model.User;
import com.hourse.web.model.UserAuth;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.CookieUtil;
import com.hourse.web.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IUserAuthService userAuthService;
    @Autowired
    private IUserRoleService userRoleService;



    @ResponseBody
    @RequestMapping("getAuth")
    public List<Map<String,Object>> getAuth(User user) {
        List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
        try{
            UserRole userRole = userRoleService.getUserRoleByRoleId(user.getRoleId());
//            UserRole userRole = userRoleService.getUserRoleByRoleId(1);
            resList = userAuthService.getAuthsByParentId("-1",userRole.getAuthIds());
        }catch (Exception e){

        }
        return resList;
    }
}
