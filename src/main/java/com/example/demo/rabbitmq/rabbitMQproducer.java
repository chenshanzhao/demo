package com.example.demo.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * @author Administrator
 *
 */
public class rabbitMQproducer {
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
            channel.basicPublish("", "test", null, "hello rabbit".getBytes("UTF-8"));
            channel.basicPublish("", "test1", null, "hello ".getBytes("UTF-8"));
            channel.basicPublish("", "test2", null, "rabbit ".getBytes("UTF-8"));
            channel.close();
            connect.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
