package com.springRMQ.springMQ.Producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.log4j.BasicConfigurator;

@Component
public class Produce {

    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static  String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private  Connection connection;
    ConnectionFactory connectionFactory;
    Session session;

    public Produce() throws IOException, TimeoutException, JMSException {

//        BasicConfigurator.configure();

        connectionFactory = new ActiveMQConnectionFactory(url);

/*
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

*/
    }
    public void call(String requestQueueName , String message) throws IOException, JMSException {



        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);


        //The queue will be created automatically on the server.
        Destination destination = session.createQueue(requestQueueName);

        // MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination);

        // We will send few messages now
        TextMessage txtMessage= session.createTextMessage(message);

        // Here we are sending our messages!
        producer.send(txtMessage);

        connection.close();


    }


}
