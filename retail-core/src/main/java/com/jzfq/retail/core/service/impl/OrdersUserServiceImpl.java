package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.OrdersUser;
import com.jzfq.retail.bean.domain.OrdersUserQuery;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.OrdersUserService;
import com.jzfq.retail.core.dao.OrdersUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:02
 * @Description: 订单接口实现
 */
@SuppressWarnings("ALL")
@Slf4j
@Service
public class OrdersUserServiceImpl implements OrdersUserService {

    @Autowired
    private OrdersUserMapper ordersUserMapper;

    @Override
    public void saveOrderUser(OrdersUser ordersUser) {
        //一个订单只有一个用户
        OrdersUserQuery query = new OrdersUserQuery();
        query.createCriteria().andOrderSnEqualTo(ordersUser.getOrderSn());
        List<OrdersUser> ordersUsers = ordersUserMapper.selectByExample(query);
        if (CollectionUtils.isEmpty(ordersUsers)) {
            ordersUserMapper.insert(ordersUser);
        }
    }

    /**
     * 根据orderSn查询orderUser
     * @param orderSn
     * @return
     */
    @Override
    public OrdersUser getByOrderSn(String orderSn) {
        OrdersUserQuery query = new OrdersUserQuery();
        query.createCriteria().andOrderSnEqualTo(orderSn);
        List<OrdersUser> ordersUserList = ordersUserMapper.selectByExample(query);
        if (ordersUserList == null || ordersUserList.size() == 0) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0032);
        }
        return ordersUserList.get(0);
    }

    /**
     * 根据orderId查询orderUser
     * @param orderId
     * @return
     */
    @Override
    public OrdersUser getByOrderId(Integer orderId) {
        OrdersUserQuery query = new OrdersUserQuery();
        query.createCriteria().andOrderIdEqualTo(orderId);
        List<OrdersUser> ordersUserList = ordersUserMapper.selectByExample(query);
        if (ordersUserList == null || ordersUserList.size() == 0) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0032);
        }
        return ordersUserList.get(0);
    }
}
