package com.jzfq.retail.bean.mapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/6/27 16:44
 * @description MQ错误日志实体类
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MQErrorLog extends BasePO {

    private Integer occasion;//错误产生时机：0表未知，1表生产时产生错误，2表消费时产生错误
    private String provider;//服务提供方
    private String scene;//场景
    private String exchange;//交换器
    private String routingKey;//路由key
    private String message;//报文
    private String exception;//异常描述

}
