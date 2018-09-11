package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @title: OrderCheck
 * @description:
 * @company: 北京桔子分期电子商务有限公司  风控准入入参
 * @author: Liu Wei
 * @date: 2018/7/10 16:21
 */
@Setter
@Getter
@ToString
public class OrderCheck {
    private Integer orderId; // 订单号
    private String orderSn;
    private String gpsCity; //北京市 gps城市(必传)	string	@mock=北京
    private String cardAddress; //四川省大竹县庙坝镇街道739号  户籍详细地址(必传)	string	@mock=xxx街道xx小区xx单元xx楼
    private BigDecimal amount; //3,下单金额(交易必传)	number	@mock=100
    private String commodityChannel; //1, ,非必传 商户渠道
    private Integer rmsCustomerType; //1, 原信审的customerType(非必传)	number	@mock=1
    private String idCard; //513029198510273968 身份证号(必传)	string	@mock=15282219950321131X
    private String mobile; //15011259641 手机号(必传)	string	@mock=13088888888
    private String generalCity; //北京市 现居城市(必传)	string	@mock=北京
    private String version; //v2.0.6
    private Integer rmsProductType; //11, 原信审的ProductType(必传)	number	@mock=1
    private String generalCityCode; //110100 现居城市code(必传)	string	@mock=100000
    private String customerName; //杜鹃 客户姓名(必传)	string	@mock=孙悟空
    private Integer orderTime; //1, 当前城市下单次数	number	@mock=20
    private Integer clientType; //1, 设备类型 1.IOS；2.安卓；3.H5；4.PC；5.微信；6.微信小程序；(必传)	number	@mock=1
    private Integer financialProductId; //6, 金融产品 1.信用钱包；2.现金贷；3.单笔商户；4.小额现金(十露盘)；(必传);5.车险首	number	@mock=1
    private Integer customerId; //210001661, 用户ID(必传)	string	@mock=123321
    private String registCode; //V00025 注册f码(必传)	string	@mock=f13088888888
    private String gpsCityCode; //110000 gps城市code(必传)	string	@mock=100000
    private Integer operationType; //2,
    private String merchantType; //4 商户类型	string	@mock=3
    private String gpsAddress; //北京市朝阳区小营北路靠近和泰大厦(小营北路)  gps详细地址	string	@mock=xxx街道xx小区xx单元xx楼
    private Integer age; //32,年龄	number	@mock=25
    private Integer channelId; //2 渠道 1.桔子分期；2.车主白条；(必传)	number	@mock=1

}
