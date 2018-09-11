//package com.jzfq.retail.core.messaging.consumer;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jzfq.retail.common.exception.MQProcessingException;
//import com.jzfq.retail.common.util.XStreamUtil;
//import com.jzfq.retail.core.messaging.support.base.MQConstants;
//import com.jzfq.retail.core.messaging.support.base.MQPersistenceSupport;
//import com.jzfq.retail.core.messaging.support.base.ResDetail;
//import com.jzfq.retail.core.messaging.support.lw.LwConsumerCondition;
//import com.jzfq.retail.core.messaging.support.lw.LwMessageProcessor;
//import com.jzfq.retail.core.messaging.util.ApiDevUtils;
//import com.jzfq.retail.core.messaging.util.CallStatus;
//import com.jzfq.retail.core.messaging.util.ProviderRealm;
//import com.jzfq.retail.core.messaging.vo.base.MQMessage;
//import com.jzfq.retail.core.service.SystemLogSupport;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author lagon
// * @time 2017/10/19 11:01
// * @description 普惠运营MQ消费者
// */
//@Slf4j
////@Component
////@Conditional(LwConsumerCondition.class)
//public class PhoLwMessageConsumer {
//
//    //消息处理器容器
//    private static final Map<String,LwMessageProcessor> messageProcessorContainer=new ConcurrentHashMap<String,LwMessageProcessor>();
//
//    @Autowired
//    private MQPersistenceSupport mqPersistenceSupport;
//
//    @Autowired
//    private SystemLogSupport systemServiceSupport;
//
//    @Value("${rabbitmq.lw.address}")
//    private String serverAddress;
//
//    @Value("${rabbitmq.lw.trans.exchange.name}")
//    private String transExchange;
//
//    @Autowired
//    public void setMessageProcessors(LwMessageProcessor[] messageProcessors){
//        for(LwMessageProcessor messageProcessor:messageProcessors){
//            Set<String> processedRoutingKeys = messageProcessor.getProcessedRoutingKeys();
//            if(CollectionUtils.isNotEmpty(processedRoutingKeys)){
//                for(String routingKey:processedRoutingKeys){
//                    messageProcessorContainer.put(routingKey,messageProcessor);
//                }
//            }
//        }
//    }
//
//    @RabbitListener(queues = {"${rabbitmq.lw.trans.req.queue.name}"}, containerFactory = "lwRabbitListenerContainerFactory")
//    public void onMessage(Message message, Channel channel) throws Exception {
//        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
//        log.info("received message routingkey:{}",receivedRoutingKey);
//        LwMessageProcessor messageProcessor=messageProcessorContainer.get(receivedRoutingKey);
//        if(messageProcessor!=null){
//            log.info("message processor:{}",messageProcessor.getClass().getCanonicalName());
//            String messageBody=null;
//            try {
//                messageBody = new String(message.getBody(), MQConstants.DEFAULT_CHARSET);
//                onMessageBySpecificProcessor(message.getMessageProperties(),messageBody,channel,messageProcessor);
//            } catch (Exception e){
//                //处理消息时发生异常，记录MQ错误日志，并确认完成消费
//                log.error("message consume exception",e);
//                String scene=null;
//                if(e instanceof MQProcessingException){
//                    scene="普惠系统MQ消息处理业务异常";
//                }else{
//                    scene="普惠系统MQ消息处理未知异常";
//                }
//                mqPersistenceSupport.mqConsumedErrorLogPersist(ProviderRealm.PHO,scene,transExchange,receivedRoutingKey,messageBody, ApiDevUtils.getExceptionTrace(e));
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            } finally {
//                log.info("receive message from rabbitmq:{}", messageBody);
//            }
//        }else{
//            log.error("routing key not be found matching message processor,routing key:{}",receivedRoutingKey);
//            mqPersistenceSupport.mqConsumedErrorLogPersist(ProviderRealm.PHO,"普惠系统MQ消息routingKey无法映射异常",transExchange,receivedRoutingKey,null,"routing key not be found matching message processor,routing key:"+receivedRoutingKey);
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        }
//    }
//
//    //针对特定消息处理器处理消息
//    private void onMessageBySpecificProcessor(MessageProperties messageProperties, String messageBody, Channel channel, LwMessageProcessor messageProcessor) throws Exception {
//        String dtf = messageProcessor.getDataTransferFormat();
//        Class clazz = messageProcessor.getTClass();
//        log.info("processor handling message type:{}",clazz);
//        Object messageBean = null;
//        if (MQConstants.DATA_TRANSFER_FORMAT_JSON.equals(dtf)) {
//            messageBean = JSONObject.parseObject(messageBody, clazz);
//        } else if (MQConstants.DATA_TRANSFER_FORMAT_XML.equals(dtf)) {
//            messageBean = XStreamUtil.fromXml(messageBody,clazz);
//        }
//        //记录日志
//        MQMessage mqMessage=(MQMessage) messageBean;
//        String serverUri="exchange："+transExchange+"；routingKey："+messageProperties.getReceivedRoutingKey();
//        String title="普惠系统>>MQ服务>>"+mqMessage.getMqApiIdentifier().getName();
//        String bizId=mqMessage.getLoanCode();
//        String serialNo=mqMessage.getEcho();
//        systemServiceSupport.persistAccessLog(ProviderRealm.PHO,serverAddress,serverUri,title,bizId,serialNo, CallStatus.PROCESSING,messageBody,null,null);
//        ResDetail resDetail = messageProcessor.process(messageProperties, messageBean);
//        if (resDetail.isSuccess()) {
//            log.info("process message success");
//            channel.basicAck(messageProperties.getDeliveryTag(), false);
//        } else {
//            log.error("process message failure,redeliver message");
//            channel.basicNack(messageProperties.getDeliveryTag(), false, true);
//        }
//    }
//
//}
