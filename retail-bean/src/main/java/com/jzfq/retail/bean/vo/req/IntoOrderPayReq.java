package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: IntoOrderPayReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月19日 13:51
 * @Description: 小程序-客户-进入支付入参
 */
@Setter
@Getter
@ToString
public class IntoOrderPayReq implements Serializable {


    //订单号
    @NotBlank(message = "订单号不可为空")
    private String orderSn;

    //月供本金
//    @NotNull(message = "月供本金不可为空")
//    @Min(value = 0, message = "月供本金不能小于0")
//    private BigDecimal monthPrincipal;

    /**
     * 经度 lng
     */
    @NotNull(message = "经度不可为空")
    private Double lng;

    /**
     * 纬度 lat
     */
    @NotNull(message = "纬度不可为空")
    private Double lat;
}
