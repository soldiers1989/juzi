package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.SellerAddress;
import com.jzfq.retail.bean.domain.SellerAddressQuery;
import com.jzfq.retail.core.api.service.SellerAddressService;
import com.jzfq.retail.core.dao.SellerAddressMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: SellerAddressServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月03日 14:45
 * @Description: 商户地址信息service操作
 */
@Slf4j
@Service
public class SellerAddressServiceImpl implements SellerAddressService {

    @Autowired
    private SellerAddressMapper sellerAddressMapper;

    @Override
    public SellerAddress getBySellerId(Integer sellerId) {
        SellerAddressQuery sellerAddressQuery = new SellerAddressQuery();
        sellerAddressQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerAddress> sellerAddressList = sellerAddressMapper.selectByExample(sellerAddressQuery);
        if (sellerAddressList != null && sellerAddressList.size() > 0) {
            return sellerAddressList.get(0);
        }
        return null;
    }
}
