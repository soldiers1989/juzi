package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @title: CapitalBackReq
 * @description:
 * @company: 北京桔子分期电子商务有限公司 资匹匹配回调参数
 * @author: Liu Wei
 * @date: 2018/7/11 10:21
 */
@ToString
@Getter
@Setter
public class CapitalBackReq implements Serializable {
    private Integer capitalCode;//资方代码 资匹id
    private String capitalOrderId;//资方返回单号
    private String contractNum;//进件订单号
    private String orderId;//订单号
}
