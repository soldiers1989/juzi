package com.jzfq.retail.core.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.jzfq.retail.core.messaging.util.CallStatus;
import com.jzfq.retail.core.messaging.util.ProviderRealm;
import com.jzfq.retail.core.service.SystemLogSupport;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
//@Component
public class MQProducer {

    @Value("${pid.zp.entry.order}")
    protected String pid;

    @Value("${topic.zp.entry.order}")
    private String topic;

    @Value("${rocketmq.address}")
    private String mqAddress;

    @Autowired
    private MQConfig mqConfig;

    @Autowired
    private SystemLogSupport systemLogSupport;

    protected Producer producer = null;

    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ONSAddr, mqConfig.getAddress());
        properties.put(PropertyKeyConst.AccessKey, mqConfig.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, mqConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, mqConfig.getSendMsgTimeoutMillis());
        properties.put(PropertyKeyConst.ProducerId, pid);

        if (log.isInfoEnabled()) {
            log.info("准备启动MQ生产者, 生产者ID：" + pid);
        }
        producer = ONSFactory.createProducer(properties);
        if (producer != null && producer.isClosed()) {
            producer.start();
            if (producer.isStarted() && log.isInfoEnabled()) {
                log.info("MQ生产者已启动, 生产者ID：" + pid);
            }
        } else {
            throw new ONSClientException("producer对象为空！");
        }
    }

    public Boolean send(String message, String tag, String code) {
        byte[] body = null;
        try {
            body = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ONSClientException("转换发送内容字节数组异常");
        }
        if (tag == null || tag.length() <= 0) {
            tag = "*";
        }

        long startTime = 0l;
        Message msg = new Message(topic, tag, body);
        long threadId = Thread.currentThread().getId();
        if (log.isInfoEnabled()) {
            log.info("MQ 开始发送消息，threadId: " + threadId + "， message:" + message);
            log.info("[" + threadId + "]MQ生产者开始发zp消息， 生产者ID: " + pid);
            log.info("[" + threadId + "]消费队列topic: " + topic);
            log.info("[" + threadId + "]消息ID: " + msg.getMsgID());
            log.info("[" + threadId + "]消息tag: " + tag);
            log.info("[" + threadId + "]消息content:" + message);
        }
        startTime = System.currentTimeMillis();
        systemLogSupport.persistAccessLog(ProviderRealm.CORE_ZP, mqAddress, topic, "推送资匹进件", code, pid, CallStatus.PROCESSING, message, null, null);
        SendResult result = producer.send(msg);

        if (log.isInfoEnabled()) {
            long time = System.currentTimeMillis() - startTime;
            log.info("[" + threadId + "]MQ生产者发送消息结果 MessageId: " + result.getMessageId());
            log.info("[" + threadId + "]MQ发送消息耗时: " + time);
        }
        systemLogSupport.updateAccessLog(ProviderRealm.CORE_ZP, code, null, CallStatus.SUCCESS, result.toString(), null);
        return result.getMessageId() != null && result.getMessageId().length() > 0;
    }

    @PreDestroy
    public void destroy() {
        if (producer != null && producer.isStarted())
            producer.shutdown();
    }
}
