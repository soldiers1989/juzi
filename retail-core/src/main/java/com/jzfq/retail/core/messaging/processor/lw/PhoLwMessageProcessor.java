//package com.jzfq.retail.core.messaging.processor.lw;
//
//import com.jzfq.retail.common.util.date.DateUtil;
//import com.jzfq.retail.core.messaging.support.base.MQConstants;
//import com.jzfq.retail.core.messaging.support.base.ResDetail;
//import com.jzfq.retail.core.messaging.support.lw.LwConsumerCondition;
//import com.jzfq.retail.core.messaging.support.lw.LwMessageProcessor;
//import com.jzfq.retail.core.messaging.vo.lw.RepaymentStatusInformReq;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Slf4j
////@Component
////@Conditional(LwConsumerCondition.class)
//public class PhoLwMessageProcessor implements LwMessageProcessor<RepaymentStatusInformReq> {
//    @Value("${rabbitmq.lw.trans.res.routingkey.name}")
//    private String routingKey;
//
//    //@Autowired
//    //private UserService userService;
//
//    @Override
//    public String getDataTransferFormat() {
//        return MQConstants.DATA_TRANSFER_FORMAT_JSON;
//    }
//
//    @Override
//    public Class<RepaymentStatusInformReq> getTClass() {
//        return RepaymentStatusInformReq.class;
//    }
//
//    @Override
//    public Set<String> getProcessedRoutingKeys() {
//        return new HashSet<String>() {
//            {
//                add(routingKey);
//            }
//        };
//    }
//
//    @Override
//    public ResDetail process(MessageProperties messageProperties, RepaymentStatusInformReq message) {
//        log.info("开始处理还款状态通知MQ消息，时间：{}", DateUtil.getStandardCurrentTime());
//        log.info("message:"+message);
//        log.info("messageProperties:"+messageProperties);
////        userService.processMessage(message);
//        log.info("完成处理还款状态通知MQ消息，时间：{}", DateUtil.getStandardCurrentTime());
//        return new ResDetail(true, "success");
//    }
//}
