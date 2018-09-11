package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.vo.req.AreasSearchReq;
import com.jzfq.retail.bean.vo.req.PurchaseApplyOrderSearchReq;

import java.util.Map;

public interface PurchaseApplyOrderManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<Map<String, Object>> findList(PurchaseApplyOrderSearchReq search);



}
