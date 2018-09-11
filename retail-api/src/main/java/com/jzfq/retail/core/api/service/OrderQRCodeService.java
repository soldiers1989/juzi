package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.OrderQRCode;
import com.jzfq.retail.common.enmu.OrderQRCodeState;

import java.util.Map;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月07日 16:29
 * @Description: 二维码服务操作接口
 */
public interface OrderQRCodeService {


    /**
     * 通过订单获取有效二维码对象
     * * 如果返回值为空则默认为失效
     *
     * @param orderSn 订单编号
     * @return
     */
    OrderQRCode getOrderQRCodeByOrderSn(String orderSn);

    /**
     * 设定二维码状态是否可用
     *
     * @param id
     * @param state 状态
     */
    void setOrderQRCodeStateByOrderSn(Integer id, OrderQRCodeState state);

    /**
     * 生成二维码入库操作
     *
     * @param orderSn
     * @param QRCUrl
     */
    void insetOrderQRCode(String orderSn, String QRCUrl);

}
