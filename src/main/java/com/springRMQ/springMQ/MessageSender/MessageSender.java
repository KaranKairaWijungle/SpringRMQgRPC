package com.springRMQ.springMQ.MessageSender;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
public class MessageSender implements CommandLineRunner , AutoCloseable{
    private boolean isUp = true;

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "spring_rpc_queue";

    public MessageSender() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    @Override
    public void run(String... args) throws Exception {
        if (isUp) {
            try (MessageSender fibonacciRpc = new MessageSender()) {
                for (int i = 0; i < 32; i++) {
                    String i_str = Integer.toString(i);
                    System.out.println(" [x] Requesting fib(" + i_str + ")");
//                String response = fibonacciRpc.call(i_str);
                    fibonacciRpc.call(i_str);
                    System.out.println("Called rpc for " + i_str);
//                System.out.println(" [.] Got '" + response + "'");
                }
            } catch (IOException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
        public void call (String message) throws IOException, InterruptedException {
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
            System.out.println("request published ");
        }

        public void close () throws IOException {
            connection.close();
        }

}
