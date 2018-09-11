package com.jzfq.retail.core.api.service;


import com.alibaba.fastjson.JSONObject;
import com.jzfq.retail.bean.domain.ProductGoods;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.OrderDeliveryInfoRes;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import org.springframework.http.ResponseEntity;

import java.util.List;
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
public interface SellerBusiService {

    /**
     * 创建订单
     *
     * @param req
     * @return
     */
    String createOrder(CreateOrderReq req) throws TouchCodeException;

    /**
     * 通过商户店铺编码获取商户信息
     *
     * @param sellerLogin
     * @return
     */
    Map<String, Object> findSellerInfo(String sellerLogin) throws TouchCodeException;

    /**
     * 查询商户关联品类、品牌、商品
     *
     * @param sellerId
     * @return
     */
    List<Map<String, Object>> findCateBrandInfo(Integer sellerId) throws TouchCodeException;

    /**
     * 获取简单的订单信息通过orderSn
     *
     * @param orderSn
     * @return
     * @throws TouchCodeException
     */
    Map<String, Object> getSimpleOrderInfoByOrderSn(String orderSn) throws TouchCodeException;

    /**
     * 获取订单二维码
     *
     * @param orderSn
     * @return 返回二维码地址
     */
    String getOrderQRCode(String orderSn) throws TouchCodeException;

    /**
     * 通过当前登录用户和筛选条件获取订单列表
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> findOrderListBySeller(Integer page, Integer pageSize, OrderSearchReq search, String seller) throws TouchCodeException;

    /**
     * 根据订单ID获取订单详情
     *
     * @param id
     * @return
     */
    Map<String, Object> findOderInfoById(Integer id) throws TouchCodeException;


    /**
     * 获取订单交货详情
     * @param orderId
     * @return
     * @throws TouchCodeException
     */
    OrderDeliveryInfoRes getOrderDeliveryInfo(Integer orderId) throws TouchCodeException;

    /**
     * 订单交货提交
     *
     * @param req
     */
    void orderDeliveryCommit(OrderDeliveryCommitReq req) throws TouchCodeException;

    /**
     * 查询店铺所有商品列表。
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> findSellerProductList(Integer page, Integer pageSize, SellerGoodsSearch search) throws TouchCodeException;

    /**
     * 获取订单还款详情
     *
     * @param orderId
     * @param indays
     * @param state
     * @param page
     * @param pageSize
     * @return
     */
    ListResultRes<Map<String, Object>> getOrderRepayments(Integer orderId, Integer indays, Integer state, Integer page, Integer pageSize) throws TouchCodeException;

    /**
     * 通过norm_name查询SKU
     * @param search
     * @return
     */
    ProductGoods getSkuByNormName(GetSkuByNormNameSearchReq search);

    /**
     * 根据商户ID查询商户关联品类及商品和属性信息
     * @param sellerId
     * @param productId
     * @return
     */
    JSONObject getProductCateAndSkuInfo(Integer sellerId, Integer productId);
}
