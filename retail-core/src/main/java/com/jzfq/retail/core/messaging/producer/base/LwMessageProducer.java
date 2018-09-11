//package com.jzfq.retail.core.messaging.producer.base;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jzfq.retail.common.util.XStreamUtil;
//import com.jzfq.retail.core.messaging.support.base.MQConstants;
//import com.jzfq.retail.core.messaging.util.CallStatus;
//import com.jzfq.retail.core.messaging.vo.base.MQResMessage;
//import com.jzfq.retail.core.messaging.vo.base.MQTransStatus;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//
///**
// * @author lagon
// * @time 2017/10/19 11:29
// * @description 普惠运营MQ生产者
// */
//@Slf4j
//public abstract class LwMessageProducer<T> {
//
//    @Value("${rabbitmq.lw.trans.exchange.name}")
//    protected String transExchange;
//
//    @Autowired(required = false)
//    @Qualifier("lwRabbitTemplate")
//    protected AmqpTemplate lwRabbitTemplate;
//
//    /**
//     * @param exchange     交换器名
//     * @param routingKey    映射key
//     * @param message       最终传输的字符串
//     */
//    protected final void publishMessage(String exchange, String routingKey, T message) {
//        String dtf = getDataTransferFormat();
//        String messageContent = "";
//        if (MQConstants.DATA_TRANSFER_FORMAT_JSON.equals(dtf)) {
//            messageContent = JSONObject.toJSONString(message,true);
//        } else if (MQConstants.DATA_TRANSFER_FORMAT_XML.equals(dtf)) {
//            messageContent = XStreamUtil.toXml(message);
//        }
//        lwRabbitTemplate.convertAndSend(exchange, routingKey, messageContent.getBytes());
//        log.info("send message to rabbitmq server,exchange:{},rotingKey:{},message content:{}",exchange,routingKey,messageContent);
//        // 变更日志状态和记录响应数据
//        MQResMessage resMessage=(MQResMessage) message;
//        CallStatus callStatus= MQTransStatus.SUCCESS.name().equals(resMessage.getResponseCode())?CallStatus.SUCCESS:CallStatus.ERROR;
//        //systemServiceSupport.updateAccessLog(ProviderRealm.PHO,resMessage.getLoanCode(),resMessage.getEcho(),callStatus,messageContent,null);
//    }
//
//    //获取数据传输格式，支持json和xml格式
//    protected abstract String getDataTransferFormat();
//
//}
