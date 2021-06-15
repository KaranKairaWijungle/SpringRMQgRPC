package com.springRMQ.springMQ.controllers;

import com.springRMQ.springMQ.Producer.Produce;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/springMQ-0.0.1-SNAPSHOT")
public class ClientController {

    private  Produce produce;
    public ClientController(Produce produce) {
        this.produce = produce;
    }


    @RequestMapping("/client/{id}")
    public String client(@PathVariable String id, Model model) throws IOException, TimeoutException, JMSException {


        String message = " Called producer for client " + id + " at time  " + LocalTime.now();
        model.addAttribute("id" , id);
        model.addAttribute("message" , message);
        String requestQueueName = "AMQ_web_client_" + id;
        produce.call(requestQueueName , message);
        return "Client/client" ;
    }
}
