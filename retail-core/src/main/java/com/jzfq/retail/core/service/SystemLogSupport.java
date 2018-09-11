package com.jzfq.retail.core.service;

import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.mapper.AccessLog;
import com.jzfq.retail.common.enmu.ForeignInterfaceStatus;
import com.jzfq.retail.common.enmu.ProductCateBrandAreasStatus;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.messaging.util.CallStatus;
import com.jzfq.retail.core.messaging.util.ProviderRealm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liuwei
 * @time 2018/6/28 17:33
 * @description 系统支撑组件
 */
@Slf4j
@Component
public class SystemLogSupport {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private RiskApprovalLogMapper riskApprovalLogMapper;

    @Autowired
    private SellerPasswordChangeRecordMapper sellerPasswordChangeRecordMapper;

    @Autowired
    private SellerEnterLogMapper sellerEnterLogMapper;

    @Autowired
    private CallRiskOperationLogMapper callRiskOperationLogMapper;

    @Autowired
    private CallAssetsOperationLogMapper callAssetsOperationLogMapper;

    @Autowired
    private SellerLoginLogMapper sellerLoginLogMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

        //持久化访问日志
    public void persistAccessLog(ProviderRealm provider, String address, String uri, String title, String bizId, String serialNo, CallStatus callStatus, String reqData, String resData, String exception) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                AccessLog accessLog = new AccessLog();
                accessLog.setProvider(provider.getMark());
                accessLog.setAddress(address);
                accessLog.setUri(uri);
                accessLog.setTitle(title);
                accessLog.setBizId(bizId);
                accessLog.setSerialNo(serialNo);
                accessLog.setCallStatus(callStatus.getCode());
                accessLog.setReqData(reqData);
                accessLog.setResData(resData);
                accessLog.setException(exception);
                accessLog.setRemark(title);
                accessLogMapper.saveAccessLog(accessLog);
                log.info("异步存储访问日志成功");
            }
        });
    }

    //变更访问日志
    public void updateAccessLog(ProviderRealm provider, String bizId, String serialNo, CallStatus callStatus, String resData, String exception){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                AccessLog accessLog = new AccessLog();
                accessLog.setProvider(provider.getMark());
                accessLog.setBizId(bizId);
                accessLog.setSerialNo(serialNo);
                accessLog.setCallStatus(callStatus.getCode());
                accessLog.setResData(resData);
                accessLog.setException(exception);
                accessLogMapper.updateAccessLog(accessLog);
                log.info("异步更新访问日志成功");
            }
        });
    }

    /**
     * 分类-品牌-城市-区间价 审核提交日志
     *
     * @param productCateBrandAreasId 关联ID
     * @param status                  状态
     * @param reason                  审核描述
     * @param approvalUser            审核人
     * @param approvalUserId          审核人ID
     */
    public void riskApprovalLog(Integer productCateBrandAreasId, ProductCateBrandAreasStatus status, String reason, String approvalUser, Integer approvalUserId) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                RiskApprovalLog riskApprovalLog = new RiskApprovalLog();
                riskApprovalLog.setProductCateBrandAreasId(productCateBrandAreasId);
                riskApprovalLog.setStatus(status.getCode());
                riskApprovalLog.setReason(reason);
                riskApprovalLog.setApprovalUser(approvalUser);
                riskApprovalLog.setApprovalUserId(approvalUserId);
                riskApprovalLog.setApprovalTime(new Date());
                riskApprovalLogMapper.insert(riskApprovalLog);
                log.info("异步存储分类-品牌-城市-区间价审核日志成功");
            }
        });
    }

    /**
     * 商户账号密码修改日志记录
     *
     * @param sellerId       商户店铺ID
     * @param sellerName     商户店铺名称
     * @param beforeMobile   旧商户店铺预留手机号
     * @param beforePassword 旧商户店铺密码
     * @param afterMobile    新商户店铺预留手机号
     * @param afterPassword  新商户店铺密码
     * @param createId       修改用户id
     * @param createUser     修改用户名称
     */
    public void sellerPasswordChangeRecord(Integer sellerId, String sellerName, String beforeMobile, String beforePassword,
                                           String afterMobile, String afterPassword, Integer createId, String createUser) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SellerPasswordChangeRecord record = new SellerPasswordChangeRecord();
                record.setSellerId(sellerId);
                record.setSellerName(sellerName);
                record.setBeforeMobile(beforeMobile);
                record.setBeforePassword(beforePassword);
                record.setAfterMobile(afterMobile);
                record.setAfterPassword(afterPassword);
                record.setCreateUser(createUser);
                record.setCreateId(createId);
                record.setCreateTime(new Date());
                sellerPasswordChangeRecordMapper.insert(record);
                log.info("添加商户账号密码修改日志成功");
            }
        });
    }

    /**
     * 商户入驻记录日志
     *
     * @param selleCode 商户code
     * @param type
     * @param infoDesc
     * @param reqUrl
     * @param reqParam
     * @param resParam
     * @param remark
     */
    public void sellerEnterLogSave(String selleCode,int type, String infoDesc, String reqUrl, String reqParam, String resParam, String remark) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SellerEnterLogWithBLOBs sellerEnterLogWithBLOBs = new SellerEnterLogWithBLOBs();
                sellerEnterLogWithBLOBs.setType(type);
                sellerEnterLogWithBLOBs.setInfoDesc(infoDesc);
                sellerEnterLogWithBLOBs.setRequestUrl(reqUrl);
                sellerEnterLogWithBLOBs.setRequestParam(reqParam);
                sellerEnterLogWithBLOBs.setResponseParam(resParam);
                sellerEnterLogWithBLOBs.setUpdateTime(new Date());
                sellerEnterLogWithBLOBs.setCreateTime(new Date());
                sellerEnterLogWithBLOBs.setRemark(remark);
                sellerEnterLogWithBLOBs.setSellerCode(selleCode);
                sellerEnterLogMapper.insert(sellerEnterLogWithBLOBs);
                log.info("商户入驻日志记录");
            }
        });
    }

    /**
     * 风控准入、风控进件记录日志 - 成功  （风控系统调用操作日志表）
     *
     * @param type
     * @param serviceType
     * @param url
     * @param reqParam
     * @param resParam
     * @param reqTime
     * @param resTime
     * @param remark
     */
    public void callRiskOperationLogSave(String orderId, int type, String serviceType, String url, String reqParam, String resParam, Date reqTime, Date resTime, String remark) {
        if(orderId == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0033);
        }
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                CallRiskOperationLogWithBLOBs callRiskOperationLogWithBLOBs = new CallRiskOperationLogWithBLOBs();
                callRiskOperationLogWithBLOBs.setMacId(orderId + "");
                callRiskOperationLogWithBLOBs.setType(type);
                callRiskOperationLogWithBLOBs.setServiceType(serviceType);
                callRiskOperationLogWithBLOBs.setUrl(url);
                callRiskOperationLogWithBLOBs.setRequestParam(reqParam);
                callRiskOperationLogWithBLOBs.setResponseParam(resParam);
                callRiskOperationLogWithBLOBs.setRequestTime(reqTime);
                callRiskOperationLogWithBLOBs.setResponseTime(resTime);
                callRiskOperationLogWithBLOBs.setRemark(remark);
                callRiskOperationLogWithBLOBs.setStatus(ForeignInterfaceStatus.SUCCESS.getCode());//'状态：0：已发送 1：成功 2：失败 3：未知-失败'
                callRiskOperationLogMapper.insert(callRiskOperationLogWithBLOBs);
                log.info("风控准入、风控进件记录日志");
            }
        });
    }

    /**
     * 170资匹生成还款计划、180资匹取消 - 成功  （资产（资金匹配）系统调用操作日志表）
     *
     * @param type
     * @param serviceType
     * @param url
     * @param reqParam
     * @param resParam
     * @param reqTime
     * @param resTime
     * @param remark
     */
    public void callAssetsOperationLogSave(String mac_id, int type, String serviceType, String url, String reqParam, String resParam, Date reqTime, Date resTime, String remark) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                CallAssetsOperationLogWithBLOBs callAssetsOperationLogWithBLOBs = new CallAssetsOperationLogWithBLOBs();
                callAssetsOperationLogWithBLOBs.setMacId(mac_id + "");
                callAssetsOperationLogWithBLOBs.setType(type);
                callAssetsOperationLogWithBLOBs.setServiceType(serviceType);
                callAssetsOperationLogWithBLOBs.setUrl(url);
                callAssetsOperationLogWithBLOBs.setRequestParam(reqParam);
                callAssetsOperationLogWithBLOBs.setResponseParam(resParam);
                callAssetsOperationLogWithBLOBs.setRequestTime(reqTime);
                callAssetsOperationLogWithBLOBs.setResponseTime(resTime);
                callAssetsOperationLogWithBLOBs.setRemark(remark);
                callAssetsOperationLogWithBLOBs.setStatus(ForeignInterfaceStatus.SUCCESS.getCode());//'状态：0：已发送 1：成功 2：失败 3：未知-失败'
                callAssetsOperationLogMapper.insert(callAssetsOperationLogWithBLOBs);
                log.info("资匹生成还款计划、180资匹取消成功");
            }
        });
    }

    /**
     * 商户登录日志记录
     *
     * @param sellerId       商户id
     * @param sellerName     商户名称
     * @param sellerMobile   商户手机号
     * @param sellerLogin    商户登录账户
     * @param sellerPassword 商户密码
     * @param code           微信code
     * @param openId         微信openId
     */
    public void sellerLoginOperationLogSave(Integer sellerId, String sellerName, String sellerMobile, String sellerLogin, String sellerPassword, String code, String openId) {
        taskExecutor.execute(new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                SellerLoginLog loginLog = new SellerLoginLog();
                loginLog.setSellerId(sellerId);
                loginLog.setSellerName(sellerName);
                loginLog.setSellerMobile(sellerMobile);
                loginLog.setSellerLogin(sellerLogin);
                loginLog.setSellerPassword(sellerPassword);
                loginLog.setCode(code);
                loginLog.setOpenid(openId);
                loginLog.setCreateTime(new Date());
                sellerLoginLogMapper.insert(loginLog);
                log.info("商户小程序端登录日志异步插入成功");
            }
        });
    }

    /**
     * 订单操作日志异步添加
     *
     * @param operationId   操作人id
     * @param operationName 操作人姓名
     * @param orderId       订单id
     * @param orderSn       订单编号
     * @param content       订单内容
     * @param orderStatus   订单状态
     */
    public void orderLogSave(Integer operationId, String operationName, Integer orderId, String orderSn, String content, Integer orderStatus) {
        taskExecutor.execute(new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                OrderLog orderLog = new OrderLog();
                orderLog.setOperationId(operationId);
                orderLog.setOperationName(operationName);
                orderLog.setOrderId(orderId);
                orderLog.setOrderSn(orderSn);
                orderLog.setContent(content);
                orderLog.setOrderStatus(orderStatus);
                orderLog.setCreateTime(new Date());
                orderLogMapper.insert(orderLog);
                log.info("订单操作日志异步添加成功");
            }
        });
    }

    /**
     * 订单操作日志异步添加 批量
     *
     * @param listMap
     * @param content       订单内容
     * @param orderStatus   订单状态
     */
    public void orderLogBatchSave(List<Map<String, Object>> listMap, String content, int orderStatus) {
        for(Map<String, Object> map : listMap){
            orderLogSave(null, null, Integer.parseInt(map.get("order_id").toString()), map.get("order_sn").toString(), content, orderStatus);
        }
    }
}
