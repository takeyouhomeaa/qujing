package edu.fzu.qujing.util;

import com.rabbitmq.client.*;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class MQCustomer {

    @Autowired
    UserService userService;

    static ConnectionFactory connectionFactory;
    static Connection connection;
    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.100.49.47");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/qujing");
        connectionFactory.setUsername("ozg");
        connectionFactory.setPassword("ozg123zxc");
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void receive(){
        try {
            Channel channel = connection.createChannel();
            channel.basicConsume("qujing", true, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String s = new String(body);
                    if(s.contains("&")) {
                        String[] split = s.split("&");
                        String[] msg = split[1].split("=");
                        User user = userService.getUserByStudentId(msg[1]);

                        if (user != null) {
                            RedisUtil.set("user::getUserByStudentId(" + user.getStudentId() + ")", user);
                            RedisUtil.set("user::getUserByPhone(" + user.getPhone() + ")", user);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
