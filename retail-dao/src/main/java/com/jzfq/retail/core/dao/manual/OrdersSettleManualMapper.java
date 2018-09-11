package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.OrdersSettle;
import com.jzfq.retail.bean.vo.req.OrdersSettleSearch;
import com.jzfq.retail.bean.vo.res.OrdersSettleRes;

import java.util.List;

public interface OrdersSettleManualMapper {
    /**
     * 获取列表 支持分页
     *
     * @param search 筛选条件
     * @return
     */
    Page<OrdersSettleRes> findList(OrdersSettleSearch search);


    List<OrdersSettleRes> findListAll(OrdersSettleSearch search);
}