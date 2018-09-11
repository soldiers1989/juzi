package com.jzfq.retail.bean.vo.req;

import lombok.Data;

/**
 * 商户账户提现回调参数
 */
@Data
public class WithdrawCashCallbackReq {
    /**
     * 提现金额
     */
    private Double amount;
    /**
     * 提现订单号
     */
    private String withdrawId;
    /**
     * 商户编码
     */
    private String sellerCode;
    /**
     * 提现结果 1 成功， 2 失败
     */
    private Integer state;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 支付流水号
     */
    private String transNum;
}
