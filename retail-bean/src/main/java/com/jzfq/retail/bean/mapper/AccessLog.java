package com.jzfq.retail.bean.mapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lagon
 * @time 2017/10/25 16:16
 * @description 访问日志实体类
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccessLog extends BasePO {

    private String provider;//服务提供方
    private String address;//服务器地址
    private String uri;//服务URI
    private String title;//标题
    private String bizId;//业务标识
    private String serialNo;//业务流水号
    private Integer callStatus;//调用状态：0，处理中；1，成功；2，失败
    private String reqData;//请求数据
    private String resData;//响应数据
    private String exception;//异常描述

}
