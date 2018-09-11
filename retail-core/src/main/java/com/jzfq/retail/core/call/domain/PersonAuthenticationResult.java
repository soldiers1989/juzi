package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @title: PersonAuthenticationResult
 * @description:
 * @company: 北京桔子分期电子商务有限公司  判断个人用户是否认证 返回
 * @author: Liu Wei
 * @date: 2018/7/10 14:48
 */
@Setter
@Getter
@ToString
public class PersonAuthenticationResult {

    private Date date;
    private BigDecimal validAmount;//剩余授信额度
    private Integer code;//0未认证 1认证审核中  2认证审核成功  3认证审核失败
    private String name;//认证描述
    private BigDecimal creditAmount;//总授信额度
    private Date applyDate;
    private Integer activated; // 1：已激活  2：未激活  3：冻结
}
