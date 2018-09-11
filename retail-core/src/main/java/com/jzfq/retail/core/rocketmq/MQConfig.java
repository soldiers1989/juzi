package com.jzfq.retail.core.rocketmq;

import com.aliyun.openservices.ons.api.PropertyValueConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class MQConfig {

    @Value("${rocketmq.accessKey}")
    private String AccessKey;
    @Value("${rocketmq.secretKey}")
    private String SecretKey;
    @Value("${rocketmq.address}")
    private String address;
    private String MessageModel = PropertyValueConst.CLUSTERING;
    private String SendMsgTimeoutMillis = "3000";
    private String ConsumeThreadNums = "10";

    public String getAccessKey() {
        return AccessKey;
    }

    public void setAccessKey(String accessKey) {
        AccessKey = accessKey;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessageModel() {
        return MessageModel;
    }

    public void setMessageModel(String messageModel) {
        MessageModel = messageModel;
    }

    public String getSendMsgTimeoutMillis() {
        return SendMsgTimeoutMillis;
    }

    public void setSendMsgTimeoutMillis(String sendMsgTimeoutMillis) {
        SendMsgTimeoutMillis = sendMsgTimeoutMillis;
    }

    public String getConsumeThreadNums() {
        return ConsumeThreadNums;
    }

    public void setConsumeThreadNums(String consumeThreadNums) {
        ConsumeThreadNums = consumeThreadNums;
    }

}
