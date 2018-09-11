package com.jzfq.retail.core.dao.manual;


import com.jzfq.retail.bean.vo.req.ProductCateSearchReq;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 14:01
 * @Description:
 */
public interface ProductCateManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<Map<String, Object>> findList(ProductCateSearchReq record);

    List<Map<String, Object>> getOptionsBySellerId(Integer sellerId);
}
