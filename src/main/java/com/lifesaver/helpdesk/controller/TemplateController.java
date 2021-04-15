package com.lifesaver.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/template")
    public String getTemplate(){

        return "user/blank_page_template";
    }

    @GetMapping("/login-template")
    public String getLogInTemplate(){

        return "user/login_template";
    }

}
