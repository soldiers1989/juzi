package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ToString
@Setter
@Getter
public class OrderListReq implements Serializable {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 店铺名称
     */
    private String sellerName;

    /**
     * 买家姓名
     */
    private String buyerName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 订单状态
     */
    private Integer orderState;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品品类
     */
    private String cateName;

    /**
     * 商品品牌
     */
    private String brandName;

    /**
     * 城市
     */
    private String showCity;

    /**
     * 下单渠道
     */
    private Integer source;

    /**
     *下单日期
     */
    private String confirmTimeBegin;
    private String confirmTimeEnd;

    /**
     * 发货日期
     */
    private String deliverTimeBegin;
    private String deliverTimeEnd;


}