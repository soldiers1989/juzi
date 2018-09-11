package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.core.api.service.JtaAtomikosCalleeService;
import com.jzfq.retail.core.api.service.JtaAtomikosService;
import com.jzfq.retail.core.dao.ProductBrandMapper;
import com.jzfq.retail.core.dao.ProductCateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * jta分布式事务测试类
 */
@Slf4j
@Service
public class JtaAtomikosCalleeServiceImpl implements JtaAtomikosCalleeService {

    @Autowired
    private ProductBrandMapper productBrandMapper;

    @Transactional
    @Override
    public void addProductBrand(ProductBrand productBrand) {
        productBrandMapper.insert(productBrand);
    }
}
