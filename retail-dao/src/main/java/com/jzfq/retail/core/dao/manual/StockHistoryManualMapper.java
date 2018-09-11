package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.StockHistory;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description:
 */
public interface StockHistoryManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<StockHistory> findList(StockHistory record);
}