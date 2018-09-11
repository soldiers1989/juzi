package com.jzfq.retail.core.api.service;

import java.util.List;

/**
 * @Title: ProductInterestFreePeriodService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月09日 18:32
 * @Description: 产品免息期数操作接口
 */
public interface ProductInterestFreePeriodService {
    /**
     * 获取免息的期数
     * @param productId
     * @return
     */
    List<Integer> getInterestFrees(Integer productId);
}
