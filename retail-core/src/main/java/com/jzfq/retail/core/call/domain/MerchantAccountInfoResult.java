package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Title: MerchantAccountInfoResult
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月13日 10:05
 * @Description: 获取商户
 */
@Getter
@Setter
@ToString
public class MerchantAccountInfoResult {

    /**
     * 冻结金额
     */
    private BigDecimal consumeFrozenAmount;
    /**
     * 可以额度
     */
    private BigDecimal creditValidAmount;
}
