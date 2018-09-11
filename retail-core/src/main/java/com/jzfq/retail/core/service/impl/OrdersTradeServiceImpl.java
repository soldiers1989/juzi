package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.OrdersTrade;
import com.jzfq.retail.bean.domain.OrdersTradeQuery;
import com.jzfq.retail.core.api.service.OrdersTradeService;
import com.jzfq.retail.core.dao.OrdersTradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: OrdersTradeServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月03日 15:05
 * @Description:
 */
@Service
public class OrdersTradeServiceImpl implements OrdersTradeService {

    @Autowired
    private OrdersTradeMapper ordersTradeMapper;

    @Override
    public OrdersTrade getByOrderSn(String orderSn) {
        OrdersTradeQuery queryTrade = new OrdersTradeQuery();
        queryTrade.createCriteria().andOrderSnEqualTo(orderSn);
        List<OrdersTrade> ordersTradeList = ordersTradeMapper.selectByExample(queryTrade);
        if (ordersTradeList != null && ordersTradeList.size() > 0) {
            return ordersTradeList.get(0);
        }
        return null;
    }
}
