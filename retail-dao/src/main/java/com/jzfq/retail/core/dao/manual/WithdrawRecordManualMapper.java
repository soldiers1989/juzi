package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.vo.req.WithdrawRecordSearchReq;

import java.util.Map;

public interface WithdrawRecordManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param req
     * @return
     */
    Page<Map<String, Object>> findList(WithdrawRecordSearchReq req);
}