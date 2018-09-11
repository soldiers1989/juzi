package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Title: RepaymentCompleteReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月02日 17:31
 * @Description: TODO(用一句话描述该文件做什么)
 */
@Getter
@Setter
@ToString
public class RepaymentCompleteReq {
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 还款期数
     */
    private Integer period;
    /**
     * 是否完成  该参数值代表全部还款完成时候为true
     */
    private Boolean isFinished;
}
