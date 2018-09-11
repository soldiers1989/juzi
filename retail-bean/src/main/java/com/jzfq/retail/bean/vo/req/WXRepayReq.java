package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: WXRepayReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月09日 15:17
 * @Description: 微信小程序还款入参
 */
@Getter
@Setter
@ToString
public class WXRepayReq implements Serializable {

    /**
     * 微信code
     */
    @NotBlank(message = "微信CODE不可为空")
    private String wxCode;
    /**
     * 还款订单编号
     */
    @NotBlank(message = "还款订单编号不可为空")
    private String orderSn;
    /**
     * 还款期数
     */
    @NotNull(message = "还款期数不可为空")
    private Integer period;

}
