package com.jzfq.retail.core.dao.manual;

public interface ProductInterestFreePeriodManualMapper {
    /**
     * 通过商品id获取免息期数字符串
     *
     * @param productId 商品id
     * @return
     */
    String findInterestFreePeriodsByProductId(Integer productId);

}