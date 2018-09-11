package com.jzfq.retail.core.messaging.support.lw;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author liuwei
 * @time 2017/1/4 18:52
 * @description 普惠运营 消息消费端开启条件
 */
public class LwConsumerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String dynamicApp=conditionContext.getEnvironment().getProperty("dynamic.app");
        String producerKey="";
        String consumerKey="";
        if("ph-ws".equals(dynamicApp)){
            producerKey="ws.rabbitmq.lw.producer";
            consumerKey="ws.rabbitmq.lw.consumer";
        }
        return "true".equals(conditionContext.getEnvironment().getProperty(consumerKey));
    }
}
