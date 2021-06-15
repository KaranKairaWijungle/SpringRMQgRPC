package com.springRMQ.springMQ.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/springMQ-0.0.1-SNAPSHOT")
public class HomeController {

    @RequestMapping("/")
    public String home(){
        System.out.println("home");
        return "Home/home";
    }
}
