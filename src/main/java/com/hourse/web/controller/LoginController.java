package com.hourse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dell on 2017/4/10.
 */
@Controller
public class LoginController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
