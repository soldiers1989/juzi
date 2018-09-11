//package com.jzfq.retail.core.messaging.support.lw;
//
//import com.jzfq.retail.core.messaging.support.base.MQConstants;
//import com.jzfq.retail.core.messaging.support.base.MQPersistenceSupport;
//import com.jzfq.retail.core.messaging.util.ProviderRealm;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author liuwei
// * @time 2018/1/4 18:26
// * @description 失败后return回调:确认消息不能路由时(exchange错误或者queue错误),进行确认操作
// */
//@Slf4j
//public class LwReturnCallBackListener implements ReturnCallback {
//
//    @Autowired
//    private MQPersistenceSupport mqPersistenceSupport;
//
//    @Override
//    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//        try {
//            String messageBody = new String(message.getBody(), MQConstants.DEFAULT_CHARSET);
//            log.error("send message content:{}",messageBody);
//            log.error("send message failed,replyCode:{},replyText:{},exchange:{},routingKey:{}", replyCode, replyText, exchange, routingKey);
//            String exDesc="send message failed,replyCode:"+replyCode+",replyText:+"+replyText+",exchange:"+exchange+",routingKey:"+routingKey;
//            mqPersistenceSupport.mqProducedErrorLogPersist(ProviderRealm.PHO,"普惠系统MQ消息发送异常",exchange,routingKey,messageBody,exDesc);
//        } catch (Exception e) {
//            log.error("MQ错误日志持久化异常",e);
//        }
//    }
//}
