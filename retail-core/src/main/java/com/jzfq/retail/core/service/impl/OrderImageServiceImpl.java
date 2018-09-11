package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.OrderImage;
import com.jzfq.retail.bean.domain.OrderImageQuery;
import com.jzfq.retail.bean.domain.OrderQRCode;
import com.jzfq.retail.bean.domain.OrderQRCodeQuery;
import com.jzfq.retail.common.enmu.OrderImageType;
import com.jzfq.retail.common.enmu.OrderQRCodeState;
import com.jzfq.retail.common.util.SymmetricEncoder;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.api.service.OrderImageService;
import com.jzfq.retail.core.api.service.OrderQRCodeService;
import com.jzfq.retail.core.dao.OrderImageMapper;
import com.jzfq.retail.core.dao.OrderQRCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月07日 16:29
 * @Description: 订单影像信息接口实现
 */
@Slf4j
@Service
public class OrderImageServiceImpl implements OrderImageService {

    @Autowired
    private OrderImageMapper orderImageMapper;

    @Value("${aes.encrypt.decrypt.secretKey}")
    private String aesSecretKey;

    @Override
    public OrderImage getOrderImages(String orderSn) {
        OrderImageQuery orderImageQuery = new OrderImageQuery();
        orderImageQuery.or().andOrderSnEqualTo(orderSn);
        List<OrderImage> orderImages = orderImageMapper.selectByExample(orderImageQuery);
        if (!CollectionUtils.isEmpty(orderImages)) {
            return orderImages.get(0);
        }
        return null;
    }

    public byte[] decryptImage(byte[] encryptData) {
        byte[] dncodData = SymmetricEncoder.AESDncode(aesSecretKey, encryptData);
        return dncodData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrderImage(Integer orderId, String orderSn, OrderImageType type, String url, String remark) {
        OrderImage image = new OrderImage();
        image.setOrderId(orderId);
        image.setOrderSn(orderSn);
        image.setType(type.getType());
        image.setUrl(url);
        image.setRemark(remark);
        image.setState(0);
        image.setCreateTime(DateUtil.getDate());
        orderImageMapper.insert(image);
    }

    @Override
    public List<OrderImage> getOrderImagesByOrderSnAndState(String orderSn, Integer state) {
        OrderImageQuery OIQ = new OrderImageQuery();
        OIQ.createCriteria().andOrderSnEqualTo(orderSn).andStateEqualTo(state);
        return orderImageMapper.selectByExample(OIQ);
    }
}
