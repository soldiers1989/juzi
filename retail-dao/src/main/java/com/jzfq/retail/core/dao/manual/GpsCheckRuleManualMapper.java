package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.vo.req.GpsCheckRuleSearchReq;

import java.util.Map;

/**
 * @Author  liuxueliang@juzifenqi.com
 * @Date 2018年08月06日 18:14
 * @Description:
 */
public interface GpsCheckRuleManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param search
     * @return
     */
    Page<Map<String, Object>> findList(GpsCheckRuleSearchReq search);
}
