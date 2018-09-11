package com.jzfq.retail.core.call.domain;

import lombok.Data;

/**
 * 商户账户提现
 */
@Data
public class WithdrawCash {
    /**
     * 提现金额
     */
    private Double amount;
    /**
     * 应用
     */
    private String application;
    /**
     * 商户编码
     */
    private String businessId;
    /**
     * 出款的商户号
     */
    private String merchant;
    /**
     * 提现业务流水号
     */
    private String withdrawId;
}
