package com.jzfq.retail.core.dao.manual;


import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.vo.req.ProductBrandSearchReq;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 14:08
 * @Description: 商品品牌扩展mapper
 */
public interface ProductBrandManualMapper {

    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<ProductBrand> findList(ProductBrandSearchReq record);

    /**
     * 通过品类ID删除品牌（逻辑删除）
     * @param cateId
     */
    void deleteByCateId(Integer cateId);

}
