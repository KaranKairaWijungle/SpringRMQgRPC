package com.springRMQ.springMQ.MessageReceiver;

import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver implements CommandLineRunner {

    private boolean isUp = true; // false -> shows that server is down right now

    // change queue name here
    private static final String RPC_QUEUE_NAME = "client_1";

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }


    @Override
    public void run(String... args) throws Exception {

        if (isUp) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);
//            channel.queuePurge(RPC_QUEUE_NAME);

                channel.basicQos(1);

                System.out.println(" [x] Awaiting RPC requests");

                Object monitor = new Object();
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(delivery.getProperties().getCorrelationId())
                            .deliveryMode(2)
                            .build();

                    String response = "";

                    try {
                        String message = new String(delivery.getBody(), "UTF-8");
                        response = "consumed message from " + RPC_QUEUE_NAME;
                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e.toString());
                    } finally {
                        channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        // RabbitMq consumer worker thread notifies the RPC server owner thread
                        synchronized (monitor) {
                            monitor.notify();
                        }
                    }
                };

                channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
                }));
                // Wait and be prepared to consume the message from RPC client.
                while (true) {
                    synchronized (monitor) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }
}
