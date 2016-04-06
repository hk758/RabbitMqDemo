package com.test.rabbitmq.demo.publish.direct;

import java.util.Random;
import java.util.UUID;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 发布与订阅
 * 
 * @author Sandy.he
 *
 * @date 2016年4月6日下午4:54:54
 *
 */
public class Direct {
    private static final String EXCHANGE_NAME = "temp_direct";
    private static final String[] TYPE = { "info", "warning", "error" };

    public static void main(String[] argv) throws java.io.IOException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("upsmart");
        factory.setPassword("upsmart");
        factory.setVirtualHost("/dev");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器的类型
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

        // 发送6条消息
        for (int i = 0; i < TYPE.length; i++) {
            String rand = getRandom();
            String message = rand + "_log :" + UUID.randomUUID().toString();
            // 持久化
            channel.queueDeclare(TYPE[i], true, false, false, null);
            // 流量
            channel.basicQos(1);
            // 将消息队列绑定到Exchange
            channel.queueBind(TYPE[i], EXCHANGE_NAME, TYPE[i]);
            // 发布消息至转发器，指定routingkey
            channel.basicPublish(EXCHANGE_NAME, TYPE[i], null, message.getBytes());
            System.out.println("队列" + TYPE[i] + "绑定成功！");
        }

        channel.close();
        connection.close();
    }

    /**
     * 随机产生一种日志类型
     * 
     * @return
     */
    private static String getRandom() {
        Random random = new Random();
        int ranVal = random.nextInt(3);
        return TYPE[ranVal];
    }
}
