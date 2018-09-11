package com.jzfq.retail.core.dao.manual;


import com.jzfq.retail.bean.domain.SellerRatio;
import com.github.pagehelper.Page;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:01
 * @Description:
 */
public interface SellerRatioManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<SellerRatio> findList(SellerRatio record);
}
