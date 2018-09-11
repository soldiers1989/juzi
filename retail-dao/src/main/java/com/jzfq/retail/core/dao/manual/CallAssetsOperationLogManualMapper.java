package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.vo.req.SelectOperationLogReq;

import java.util.Map;

/**
 * @Title: CallAccountsOperationLogManualMapper
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月07日 17:53
 * @Description:
 */
public interface CallAssetsOperationLogManualMapper {
    /**
     * 通过MacId 和 serviceType查询
     *
     * @param search
     * @return
     */
    Page<Map<String, Object>> findListByMacId(SelectOperationLogReq search);
}
