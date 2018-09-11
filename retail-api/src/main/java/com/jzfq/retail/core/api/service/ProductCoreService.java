package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.ProductCore;

import java.util.List;

public interface ProductCoreService {

    List<ProductCore> findAll(Integer term, Integer type);

}
