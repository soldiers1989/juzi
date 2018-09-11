package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.OrderSnCount;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.OrderSnCountService;
import com.jzfq.retail.core.dao.manual.OrderSnCountManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月6日 17:43
 * @Description: 订单号计数数据接口
 */
@Slf4j
@Service
public class OrderSnCountServiceImpl implements OrderSnCountService {

    @Autowired
    private OrderSnCountManualMapper orderSnCountManualMapper;

    private final static String ORDER_SN_START = "XLS";

    @Override
    public String getOrderSn(Integer sellerId) {
        //xls+商户id（最长五位,不够左补0）+6位年月日+当天商户的订单数量（4位，不够左补0）
        StringBuffer orderSn = new StringBuffer("");
        String dateStr = DateUtil.formatDate(new Date(), "yyMMdd");
        OrderSnCount orderSnCount = orderSnCountManualMapper.getBySellerIdAndDateStr(sellerId, dateStr);
        Integer countVal = (orderSnCount != null && orderSnCount.getCurrentVal() != null) ? orderSnCount.getCurrentVal() : 0;
        orderSn.append(ORDER_SN_START).append(String.format("%05d", sellerId)).append(dateStr).append(String.format("%04d", countVal));
        if (countVal == 0) {
            //新增
            orderSnCountManualMapper.initCurrentVal(sellerId, dateStr);
        } else {
            //修改
            orderSnCountManualMapper.updateCurrentValById(orderSnCount.getId());
        }
        return orderSn.toString();
    }


}
