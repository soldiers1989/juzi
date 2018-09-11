//package com.jzfq.retail.core.messaging.producer.lw;
//
//import com.jzfq.retail.core.messaging.producer.base.LwMessageProducer;
//import com.jzfq.retail.core.messaging.support.base.MQConstants;
//import com.jzfq.retail.core.messaging.vo.lw.LwPushReq;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * @author liuwei
// * @time 2017/10/19 11:20
// * @description 普惠运营>>MQ服务>>推送测试消息生产者
// */
//@Slf4j
////@Component
//public class PushLwMessageProducer extends LwMessageProducer<LwPushReq> {
//
//    @Value("${rabbitmq.lw.trans.req.queue.binding.routingkey}")
//    private String routingkey;
//
//    public void sendMessage(LwPushReq transMessage) {
//        log.info("测试推送MQ请求处理完成，返回响应");
//        publishMessage(transExchange, routingkey, transMessage);
//    }
//
//    public void sendMessage(String exchange, String routingKey, LwPushReq lwPushRes) {
//        publishMessage(exchange, routingKey, lwPushRes);
//    }
//
//    @Override
//    protected String getDataTransferFormat() {
//        return MQConstants.DATA_TRANSFER_FORMAT_JSON;
//    }
//
//}
