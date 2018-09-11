package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderLogManualMapper {

    List<Map<String, Object>> selectByOrdersId(@Param("status") Integer status, @Param("ids") List<Integer> ids, @Param("hours") int hours);

    /**
     * 通过orderSn 查询订单操作日志
     * 支持分页
     *
     * @param orderSn
     * @return
     */
    Page<Map<String, Object>> findListByOrderSn(String orderSn);
}