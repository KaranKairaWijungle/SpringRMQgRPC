package com.springRMQ.springMQ.controllers;

import com.springRMQ.springMQ.MessageSender.MessageSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController {

    private MessageSender messageSender;

    public ClientController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @RequestMapping("/client/{id}")
    public String client(@PathVariable String id, Model model){
        model.addAttribute("id" , id);
        return "Client/client" ;
    }
}
