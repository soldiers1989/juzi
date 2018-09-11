package com.jzfq.retail.core.messaging.vo.base;

import com.jzfq.retail.common.util.date.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/5/23 14:40
 * @description MQ交易类公共响应基础报文
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MQResMessage extends MQMessage {

    //SUCCESS为成功，PROCESSING处理中，ERROR失败
    protected String responseCode = MQTransStatus.SUCCESS.name();
    //状态描述
    protected String responseMessage = MQTransStatus.SUCCESS.getDesc();
    //响应时间
    protected String responseTime = DateUtil.getStandardCurrentTime();
    //响应实体
    protected MQResponseBody responseBody;

}
