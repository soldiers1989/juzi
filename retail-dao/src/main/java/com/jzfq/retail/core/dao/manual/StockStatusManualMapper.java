package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.StockStatus;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description:
 */
public interface StockStatusManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<StockStatus> findList(StockStatus record);
}