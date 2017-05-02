package com.hourse.web.controller;

import com.hourse.web.model.User;
import com.hourse.web.model.UserAuth;
import com.hourse.web.service.IUserAuthService;
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
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IUserAuthService userAuthService;




    @ResponseBody
    @RequestMapping()
    public List<Map<String,Object>> getAuth() {
        List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
        try{
            resList = userAuthService.getAuthsByParentId("-1","1");
        }catch (Exception e){

        }
        return resList;
    }
}
