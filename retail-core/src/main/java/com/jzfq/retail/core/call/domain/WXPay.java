package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Title: WXPay
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月08日 17:00
 * @Description: 微信小程序或公众号支付 请求参数封装
 */
@Getter
@Setter
@ToString
public class WXPay {
    /**
     * 订单号 必填
     */
    private String orderId;

    /**
     * 商品名称 不参与签名 必填
     */
    private String tradeName;

    /**
     * 支付金额 单位分 必填
     */
    private String amount;

    /**
     * 用户id 必填
     */
    private String customerId;

    /**
     * 支付类型 pay(支付首付或全款) repay(还款) 必填
     */
    private String payType;

    /**
     * 支付类型为repay时必传 还款期数  非必填
     */
    private String period;

    /**
     * 支付类型为pay时 必传  D(首付) F(全款) 非必填
     */
    private String payGoal;

    /**
     * 应扣减信用额度 单位元 非必填
     */
    private String deductCreditAmount;

    /**
     * 应用编码 shangcheng(桔子分期) baitiao(车主白条) 必填
     */
    private String application;

    /**
     * 支付来源 ios(IOS) h5(H5) gzh(公众号) xcx(小程序) android（安卓） 必填
     */
    private String source;

    /**
     * 小程序或公众号的app_id 非必填
     */
    private String appId;

    /**
     * 小程序或公众号的openId  非必填
     */
    private String openId;

    /**
     * 签名 必填
     */
    private String sign;

    /**
     * swiftpassWx 不参与签名 必填
     */
    private String payChannel;


}
