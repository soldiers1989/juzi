package com.jzfq.retail.core.messaging.vo.lw;

import com.jzfq.retail.core.messaging.support.base.MQApiIdentifier;
import com.jzfq.retail.core.messaging.vo.base.MQReqMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/10/19 11:41
 * @description 消息推送响应实体
 */
@Getter
@Setter
@ToString
public class LwPushReq extends MQReqMessage<LwPushReqBody> {
    public LwPushReq(){
        mqApiIdentifier= MQApiIdentifier.LW_MQ_TEST_INFORM;
        apiIdentifier= MQApiIdentifier.LW_MQ_TEST_INFORM.getMark();
    }
}
