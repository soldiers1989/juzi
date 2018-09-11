package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: BankCardResult
 * @description:
 * @company: 北京桔子分期电子商务有限公司  获取个人绑卡信息 - 请求丁浩 核心支付接口返回
 * @author: Liu Wei
 * @date: 2018/7/10 14:31
 */
@Setter
@Getter
@ToString
public class BankCardResult {
    private String cardNo;//银行卡号
    private String phoneNumber;//手机号
    private String certNo;//身份证
    private String customerName;//客户姓名
    private String picture;//客户头像
    private String backImg;//被几个呢图片
    private String customerId;//客户id
    private String id;//
    private String bankCode;
    private String bankName;//银行名称
    private String isDefault;//
    private String isSupport;//
    private String paySuppot;

}
