package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.req.WithdrawRecordSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.Map;

public interface WithdrawRecordService {

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, WithdrawRecordSearchReq search);
}
