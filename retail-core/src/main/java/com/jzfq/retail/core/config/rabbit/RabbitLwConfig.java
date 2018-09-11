//package com.jzfq.retail.core.config.rabbit;
//
//import com.jzfq.retail.core.messaging.support.lw.LwMQCondition;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//
///**
// * @author liuwei
// * @time 2018/1/4 15:45
// * @description 普惠运营MQ配置类
// */
////@Configuration
////@Conditional(LwMQCondition.class)
//public class RabbitLwConfig {
//
//    @Autowired
//    @Qualifier("mqTaskExecutor")
//    protected TaskExecutor mqTaskExecutor;
//
//    @Value("${rabbitmq.lw.address}")
//    private String serverAddress;
//    @Value("${rabbitmq.lw.username}")
//    private String userName;
//    @Value("${rabbitmq.lw.password}")
//    private String password;
//    @Value("${rabbitmq.lw.vhost}")
//    private String vhost;
//    @Value("${rabbitmq.lw.trans.exchange.name}")
//    protected String exchangeName;
//    @Value("${rabbitmq.lw.trans.req.queue.name}")
//    protected String reqQueueName;
//    @Value("${rabbitmq.lw.trans.req.queue.binding.pattern}")
//    protected String contractRelationPushBindingPattern;
//
//    @Value("${rabbitmq.lw.pho.req.queue.binding.pattern}")
//    protected String phoPushBindingPattern;
//
//    @Bean
//    public ConnectionFactory lwRabbitConnectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setAddresses(serverAddress);
//        connectionFactory.setUsername(userName);
//        connectionFactory.setPassword(password);
//        connectionFactory.setVirtualHost(vhost);
//        connectionFactory.setPublisherConfirms(true);//设置发布确认
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitAdmin lwRabbitAdmin() {
//        return new RabbitAdmin(lwRabbitConnectionFactory());
//    }
//
//}
