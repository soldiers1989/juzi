package com.jzfq.retail.core.rocketmq;

import com.aliyun.openservices.ons.api.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

@Slf4j
//@Component
public abstract class AbstractMQConsumer {

    @Value("${topic.account.repay.status}")
    String topic;

    @Value("${cid.account.repay.status.newretail}")
    String consumerId;

    @Value("${tag.repay.status}")
    String tag;

    @Autowired
    MQConfig mqConfig;

    Consumer consumer = null;

    //被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行,init()方法之前执行。
    @PostConstruct
    public void init() {
        Properties properties = new Properties();

        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, mqConfig.getAccessKey());
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, mqConfig.getSecretKey());
        // 设置 TCP 接入域名（此处以公共云生产环境为例）
        properties.put(PropertyKeyConst.ONSAddr, mqConfig.getAddress());
        // 集群模式，不同的cid是广播，相同的cid是争抢
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, mqConfig.getSendMsgTimeoutMillis());
        // 线程数
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, mqConfig.getConsumeThreadNums());
        // 您在控制台创建的 Consumer ID，一个consumerId一个子类，处理不同的接收业务
        properties.put(PropertyKeyConst.ConsumerId, consumerId);
        if (log.isInfoEnabled()) {
            log.info("准备启动MQ消费者, 消费者ID：" + consumerId);
        }
        consumer = ONSFactory.createConsumer(properties);
        if (consumer != null && consumer.isClosed()) {
            consumer.start();
            if (consumer.isStarted() && log.isInfoEnabled()) {
                log.info("MQ消费者已启动, 消费者ID：" + consumerId);
            }
        }
        consumer.subscribe(topic, "*", new MessageListener() { //订阅全部Tag
            public Action consume(Message message, ConsumeContext context) {
                if (log.isDebugEnabled()) {
                    log.info("MQ消费者开始接收消息， 消费者ID:" + consumerId);
                    log.info("消息内容:" + message);
                }
                if (tag == null || "".equals(tag) || "*".equals(tag)
                    || tag.equals(message.getTag().trim())) {
                    if (log.isInfoEnabled()) {
                        log.info("消费tag：{} 与接收tag: {} 匹配，开始消费");
                    }

                    long startTime = System.currentTimeMillis();
                    Boolean r = handleAction(message, context);
                    long time = System.currentTimeMillis() - startTime;
                    if (log.isInfoEnabled()) {
                        log.info("MQ消费者处理结果：" + r + "， 消费者ID:" + consumerId + "，耗时: " + time);
                    }
                    if (r) {
                        return Action.CommitMessage;
                    }
                    return Action.ReconsumeLater;
                } else {
                    if (log.isInfoEnabled()) {
                        log.info("消费tag：{} 与接收tag: {} 不匹配，不消费并返回成功！");
                    }
                    return Action.CommitMessage;
                }
            }
        });
    }

    //PreDestroy（）方法在destroy()方法执行执行之后执行
    @PreDestroy
    public void destroy() {
        if (consumer != null && consumer.isStarted())
            consumer.shutdown();
    }

    abstract Boolean handleAction(Message message, ConsumeContext context);
}
