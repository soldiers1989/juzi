package com.jzfq.retail.core.service;

import com.jzfq.retail.bean.domain.CallAccountsOperation;

/**
 * @Title: CallAccountsOperationService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月05日 20:35
 * @Description: 账务系统操作记录接口
 */
public interface CallAccountsOperationService {
    /**
     * 添加
     * @param macId
     * @param serviceType
     * @param status
     * @param remark
     */
    void addOperation(String macId, String serviceType, String status, String remark);

    /**
     * 查询
     * @param macId
     * @param serviceType
     * @return
     */
    CallAccountsOperation getByMacIdAndServiceType(String macId, String serviceType);

    /**
     * 操作是否成功
     * @param macId
     * @param serviceType
     * @return
     */
    boolean serviceSuccess(String macId, String serviceType);

}
