package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.OrderImage;
import com.jzfq.retail.common.enmu.OrderImageType;

import java.util.List;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月7日 17:43
 * @Description: 订单影像信息接口
 */
public interface OrderImageService {

    OrderImage getOrderImages(String orderSn);

    byte[] decryptImage(byte[] encryptData);

    /**
     * 添加订单图片信息
     *
     * @param orderId
     * @param orderSn
     * @param type
     * @param url
     * @param remark
     */
    void addOrderImage(Integer orderId, String orderSn, OrderImageType type, String url, String remark);
    /**
     * 通过订单编号和状态查询订单图片
     *
     * @param orderSn
     * @param state
     * @return
     */
    List<OrderImage> getOrderImagesByOrderSnAndState(String orderSn, Integer state);
}
