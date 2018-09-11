package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 商户账户提现回调参数
 */
@Getter
@Setter
@ToString
public class WithdrawCashReq implements Serializable {
    /**
     * 提现金额
     */
    @NotBlank(message = "提现金额不能为空")
    private String amount;
    /**
     * 商户编码
     */
    @NotBlank(message = "商户编码不能为空")
    private String sellerCode;

    @NotBlank(message = "商户编码不能为空")
    private String sellerName;
    /**
     * 出款的商户号
     */
    @NotBlank(message = "出款的商户号不能为空")
    private String merchant;
}
