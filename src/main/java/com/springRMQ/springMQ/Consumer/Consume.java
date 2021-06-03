package com.springRMQ.springMQ.Consumer;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
public class Consume {


    public void consume(String RPC_QUEUE_NAME) throws Exception {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);

                System.out.println(" [x] Awaiting RPC requests");

                while(channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null).getMessageCount() > 0) {

                    channel.basicQos(1);


                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                                .Builder()
                                .correlationId(delivery.getProperties().getCorrelationId())
                                .deliveryMode(2)
                                .build();

                        String response = "";

                        try {
                            String message = new String(delivery.getBody(), "UTF-8");
                            response = "consumed message from " + RPC_QUEUE_NAME + " Message is : " + message;
                            System.out.println(response);

                        } catch (RuntimeException e) {
                            System.out.println(" [.] " + e.toString());
                        } finally {
                            channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        }
                    };

                    String tag = channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
                    }));
                    channel.basicCancel(tag);
                }

            }

    }

}
