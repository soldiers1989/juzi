package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.vo.req.CapitalBackReq;
import com.jzfq.retail.bean.vo.req.OrderListReq;
import com.jzfq.retail.bean.vo.req.RiskCallbackReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:43
 * @Description: 订单接口
 */
public interface OrdersBaseService {

    /**
     * 返回所有列表
     *
     * @param search 查询条件
     * @return
     */
    List<OrdersBase> getAllList(OrdersBase search);

    /**
     * 分页条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页多少
     * @param search   筛选条件
     * @return
     */
    ListResultRes<OrdersBase> getList(Integer page, Integer pageSize, OrdersBase search);

    /**
     * 分页条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页多少
     * @param search   筛选条件
     * @return
     */
    ListResultRes<Map<String, Object>> getOrderBaseRelatedInfoList(Integer page, Integer pageSize, OrderListReq search);

    /**
     * 添加
     *
     * @param ordersBase
     */
    void create(OrdersBase ordersBase);

    /**
     * 修改
     *
     * @param ordersBase
     */
    void update(OrdersBase ordersBase);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询订单
     *
     * @param id
     * @return
     */
    OrdersBase getOrderById(Integer id);

    /**
     * 通过SELLERID修改订单
     * <p>
     * 通过订单编号获取订单基本信息
     *
     * @param orderSn
     * @return
     */
    OrdersBase getByOrderSn(String orderSn);

    /**
     * 通过SELLER_ID修改订单
     *
     * @param sellerId
     * @return
     */
    void modifyBySellerId(Integer sellerId);

    /**
     * 缺少注释
     *
     * @param order
     * @return
     */
    List<Map<String, Object>> findOrderAndUserList(OrdersBase order);

    /**
     * 订单完成调用
     *
     * @param orderSn
     * @param isFinished
     */
    void orderComplete(String orderSn, boolean isFinished);

    /**
     * 风控回调
     *
     * @param req
     */
    void riskCallback(RiskCallbackReq req);

    /**
     * 资匹回调
     *
     * @param req
     */
    void capitalBack(CapitalBackReq req);

    /**
     * 定时任务，将待收货状态自动变成已收货 155 -》 160
     */
    void ChangeOrderStatus();

    /**
     * 定时任务，修改状态，时间间隔minutes分钟 orgStatus -》 destStatus
     */
    void changeOrderStatus(int orgStatus, int destStatus, int minutes);


    /**
     * 生成提货码
     *
     * @return
     */
    String generateTakeCode();




    List<Map<String, Object>> findOrderStatusByQuery(String phone, String orderSn, String memberName);
}
