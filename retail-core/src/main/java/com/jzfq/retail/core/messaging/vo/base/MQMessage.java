package com.jzfq.retail.core.messaging.vo.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.jzfq.retail.common.util.UUIDGenerator;
import com.jzfq.retail.core.messaging.support.base.MQApiIdentifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lagon
 * @time 2017/7/5 18:36
 * @description MQ公共基础报文
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MQMessage implements Serializable {

    //进件编号
    protected String loanCode;
    //API标识
    protected String apiIdentifier;
    //API标识枚举
    @JSONField(serialize = false)
    protected MQApiIdentifier mqApiIdentifier;
    //透传参数
    protected String echo = UUIDGenerator.getUUID();

}
