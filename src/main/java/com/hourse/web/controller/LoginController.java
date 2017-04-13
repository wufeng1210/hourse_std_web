package com.hourse.web.controller;

import com.hourse.web.model.Hourse;
import com.hourse.web.model.User;
import com.hourse.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ResponseBody
    @RequestMapping("getUser")
    public Object getUser(){
        List<Hourse> a =iUserService.getUserById(1);
        return a;
    }
}
