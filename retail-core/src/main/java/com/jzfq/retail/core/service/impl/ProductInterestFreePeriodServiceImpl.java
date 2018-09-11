package com.jzfq.retail.core.service.impl;

import com.google.common.collect.Lists;
import com.jzfq.retail.core.api.service.ProductInterestFreePeriodService;
import com.jzfq.retail.core.dao.manual.ProductInterestFreePeriodManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: ProductInterestFreePeriodServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月09日 18:34
 * @Description: 产品免息期数操作接口实现
 */
@Slf4j
@Service
public class ProductInterestFreePeriodServiceImpl implements ProductInterestFreePeriodService {

    @Autowired
    private ProductInterestFreePeriodManualMapper productInterestFreePeriodManualMapper;

    @Override
    public List<Integer> getInterestFrees(Integer productId) {
        List<Integer> result = Lists.newArrayList();
        String InterestFreeStr = productInterestFreePeriodManualMapper.findInterestFreePeriodsByProductId(productId);
        if (StringUtils.isNotBlank(InterestFreeStr)) {
            String[] periods = InterestFreeStr.split(",");
            for (String str : periods) {
                result.add(Integer.valueOf(str));
            }
        }
        return result;
    }
}
