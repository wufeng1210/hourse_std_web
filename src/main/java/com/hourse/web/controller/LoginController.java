package com.hourse.web.controller;

import com.hourse.web.model.User;
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
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private IUserService iUserService;


    @RequestMapping("index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
//        modelAndView.addObject("securityName","1233");
        request.setAttribute("securityName","12212");
        return modelAndView;
    }

    /**
     * 登录并跳转到主页
     * @param user
     * @param response
     * @return
     */
    @RequestMapping("login")
    public ModelAndView login(User user,HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session=request.getSession();
        String imageCode=request.getParameter("Captcha");
        if(null == session || null == session.getAttribute("sRand") || null == imageCode){
            modelAndView.setViewName("index");
            modelAndView.addObject("securityName","12212");
            return modelAndView;
        }

        if(!imageCode.equals(session.getAttribute("sRand"))){
            modelAndView.setViewName("index");
            modelAndView.addObject("securityName","12212");
            modelAndView.addObject("loginError","验证码错误");
            return modelAndView;
        }
        List<User> userList =iUserService.getUserById(user);
        if(!userList.isEmpty()){
            CookieUtil.setObjectCookie(response, userList.get(0), "hoursestd", -1, PropertiesUtils.get("domain"));
            modelAndView.addObject("userInfo",userList.get(0));
            modelAndView.setViewName("home");
        }else{
            modelAndView.setViewName("index");
            modelAndView.addObject("securityName","12212");
            modelAndView.addObject("loginError","账号密码错误");
        }

        return modelAndView;
    }

    /**
     * 登录并跳转到主页
     * @param user
     * @param response
     * @return
     */
    @RequestMapping("logout")
    public ModelAndView logout(User user, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        CookieUtil.clearCookie(response,"hoursestd", PropertiesUtils.get("domain"));
        modelAndView.addObject("securityName","12212");
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
