package com.springRMQ.springMQ.controllers;

import com.springRMQ.springMQ.Consumer.Consume;
import com.springRMQ.springMQ.Producer.Produce;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.JMSException;
import java.io.IOException;
import java.time.LocalTime;

@Controller
@RequestMapping("/springMQ-0.0.1-SNAPSHOT")
public class ServerController  {

    private final Consume consume;
    private final Produce produce;

    public ServerController(Consume consume, Produce produce) {
        this.consume = consume;
        this.produce = produce;
    }

    @RequestMapping("/server")
    public String Server(){
        return "Server/server";
    }

    @RequestMapping("/server/consume/client/{id}")
    public String consume_client(@PathVariable String id, Model model) throws Exception {

        String queueName = "AMQ_web_client_" + id;
        consume.consume(queueName);
        System.out.println("server consume " + id);

        return "Home/home";
    }

    @RequestMapping("/server/send/client/{id}")
    public String send_to_client(@PathVariable String id,Model model) throws IOException, JMSException {
        String message = " Called producer for client " + id + " at time  " + LocalTime.now() + " from server ";
        String requestQueueName = "AMQ_web_client_" + id;
        model.addAttribute("message" , message);
        model.addAttribute("id" , id);
        produce.call(requestQueueName , message);
        return "Client/client" ;
    }

}
