//package com.jzfq.retail.core.messaging.support.lw;
//
//import com.jzfq.retail.core.messaging.support.base.MQPersistenceSupport;
//import com.jzfq.retail.core.messaging.util.ProviderRealm;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
//import org.springframework.amqp.rabbit.support.CorrelationData;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author liuwei
// * @time 2018/1/4 14:57
// * @description 确认后回调:当需要发送的队列都发送成功后,进行消息确认.对于持久化的队列,意味着已经写入磁盘,对于镜像队列,意味着所有镜像都接受成功.
// */
//@Slf4j
//public class LwConfirmCallBackListener implements ConfirmCallback {
//
//    @Autowired
//    private MQPersistenceSupport mqPersistenceSupport;
//
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        if (!ack) {
//            log.error("send message failed,cause:{},correlationData:{}", cause, correlationData.toString());
//            mqPersistenceSupport.mqProducedErrorLogPersist(ProviderRealm.PHO,"普惠系统MQ消息发送异常",null,null,correlationData.toString(),cause);
//        } else {
//            log.info("send message success");
//        }
//    }
//}
