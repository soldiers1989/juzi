package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.vo.req.OrderListReq;
import com.jzfq.retail.bean.vo.req.OrderSearchReq;
import com.jzfq.retail.bean.vo.res.TakeCodeOrderRes;
import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:01
 * @Description:
 */
public interface OrdersBaseManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<OrdersBase> findList(OrdersBase record);

    /**
     * 根据商户id修改订单状态  冻结商户操作
     *
     * @param sellerId
     */
    void modifyBySellerId(Integer sellerId);

    /**
     * TODO：缺少注释
     *
     * @param order
     * @return
     */
    List<Map<String, Object>> findOrderAndUserList(OrdersBase order);

    /**
     * 通过orderSn查询订单
     *
     * @param orderSN
     * @return
     */
    OrdersBase findEntityByOrderSN(String orderSN);

    /**
     * 根据取货码查询订单
     *
     * @param takeCode
     * @return
     */
    TakeCodeOrderRes findOrderByTackCode(@Param("takeCode") String takeCode, @Param("sellerId") Integer sellerId);

    /**
     * 通过订单编号获取生成二维码图片得订单信息
     *
     * @param orderSn
     * @return
     */
    Map<String, Object> findByOrderSn(String orderSn);

    /**
     * 通过商户登录的编码获取此商户所有订单
     *
     * @return
     */
    Page<Map<String, Object>> findOrderListBySeller(@Param("search") OrderSearchReq search, @Param("seller") String seller);

    /**
     * 通过商户登录的编码获取此商户所有订单
     *
     * @return
     */
    Page<Map<String, Object>> findOrderListByMember(@Param("status") Integer[] status, @Param("member") Integer member);

    /**
     * 缺少注释
     *
     * @param ordersBase
     */
    void updateCreditFlagById(OrdersBase ordersBase);

    /**
     * TODO：缺少注释
     *
     * @param ordersBase
     */
    void updateRiskTokenById(OrdersBase ordersBase);

    /**
     * TODO：缺少注释
     *
     * @param status
     * @param ordersIds
     */
    void updateStatusByIds(@Param("status") Integer status, @Param("ordersIds") List<Integer> ordersIds);

    /**
     * TODO：缺少注释
     *
     * @param search
     * @return
     */
    Page<Map<String, Object>> findOrderBaseRelatedInfoList(OrderListReq search);

    /**
     * 查询数据库是否存在
     *
     * @param takeCode
     * @return
     */
    int countNonDeliveryTakeCodeRepeat(String takeCode);

    /**
     * 根据订单号更新状态
     */
    void modifyOrderStateByOrderSn(OrdersBase ordersBase);

    /**
     * 统计当日支付成功的顾客数量
     */
    Integer countMember();

    /**
     * 统计当日支付成功的订单总金额
     */
    BigDecimal sumOrderMoney();

    /**
     * 统计当日订单进件
     */
    Integer countEntryOrder();
}
