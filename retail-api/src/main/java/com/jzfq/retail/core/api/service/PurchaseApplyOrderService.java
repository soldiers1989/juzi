package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.req.ProductCateSearchReq;
import com.jzfq.retail.bean.vo.req.PurchaseApplyOrderApplyReq;
import com.jzfq.retail.bean.vo.req.PurchaseApplyOrderSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.PurchaseApplyOrderListRes;

/**
 * @Title: PurchaseApplyOrderService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月02日 14:41
 * @Description: 采购申单service操作接口
 */
public interface PurchaseApplyOrderService {

    /**
     * 获取采购申请单列表
     *
     * @param page     当前页
     * @param pageSize 每页条数
     * @param search   筛选条件
     * @return 返回列表
     */
    ListResultRes<PurchaseApplyOrderListRes> getList(Integer page, Integer pageSize, PurchaseApplyOrderSearchReq search);

    /**
     * 申请采购申请单
     *
     * @param applyReq 申请参数
     */
    void apply(PurchaseApplyOrderApplyReq applyReq);

    /**
     * 生成 单号
     *
     * @return
     */
    String generateApplySn();

}
