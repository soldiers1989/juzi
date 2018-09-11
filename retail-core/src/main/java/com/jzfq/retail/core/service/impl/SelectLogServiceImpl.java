package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.CallAccountsOperation;
import com.jzfq.retail.bean.domain.CallAccountsOperationQuery;
import com.jzfq.retail.bean.vo.req.SelectOperationLogReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.SelectLogService;
import com.jzfq.retail.core.dao.CallAccountsOperationMapper;
import com.jzfq.retail.core.dao.manual.CallAccountsOperationLogManualMapper;
import com.jzfq.retail.core.dao.manual.CallAssetsOperationLogManualMapper;
import com.jzfq.retail.core.dao.manual.CallRiskOperationLogManualMapper;
import com.jzfq.retail.core.dao.manual.OrderLogManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Title: OrderLogServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月07日 17:15
 * @Description:
 */
@Slf4j
@Service
public class SelectLogServiceImpl implements SelectLogService {

    @Autowired
    private OrderLogManualMapper orderLogManualMapper;

    @Autowired
    private CallAccountsOperationLogManualMapper callAccountsOperationLogManualMapper;

    @Autowired
    private CallAssetsOperationLogManualMapper callAssetsOperationLogManualMapper;

    @Autowired
    private CallRiskOperationLogManualMapper callRiskOperationLogManualMapper;

    @Autowired
    private CallAccountsOperationMapper callAccountsOperationMapper;

    @Override
    public ListResultRes<Map<String, Object>> getOrderLogList(Integer page, Integer pageSize, String orderSn) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> result = orderLogManualMapper.findListByOrderSn(orderSn);
        return ListResultRes.newListResult(result.getResult(), result.getTotal(), result.getPageNum(), result.getPageSize());
    }


    @Override
    public ListResultRes<Map<String, Object>> getCallAccountsLogList(Integer page, Integer pageSize, SelectOperationLogReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> result = callAccountsOperationLogManualMapper.findListByMacId(search);
        return ListResultRes.newListResult(result.getResult(), result.getTotal(), result.getPageNum(), result.getPageSize());
    }

    @Override
    public ListResultRes<Map<String, Object>> getCallAssetsLogList(Integer page, Integer pageSize, SelectOperationLogReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> result = callAssetsOperationLogManualMapper.findListByMacId(search);
        return ListResultRes.newListResult(result.getResult(), result.getTotal(), result.getPageNum(), result.getPageSize());
    }

    @Override
    public ListResultRes<Map<String, Object>> getCallRiskLogList(Integer page, Integer pageSize, SelectOperationLogReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> result = callRiskOperationLogManualMapper.findListByMacId(search);
        return ListResultRes.newListResult(result.getResult(), result.getTotal(), result.getPageNum(), result.getPageSize());
    }

    @Override
    public ListResultRes<Map<String, Object>> getCallAccountsList(Integer page, Integer pageSize, String macId) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> result = callAccountsOperationMapper.selectByMacId(macId);
        return ListResultRes.newListResult(result.getResult(), result.getTotal(), result.getPageNum(), result.getPageSize());
    }
}
