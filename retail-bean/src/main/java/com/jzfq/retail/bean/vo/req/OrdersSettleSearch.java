package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: OrdersSettleSearch
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月14日 16:10
 * @Description: 商户结算返回
 */
@Setter
@Getter
@ToString
public class OrdersSettleSearch implements Serializable {

    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 商户名称
     */
    private String sellerName;
    /**
     * 结算状态
     */
    private Integer state;
    /**
     * 发货开始时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String orderDeliverTimeStart;
    /**
     * 发货结束时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String orderDeliverTimeEnd;
}
