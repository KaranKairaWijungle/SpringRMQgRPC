package com.springRMQ.springMQ.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController {


    @RequestMapping("/client/{id}")
    public String client(@PathVariable String id){

        return "Client/Client" + id;
    }
}
