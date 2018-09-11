package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;

import java.util.Map;

/**
 * import java.util.Map;
 * <p>
 * /**
 *
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月6日 14:43
 * @Description: 商户端小程序service接口
 */
public interface ShopScanService {


    /**
     * 小程序店中店扫码获取商品详情
     *
     * @param req
     * @return
     */
    Map<String, Object> queryProductInfo(ScanProductReq req) throws Exception;

    /**
     * 校验sku是否有库存
     *
     * @param req
     * @return
     */
    Map<String,Object> checkGoodsStock(GoodsStockReq req);

    /**
     * 校验用户是否注册
     * @param req
     * @return
     */
    void checkRegister(CheckRegisterReq req);

    /**
     * 生成订单
     * @param req
     * @return
     */
    Map<String, Object> generateOrder(GenerateOrderReq req) throws TouchCodeException;

    /**
     * 微信小程序还款
     *
     * @param wxCode  微信code
     * @param orderSn 订单编号
     * @param period  还款期数
     * @return
     * @throws TouchCodeException
     */
    String wxRepaymentPay(String wxCode, String orderSn, Integer period) throws TouchCodeException;
}
