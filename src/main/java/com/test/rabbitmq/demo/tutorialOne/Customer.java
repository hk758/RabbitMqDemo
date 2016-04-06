package com.test.rabbitmq.demo.tutorialOne;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 简单rabbitmq例子,服务消费都
 * 
 * @author Sandy.he
 *
 * @date 2016年4月6日下午4:51:18
 *
 */
public class Customer {

    private final static String QUEUE_NAME = "demo";

    public static void main(String[] args) {
        System.out.println("===============C1================");
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("upsmart");
            factory.setPassword("upsmart");
            factory.setVirtualHost("/dev");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, true, consumer);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received '" + message + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
