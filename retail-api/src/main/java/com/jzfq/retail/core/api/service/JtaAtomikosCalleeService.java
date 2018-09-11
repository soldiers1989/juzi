package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.domain.ProductCate;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月10日 10:23
 * @Description: 分布式事务测试接口
 */
public interface JtaAtomikosCalleeService {

   void addProductBrand(ProductBrand productBrand);
}
