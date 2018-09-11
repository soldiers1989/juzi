package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.StockAddress;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description:
 */
public interface StockAddressManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<StockAddress> findList(StockAddress record);
}
