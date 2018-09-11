package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.ProductCore;
import com.jzfq.retail.bean.domain.ProductCoreQuery;
import com.jzfq.retail.core.api.service.ProductCoreService;
import com.jzfq.retail.core.dao.ProductCoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jzfq.retail.core.config.redis.CacheConstant.*;

@Service
public class ProductCoreServiceImpl implements ProductCoreService{

    @Autowired
    private ProductCoreMapper productCoreMapper;

    @Cacheable(value = PRODUCT_CORE, key = "#term + #type")
    @Override
    public List<ProductCore> findAll(Integer term, Integer type){
        ProductCoreQuery productCoreQuery = new ProductCoreQuery();
        productCoreQuery.createCriteria().andPeriodEqualTo(term).andTypeEqualTo(type);
        return productCoreMapper.selectByExample(productCoreQuery);
    };

}
