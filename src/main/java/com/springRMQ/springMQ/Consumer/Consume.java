package com.springRMQ.springMQ.Consumer;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Component;


import javax.jms.*;
import javax.jms.Connection;

@Component
public class Consume {

    private static  String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private Connection connection;
    javax.jms.ConnectionFactory connectionFactory;
    Session session;
    public Consume() throws JMSException {
//        BasicConfigurator.configure();

        connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
    }

    public void consume(String RPC_QUEUE_NAME) throws Exception {

        // Getting the queue named 'test'
           Destination destination = session.createQueue(RPC_QUEUE_NAME);

        // MessageConsumer is used for receiving (consuming) messages
        MessageConsumer consumer = session.createConsumer(destination);

        // Here we receive the message.
        Message message = consumer.receive();

        connection.close();

    }

}
