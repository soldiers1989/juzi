package com.jzfq.retail.bean.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ToString
@Setter
@Getter
public class OrdersBaseRelatedInfo implements Serializable {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 店铺
     */
    private String sellerName;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单状态
     */
    private Integer orderState;

    /**
     * 订单总金额
     */
    private BigDecimal moneyOrder;

    /**
     * 首付
     */
    private BigDecimal downpaymentAmount;

    /**
     * 月供
     */
    private BigDecimal monthlyAmount;

    /**
     *下单时间
     */
    private Date confirmTime;

    /**
     * 发贷时间
     */
    private Date deliverTime;

    /**
     * 配货时间
     */
    private Date pickingTime;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 物流公司
     */
    private String logisticsName;

    /**
     * 物流单号
     */
    private String logisticsNumber;

    /**
     * 关闭时间
     */
    private Date finishTime;

    /**
     * 下单渠道
     */
    private Integer source;

    /**
     * 买家姓名
     */
    private String buyerName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 注册邀请码
     */
    private String registeCode;

    /**
     * 下单邀请码
     */
    private String invitationCode;

    /**
     * 用户类型
     */
    private String userStatus;

    /**
     * 城市
     */
    private String showCity;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 规格/属性
     */
    private String specInfo;

    /**
     * 单价
     */
    private String moneyPrice;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 订单交易期数
     */
    private Integer term;

    /**
     * 商品品类
     */
    private String cateName;
}