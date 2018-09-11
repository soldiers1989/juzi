package com.jzfq.retail.core.rocketmq;

import java.io.UnsupportedEncodingException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;

@Slf4j
public class MQConsumer1 extends AbstractMQConsumer {

    @Override
    Boolean handleAction(Message message, ConsumeContext context) {
        if (log.isInfoEnabled()) {
            try {
                log.info(new String(message.getBody(), "utf-8"));
                // 处理成功返回true， 失败返回false
                return true;
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

}
