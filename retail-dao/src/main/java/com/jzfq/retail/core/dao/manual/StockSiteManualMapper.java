package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.StockSite;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description:
 */
public interface StockSiteManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<StockSite> findList(StockSite record);
}
