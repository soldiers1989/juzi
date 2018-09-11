package com.jzfq.retail.common.exception.base;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author lagon
 * @time 2017/7/12 14:53
 * @description 异常体系枚举
 */
public enum ExceptionRealm {

	BASIC("basic","基础模块异常"), //基础模块
    INTERNAL_PROCESSING("internal_processing","内部处理异常"),
    MQ_PROCESSING("mq_processing","MQ处理异常"),
    CLAIM_INTERACTION("claim_interaction","债匹系统交互异常"),
    LAS_INTERACTION("las_interaction","贷后系统交互异常"),
    OLS_INTERACTION("ols_interaction","线上借款端系统交互异常")
    ;
	
    private String mark;
    private String desc;
    
    // 构造函数，枚举类型只能为私有
    private ExceptionRealm(String mark, String desc) {
        this.mark=mark;
        this.desc=desc;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
