package com.jzfq.retail.core.service;

import com.jzfq.retail.bean.domain.CallAccountsOperationLogQuery;
import com.jzfq.retail.bean.domain.CallAccountsOperationLogWithBLOBs;
import com.jzfq.retail.common.enmu.ForeignInterfaceStatus;
import com.jzfq.retail.core.dao.CallAccountsOperationLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Title: CallAccountsLog
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月05日 17:51
 * @Description: 调用账户系统账户 日志记录
 */
@Slf4j
@Component
public class CallAccountsLog {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private CallAccountsOperationLogMapper callAccountsOperationLogMapper;

    /**
     * 添加 日志
     *
     * @param macId        关键字，唯一标识
     * @param type         请求类型：1请求消息，2接收消息
     * @param serviceType  消息描述， 120开立商户额度账户、130开立商户资金账户、140商户绑卡、200用户绑卡
     * @param url          请求地址
     * @param requestParam 请求参数
     * @param remark       备注
     */
    public void addLog(String macId, Integer type, String serviceType, String url, String requestParam, String remark) {
        taskExecutor.execute(new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                CallAccountsOperationLogWithBLOBs operationLog = new CallAccountsOperationLogWithBLOBs();
                operationLog.setMacId(macId);
                operationLog.setType(type);
                operationLog.setServiceType(serviceType);
                operationLog.setUrl(url);
                operationLog.setRequestTime(new Date());
                operationLog.setRequestParam(requestParam);
                operationLog.setStatus(ForeignInterfaceStatus.SEND.getCode());
                operationLog.setRemark(remark);
                callAccountsOperationLogMapper.insert(operationLog);
                log.info("添加调用账务系统日志");
            }
        });
    }


    /**
     * 添加 日志
     *
     * @param macId         关键字，唯一标识
     * @param type          请求类型：1接收消息，2请求消息
     * @param serviceType   消息描述， 120开立商户额度账户、130开立商户资金账户、140商户绑卡、200用户绑卡、210恢复信用额度
     * @param url           请求地址
     * @param requestParam  请求参数
     * @param responseParam 返回参数
     * @param status        状态：0：已发送 1：成功 2：失败 3：未知-失败
     * @param remark        备注
     */
    public void addLog(String macId, Integer type, String serviceType, String url, String requestParam, String responseParam, Integer status, String remark) {
        taskExecutor.execute(new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                CallAccountsOperationLogWithBLOBs operationLog = new CallAccountsOperationLogWithBLOBs();
                operationLog.setMacId(macId);
                operationLog.setType(type);
                operationLog.setServiceType(serviceType);
                operationLog.setUrl(url);
                operationLog.setRequestTime(new Date());
                operationLog.setRequestParam(requestParam);
                operationLog.setResponseTime(new Date());
                operationLog.setResponseParam(responseParam);
                operationLog.setStatus(status);
                operationLog.setRemark(remark);
                callAccountsOperationLogMapper.insert(operationLog);
                log.info("添加调用账务系统日志");
            }
        });
    }

    /**
     * 添加 日志
     *
     * @param macId         关键字，唯一标识
     * @param type          请求类型：1接收消息，2请求消息
     * @param serviceType   消息描述， 120开立商户额度账户、130开立商户资金账户、140商户绑卡、200用户绑卡
     * @param responseParam 返回参数
     * @param status        状态：0：已发送 1：成功 2：失败 3：未知-失败
     * @param remark        备注
     */
    public void updateLog(String macId, Integer type, String serviceType, String responseParam, Integer status, String remark) {
        taskExecutor.execute(new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                CallAccountsOperationLogQuery query = new CallAccountsOperationLogQuery();
                query.createCriteria().andMacIdEqualTo(macId).andTypeEqualTo(type).andServiceTypeEqualTo(serviceType);
                List<CallAccountsOperationLogWithBLOBs> list = callAccountsOperationLogMapper.selectByExampleWithBLOBs(query);
                if (list != null && list.size() == 1) {
                    CallAccountsOperationLogWithBLOBs operationLog = list.get(0);
                    if (operationLog.getStatus().equals(ForeignInterfaceStatus.SEND.getCode())) {
                        operationLog.setResponseTime(new Date());
                        operationLog.setResponseParam(responseParam);
                        operationLog.setStatus(status);
                        operationLog.setRemark(remark);
                        callAccountsOperationLogMapper.updateByExample(operationLog, query);
                    }
                }
            }
        });
    }

}
