package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Title: IntoConfirmOrderReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月19日 11:47
 * @Description: 小程序-客户-进入确认订单接口入参
 */
@Getter
@Setter
@ToString
public class IntoConfirmOrderReq implements Serializable {

    //订单号
    @NotBlank(message = "订单号不可为空")
    private String orderSn;

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
