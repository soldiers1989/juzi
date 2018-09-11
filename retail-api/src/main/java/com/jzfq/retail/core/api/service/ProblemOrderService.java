package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.vo.res.OrderStatisticsRes;

import java.util.List;
import java.util.Map;

import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:43
 * @Description: 订单接口
 */
public interface ProblemOrderService {

    String queryCustomerAuth(String value);

    List<Map> statisticsAuthFailSuccessRate();
    /**
     * 统计未认证、认证成功、认证失败用户的数量及对应比例
     *
     * @return
     */
    Map<String, Object> getAuthenticationStateOfCountAndRatio();
    OrderStatisticsRes countUpOrderBase();
    Integer countEntryOrder();
    Map<String, Object> getMap1();
    List<Map<String,Object>> statisticsSellerRebate();
}
