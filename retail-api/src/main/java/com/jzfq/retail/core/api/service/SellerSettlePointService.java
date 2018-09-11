package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.req.ProductSearchReq;
import com.jzfq.retail.bean.vo.req.SellerSettlePointReq;
import com.jzfq.retail.bean.vo.req.SellerSettlePointSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

public interface SellerSettlePointService {

    ListResultRes<Map<String, Object>> findList(Integer page, Integer pageSize, SellerSettlePointSearchReq search);

    /**
     * 添加
     *
     * @param sellerId  商户Id
     * @param settlePoint  结算扣点百分比
     * @return
     */
    int save(SellerSettlePointReq req);

    /**
     * @param sellerId  商户Id
     * @param settlePoint  结算扣点百分比
     * @param status  结算扣点百分比
     * @return
     */
    int update(SellerSettlePointReq req);
    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Integer id);
}
