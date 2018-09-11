package com.jzfq.retail.core.messaging.vo.base;

import com.jzfq.retail.common.util.date.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/7/5 18:36
 * @description MQ交易类公共请求基础报文
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MQReqMessage<T extends MQRequestBody> extends MQMessage {

    //请求时间
    protected String requestTime= DateUtil.getStandardCurrentTime();
    //请求体
    protected T requestBody;

}
