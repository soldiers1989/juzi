package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.req.RemittanceAccountReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.HashMap;
import java.util.List;

public interface RemittanceAccountService {

    /**
     * 添加
     * @param remittanceAccountReq
     * @return
     */
    int save(RemittanceAccountReq remittanceAccountReq);

    /**
     * @param remittanceAccountReq
     * @return
     */
    int update(RemittanceAccountReq remittanceAccountReq);
    /**
     * 删除
     * @param remittanceAccountReq
     * @return
     */
    int delete(RemittanceAccountReq remittanceAccountReq);

    /**
     * 查询帐户列表
     * @param sellerName
     * @return
     */
    ListResultRes<HashMap<String,Object>> queryRemittanceAccountList(Integer page, Integer pageSize, String sellerName);
}
