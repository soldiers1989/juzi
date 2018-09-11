package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.SellerTerm;
import com.github.pagehelper.Page;
import com.jzfq.retail.bean.vo.req.SellerTermSearchReq;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:01
 * @Description:
 */
public interface SellerTermManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<Map<String, Object>> findList(SellerTermSearchReq record);

    /***
     * 获取商户分期
     * @param sellerId
     * @return
     */
    List<SellerTerm> findListBySellerId(Integer sellerId);
}
