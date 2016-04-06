package com.test.rabbitmq.demo.tutorialOne;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 简单rabbitmq例子,服务提供者
 * 
 * @author Sandy.he
 *
 * @date 2016年4月6日下午4:50:11
 *
 */
public class Proivder {

    private final static String QUEUE_NAME = "demo";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("upsmart");
            factory.setPassword("upsmart");
            factory.setVirtualHost("/dev");
            Connection connertion = factory.newConnection();
            Channel channel = connertion.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, "this rabbitmq test".getBytes());
            channel.close();
            connertion.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("rabbitmq is start");
    }

}
