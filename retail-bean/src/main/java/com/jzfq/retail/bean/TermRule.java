package com.jzfq.retail.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Setter
@Getter
public class TermRule {
    // 期数
    private Integer term;
    // 默认费率
    private Double defaultRate;
    // 折扣
    private BigDecimal discount;
    // 折扣费率
    private BigDecimal afterDiscountRate;
    // 商户扣点
    private Double settlePoint;

    public TermRule() {}

    public TermRule(Integer term,Double defaultRate,BigDecimal discount,BigDecimal afterDiscountRate,Double settlePoint) {
        this.term = term;
        this.defaultRate = defaultRate;
        this.discount = discount;
        this.afterDiscountRate = afterDiscountRate;
        this.settlePoint = settlePoint;
    }
}
