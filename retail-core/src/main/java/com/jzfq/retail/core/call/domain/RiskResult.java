package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: BankCardResult
 * @description:
 * @company: 北京桔子分期电子商务有限公司  风控返回
 * @author: Liu Wei
 * @date: 2018/7/10 14:31
 */
@Setter
@Getter
@ToString
public class RiskResult {
    private String code;//状态码  1通过，2不通过
    private InnerData data;//状态描述
    private String msg;//信息
    private String token;//token
    private String traceID;//

    @Setter
    @Getter
    @ToString
    public static class InnerData {
        Integer fore; //提示状态码
        String fore_name; //提示语
        String date;
        String result;
        String result_name;
    }

}
