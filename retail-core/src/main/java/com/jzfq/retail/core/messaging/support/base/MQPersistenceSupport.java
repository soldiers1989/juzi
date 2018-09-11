package com.jzfq.retail.core.messaging.support.base;

import com.jzfq.retail.bean.mapper.MQErrorLog;
import com.jzfq.retail.core.dao.MQErrorLogMapper;
import com.jzfq.retail.core.messaging.util.ProviderRealm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lagon
 * @time 2017/6/27 18:15
 * @description MQ持久化支撑类
 */
@Slf4j
@Component
public class MQPersistenceSupport {

    @Autowired
    private MQErrorLogMapper mqErrorLogMapper;

    //MQ消息生产时产生错误日志持久化
    public void mqProducedErrorLogPersist(ProviderRealm provider, String scene, String exchange, String routingKey, String message, String exception) {
        MQErrorLog mqErrorLogPO=new MQErrorLog();
        mqErrorLogPO.setOccasion(2);
        mqErrorLogPO.setException(exception);
        mqErrorLogPO.setProvider(provider.getName());
        mqErrorLogPO.setMessage(message);
        mqErrorLogPO.setScene(scene);
        mqErrorLogPO.setExchange(exchange);
        mqErrorLogPO.setRoutingKey(routingKey);
        mqErrorLogMapper.saveMQErrorLog(mqErrorLogPO);
    }

    //MQ消息消费时产生错误日志持久化
    public void mqConsumedErrorLogPersist(ProviderRealm provider, String scene, String exchange, String routingKey, String message, String exception) {
        MQErrorLog mqErrorLogPO=new MQErrorLog();
        mqErrorLogPO.setOccasion(1);
        mqErrorLogPO.setException(exception);
        mqErrorLogPO.setProvider(provider.getName());
        mqErrorLogPO.setMessage(message);
        mqErrorLogPO.setScene(scene);
        mqErrorLogPO.setExchange(exchange);
        mqErrorLogPO.setRoutingKey(routingKey);
        mqErrorLogMapper.saveMQErrorLog(mqErrorLogPO);
    }


}
