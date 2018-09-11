package com.jzfq.retail.core.dao.manual;


import com.jzfq.retail.bean.domain.ProductCateBrandAreas;
import com.jzfq.retail.bean.vo.req.ProductCateBrandAreasSearchReq;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface ProductCateBrandAreasManualMapper {
    /**
     * 根据筛选条件查询列表
     * 支持分页
     *
     * @param search
     * @return
     */
    Page<Map<String, Object>> findList(ProductCateBrandAreasSearchReq search);

}