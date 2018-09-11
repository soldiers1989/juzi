package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.CallAccountsOperation;
import com.jzfq.retail.bean.domain.CallAccountsOperationQuery;
import com.jzfq.retail.core.dao.CallAccountsOperationMapper;
import com.jzfq.retail.core.service.CallAccountsOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Title: CallAccountsOperationServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月06日 9:53
 * @Description: TODO(用一句话描述该文件做什么)
 */
@Slf4j
@Service
public class CallAccountsOperationServiceImpl implements CallAccountsOperationService {

    @Autowired
    private CallAccountsOperationMapper callAccountsOperationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOperation(String macId, String serviceType, String status, String remark) {
        CallAccountsOperation callAccountsOperation = new CallAccountsOperation();
        callAccountsOperation.setMacId(macId);
        callAccountsOperation.setServiceType(serviceType);
        callAccountsOperation.setStatus(status);
        callAccountsOperation.setRemark(remark);
        callAccountsOperation.setCreateTime(new Date());
        //查询是否存在
        CallAccountsOperationQuery query = new CallAccountsOperationQuery();
        query.createCriteria().andMacIdEqualTo(macId).andServiceTypeEqualTo(serviceType);
        List<CallAccountsOperation> callAccountsOperations = callAccountsOperationMapper.selectByExample(query);
        if (callAccountsOperations != null && callAccountsOperations.size()>0) {
            callAccountsOperationMapper.updateByExample(callAccountsOperation, query);
        } else {
            callAccountsOperationMapper.insert(callAccountsOperation);
        }
    }

    @Override
    public CallAccountsOperation getByMacIdAndServiceType(String macId, String serviceType) {
        CallAccountsOperationQuery query = new CallAccountsOperationQuery();
        query.createCriteria().andMacIdEqualTo(macId).andServiceTypeEqualTo(serviceType);
        List<CallAccountsOperation> callAccountsOperations = callAccountsOperationMapper.selectByExample(query);
        if (callAccountsOperations != null && callAccountsOperations.size()>0) {
            return callAccountsOperations.get(0);
        }
        return null;
    }

    @Override
    public boolean serviceSuccess(String macId, String serviceType) {
        CallAccountsOperation accounts = getByMacIdAndServiceType(macId, serviceType);
        if(accounts!=null && accounts.getStatus().equals("1")){
            return true;
        }
        return false;
    }
}
