package com.example.demo.rabbitmq;

import com.rabbitmq.client.*;

/**
 * 消费者
 * @author Administrator
 *
 */
public class rabbitMGconsumer {


    private static String host = "127.0.0.1";
    private static String userName = "guest";
    private static String passWord = "guest";
    private static int port = 5672;
    /**
     * @param args
     */
    public static void main(String[] args) {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(userName);
            factory.setPassword(passWord);
            Connection connect = factory.newConnection();
            Channel channel = connect.createChannel();
            AMQP.Queue.DeclareOk declareOK = channel.queueDeclare("test", false, false, false, null);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume("test1", true, consumer);
            while (true) {
                QueueingConsumer.Delivery deliver = consumer.nextDelivery();
                System.out.println("reciver messager:"+new String(deliver.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

