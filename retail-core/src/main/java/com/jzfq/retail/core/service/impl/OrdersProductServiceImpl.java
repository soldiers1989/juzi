package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.OrdersProduct;
import com.jzfq.retail.bean.domain.OrdersProductQuery;
import com.jzfq.retail.core.api.service.OrdersProductService;
import com.jzfq.retail.core.dao.OrdersProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Title: OrdersProductServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月03日 14:52
 * @Description: 订单产品信息service操作接口实现
 */
@Slf4j
@Service
public class OrdersProductServiceImpl implements OrdersProductService {

    @Autowired
    private OrdersProductMapper ordersProductMapper;

    @Override
    public OrdersProduct getByOrderSn(String orderSn) {
        OrdersProductQuery ordersProductQuery = new OrdersProductQuery();
        ordersProductQuery.createCriteria().andOrdersSnEqualTo(orderSn);
        List<OrdersProduct> ordersProducts = ordersProductMapper.selectByExample(ordersProductQuery);
        if (!CollectionUtils.isEmpty(ordersProducts)) {
            return ordersProducts.get(0);
        }
        return null;
    }
}
