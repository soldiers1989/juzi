package com.jzfq.retail.core.messaging.vo.lw;

import com.jzfq.retail.core.messaging.support.base.MQApiIdentifier;
import com.jzfq.retail.core.messaging.vo.base.MQReqMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/10/19 11:41
 * @description 贷后订单状态通知请求实体
 */
@Getter
@Setter
@ToString
public class RepaymentStatusInformReq extends MQReqMessage<RepaymentStatusInformReqBody> {

    public RepaymentStatusInformReq(){
        mqApiIdentifier= MQApiIdentifier.LOAN_STATUS_INFORM;
        apiIdentifier= MQApiIdentifier.LOAN_STATUS_INFORM.getMark();
    }

}
