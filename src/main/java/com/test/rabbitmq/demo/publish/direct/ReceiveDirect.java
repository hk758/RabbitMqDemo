package com.test.rabbitmq.demo.publish.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 发布与订阅
 * 
 * @author Sandy.he
 *
 * @date 2016年4月6日下午4:55:25
 *
 */
public class ReceiveDirect {
    private static final String EXCHANGE_NAME = "temp_direct";
    private static final String[] TYPE = { "info", "warning", "error" };

    public static void main(String[] argv) throws java.io.IOException, java.lang.InterruptedException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("upsmart");
        factory.setPassword("upsmart");
        factory.setVirtualHost("/dev");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        // 声明direct类型转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        for (int i = 0; i < TYPE.length; i++) {
            // 持久化
            channel.queueDeclare(TYPE[i], true, false, false, null);
            // 流量控制
            channel.basicQos(1);
            // 将消息队列绑定到Exchange
            channel.queueBind(TYPE[i], EXCHANGE_NAME, TYPE[i]);

            System.out.println("队列" + TYPE[i] + "绑定成功！");
        }

        for (int i = 0; i < TYPE.length; i++) {
            final String queue = TYPE[2];
            new Thread() {
                public void run() {
                    try {
                        receive(channel, queue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private static void receive(Channel channel, String QUEUE_NAME) throws Exception {
        // 声明消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, false, consumer);
        while (true) {
            // 等待队列推送消息
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(QUEUE_NAME + " Received '" + message + "'");
            // 反馈给服务器表示收到信息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
