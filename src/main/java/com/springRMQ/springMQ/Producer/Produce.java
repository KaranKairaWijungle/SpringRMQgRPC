package com.springRMQ.springMQ.Producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
public class Produce {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "spring_rpc_queue";

    public Produce() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }
    public void call(String requestQueueName , String message) throws IOException {
        final String corrId = UUID.randomUUID().toString();

        channel.queueDeclare(requestQueueName, true, false, false, null);
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .deliveryMode(2)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
        System.out.println("request published");


    }

    public void close() throws IOException {
        connection.close();
    }
}
