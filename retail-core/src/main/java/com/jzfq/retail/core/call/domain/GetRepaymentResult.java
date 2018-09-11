package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Title: GetRepaymentResult
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月10日 13:45
 * @Description:
 */
@Getter
@Setter
@ToString
public class GetRepaymentResult {
    /**
     * 贷款金额
     */
    private BigDecimal amount;
    /**
     * 身份证号
     */
    private String certNo;
    /**
     * 用户id
     */
    private String customerId;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 逾期天数
     */
    private Integer overDueDay;
    /**
     * 逾期金额
     */
    private BigDecimal overDueFee;
    /**
     * 本金
     */
    private BigDecimal principal;
    /**
     * 账单状态 0未还，1已还，5取消，10逾期
     */
    private String state;
    /**
     * 商品名称
     */
    private String title;
}
