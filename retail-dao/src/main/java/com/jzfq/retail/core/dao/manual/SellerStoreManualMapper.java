package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.SellerStore;

/**
 * @Author MaoLixia
 * @Date 2018/8/21 18:03
 */
public interface SellerStoreManualMapper {

    /**
     * 根据sellerId查询店铺信息
     * @param sellerId
     * @return
     */
    SellerStore selectBySellerId(Integer sellerId);
}
