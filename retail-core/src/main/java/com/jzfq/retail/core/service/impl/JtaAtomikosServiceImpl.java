package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.Product;
import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.JtaAtomikosService;
import com.jzfq.retail.core.dao.ProductCateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


/**
 * jta分布式事务测试类
 */
@Slf4j
@Service
public class JtaAtomikosServiceImpl implements JtaAtomikosService {

    @Autowired
    private ProductCateMapper productCateMapper;

    @Transactional
    @Override
    public void addProductCate(ProductCate productCate) {
        productCateMapper.insert(productCate);
        productCate.setSort(9);
        productCateMapper.insert(productCate);
    }
}
