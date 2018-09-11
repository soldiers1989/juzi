package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.CallAccountsOperation;
import com.jzfq.retail.bean.vo.req.SelectOperationLogReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Title: SelectLogService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月07日 17:12
 * @Description: 日志操作查询
 */
public interface SelectLogService {

    /**
     * 通过订单编号查询订单操作日志记录
     *
     * @param page
     * @param pageSize
     * @param orderSn
     * @return
     */
    ListResultRes<Map<String, Object>> getOrderLogList(Integer page, Integer pageSize, String orderSn);

    /**
     * 通过MacId查询账务系统调用操作日志记录
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> getCallAccountsLogList(Integer page, Integer pageSize, SelectOperationLogReq search);

    /**
     * 通过MacId查询资产（资金匹配）系统调用操作日志记录
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> getCallAssetsLogList(Integer page, Integer pageSize, SelectOperationLogReq search);

    /**
     * 通过MacId查询风控系统调用操作日志记录
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> getCallRiskLogList(Integer page, Integer pageSize, SelectOperationLogReq search);

    /**
     * 查询商户开户日志记录
     *
     * @param macId
     * @return
     */
    ListResultRes<Map<String, Object>> getCallAccountsList(Integer page, Integer pageSize, String macId);

}
