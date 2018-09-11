package com.jzfq.retail.bean.vo.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: OrdersSettleRes
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月14日 16:10
 * @Description: 商户结算返回
 */
@Setter
@Getter
@ToString
public class OrdersSettleRes implements Serializable {


    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 下单时间
     */
    private Date confirmTime;
    /**
     * 结算状态 1、待结算 2、已结算 3、拒绝 4、未到结算日 10、待对账；20、待核账；30、待放款；40、已放款；50、取消结算
     */
    private Integer state;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品规格
     */
    private String productNormName;
    /**
     * 订单商品数量
     */
    private Integer orderProductNumber;
    /**
     * 优惠总金额
     */
    private BigDecimal moneyDiscount;
    /**
     * 订单总金额
     */
    private BigDecimal moneyOrder;
    /**
     * 首付金额
     */
    private BigDecimal downpaymentAmount;
    /**
     * 品类名称
     */
    private String productCateName;
    /***
     *品牌 名称
     */
    private String productBrandName;
    /**
     * 买家名称
     */
    private String memberName;
    /**
     * 账期
     */
    private Integer accountPeriod;
    /**
     * 商户名称
     */
    private String sellerName;
    /**
     * 商家扣点
     */
    private BigDecimal sellerSettlePoint;

    //TODO:缺少计算金额

    /**
     * 商户公司名称
     */
    private String sellerCompanyName;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 银行卡号
     */
    private String bankCode;
    /**
     * 订单备注
     */
    private String orderRemark;
    /**
     * 订单类型 1、扫码店订单，2、便利店订单
     */
    private Integer orderType;

    //TODO:缺少配货时间

    /**
     * 订单发货时间
     */
    private Date orderDeliverTime;
    /**
     * 确认收货时间
     */
    private Date orderCodconfirmTime;
    /**
     * 成本价
     */
    private BigDecimal productCostPrice;
    /**
     * 资方id
     */
    private Integer orderCapitalId;

    //TODO:缺少优惠劵

    /**
     * 订单月供
     */
    private String orderMonthlyAmount;

    //TODO:缺少城市
    //TODO:缺少是否免息

    /**
     * 实际结算时间
     */
    private Date settleRealTime;
    /**
     * 月利率
     */
    private Double monthRatio;

    //TODO:缺少分期金额

    /**
     * 收货人
     */
    private String consignee;
    /**
     * 收货城市
     */
    private String receivingCity;

    //TODO:缺少可打款时间

    /**
     * 期数
     */
    private Integer term;
    /**
     * 月服务费
     */
    private BigDecimal monthlyService;


}
