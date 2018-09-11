package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.SellerAddress;

/**
 * @Title: SellerAddressService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月03日 14:43
 * @Description: 商户地址信息service操作
 */
public interface SellerAddressService {
    /**
     * 通过商户ID获取商户地址信息
     *
     * @param sellerId
     * @return
     */
    SellerAddress getBySellerId(Integer sellerId);
}
