package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.Product;
import com.jzfq.retail.bean.domain.ProductGoods;

import java.util.List;

/**
 * @Author MaoLixia
 * @Date 2018/8/21 16:18
 */
public interface ProductGoodsManualMapper {
    /**
     * 根据商户ID查询二期店铺商品信息
     * @param sellerId
     * @return
     */
    List<ProductGoods> selectBySellerId(Integer sellerId);

    /**
     * 根据条件查询商品信息
     * @param productGoods
     * @return
     */
    List<ProductGoods> selectByParam(ProductGoods productGoods);
}
