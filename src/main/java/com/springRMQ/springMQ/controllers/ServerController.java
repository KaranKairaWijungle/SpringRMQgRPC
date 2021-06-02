package com.springRMQ.springMQ.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServerController  {

    @RequestMapping("/server")
    public String Server(){
        return "Server/server";
    }

}
