package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.common.enmu.OrderQRCodeState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.jzfq.retail.bean.domain.OrderQRCode;
import com.jzfq.retail.bean.domain.OrderQRCodeQuery;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.OrderQRCodeService;
import com.jzfq.retail.core.dao.OrderQRCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月07日 16:29
 * @Description: 二维码服务操作接口
 */
@Slf4j
@Service
public class OrderQRCodeServiceImpl implements OrderQRCodeService {

    @Value("${qrcode.valid-time}")
    private Integer validTime;

    @Autowired
    private OrderQRCodeMapper orderQRCodeMapper;

    @Override
    public OrderQRCode getOrderQRCodeByOrderSn(String orderSn) {
        OrderQRCodeQuery query = new OrderQRCodeQuery();
        query.createCriteria().andOrderSnEqualTo(orderSn).andIsAvailableEqualTo(OrderQRCodeState.ORDER_QRCODE_STATE_0.getCode());
        List<OrderQRCode> orderQRCodes = orderQRCodeMapper.selectByExample(query);
        if (orderQRCodes == null || orderQRCodes.size() == 0) {
            return null;
        }
        if (orderQRCodes.size() == 1) {
            return orderQRCodes.get(0);
        }
        log.error("获取订单[orderSn:{}]二维码对象存在多条可用数据");
        throw new RuntimeException("获取订单二维码对象异常,对象存在多条");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setOrderQRCodeStateByOrderSn(Integer id, OrderQRCodeState state) {
        OrderQRCode orderQRCode = orderQRCodeMapper.selectByPrimaryKey(id);
        if (orderQRCode == null) {
            log.error("设定订单[orderSn:{}]二维码状态的对象不存在");
            throw new RuntimeException("设定订单二维码状态的对象不存在");
        }
        orderQRCode.setIsAvailable(state.getCode());
        orderQRCodeMapper.updateByPrimaryKey(orderQRCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insetOrderQRCode(String orderSn, String QRCUrl) {
        //先把有效的设定为无效
        OrderQRCodeQuery query = new OrderQRCodeQuery();
        query.createCriteria().andOrderSnEqualTo(orderSn).andIsAvailableEqualTo(OrderQRCodeState.ORDER_QRCODE_STATE_0.getCode());
        List<OrderQRCode> orderQRCodes = orderQRCodeMapper.selectByExample(query);
        orderQRCodes.stream().forEach(x -> {
            x.setIsAvailable(OrderQRCodeState.ORDER_QRCODE_STATE_1.getCode());
            orderQRCodeMapper.updateByPrimaryKey(x);
        });
        //增加新的二维码对象
        OrderQRCode newCode = new OrderQRCode();
        newCode.setOrderSn(orderSn);
        newCode.setLastTime(DateUtil.minusTimes(validTime));
        newCode.setIsAvailable(OrderQRCodeState.ORDER_QRCODE_STATE_0.getCode());//是否可用 0可用，1失效
        newCode.setQrCodeAddr(QRCUrl);
        newCode.setCreateTime(DateUtil.getDate());
        orderQRCodeMapper.insert(newCode);
    }
}
