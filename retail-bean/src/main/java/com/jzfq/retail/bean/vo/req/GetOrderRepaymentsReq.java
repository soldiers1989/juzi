package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Title: GetOrderRepaymentsReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月26日 20:17
 * @Description:
 */
@Getter
@Setter
@ToString
public class GetOrderRepaymentsReq implements Serializable {


    /**
     * 订单id
     */
    @NotNull(message = "订单编号不可以为空")
    private Integer orderId;
    /**
     * 近N天待还 可以为空
     */
    @NotNull(message = "近N天待还不可以为空")
    private Integer indays;
    /**
     * 0未还和逾期；1已还 可以为空
     */
    @NotNull(message = "还款状态不可以为空")
    private Integer state;

}
