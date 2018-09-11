package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.bean.vo.res.TakeCodeOrderRes;

import java.util.List;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月10日 10:23
 * @Description: 分布式事务测试接口
 */
public interface JtaAtomikosService {

   void addProductCate(ProductCate productCate);
}
