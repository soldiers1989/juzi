package com.jzfq.retail.core.dao.manual;


import com.jzfq.retail.bean.domain.SellerSingleCredit;
import com.github.pagehelper.Page;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:01
 * @Description:
 */
public interface SellerSingleCreditManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<SellerSingleCredit> findList(SellerSingleCredit record);
}
