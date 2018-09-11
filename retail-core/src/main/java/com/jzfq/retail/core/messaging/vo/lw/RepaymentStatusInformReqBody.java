package com.jzfq.retail.core.messaging.vo.lw;

import com.alibaba.fastjson.annotation.JSONField;
import com.jzfq.retail.core.messaging.vo.base.MQRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lagon
 * @time 2017/10/18 16:25
 * @description 贷后订单状态通知请求主体
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RepaymentStatusInformReqBody extends MQRequestBody {

    private Integer period;//期数
    private String status;//状态：1表还款成功，2表还款失败
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date repayDate;//还款时间，格式为：yyyy-MM-dd HH:mm:ss
    private BigDecimal actualRepaymentTotal;//还款金额
    private String desc;//描述信息

}
