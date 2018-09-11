package com.jzfq.retail.bean.vo.res;

import com.jzfq.retail.bean.vo.req.OrderImageReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: OrderDeliveryInfoRes
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月28日 18:34
 * @Description:
 */
@Getter
@Setter
@ToString
public class OrderDeliveryInfoRes implements Serializable {
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 商品SPU
     */
    private String productName;
    /**
     * 商品标签
     */
    private String proLabel;
    /**
     * 规格信息
     */
    private String specInfo;
    /**
     * 订单图片
     */
    private List<OrderImageReq> orderImages;

}
