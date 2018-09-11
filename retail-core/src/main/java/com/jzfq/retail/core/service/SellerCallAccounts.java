package com.jzfq.retail.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jzfq.retail.bean.domain.Seller;
import com.jzfq.retail.bean.vo.crm.*;
import com.jzfq.retail.common.enmu.ForeignInterfaceServiceType;
import com.jzfq.retail.common.enmu.ForeignInterfaceType;
import com.jzfq.retail.common.enmu.SellerStatus;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.api.service.SellerService;
import com.jzfq.retail.core.call.service.MerchantCapitalAccountService;
import com.jzfq.retail.core.call.service.MerchantCreditAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Title: SellerCallAccounts
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月05日 17:15
 * @Description: 商户入住调用账户系统
 */
@Slf4j
@Component
public class SellerCallAccounts {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private MerchantCapitalAccountService merchantCapitalAccountService;

    @Autowired
    private MerchantCreditAccountService merchantCreditAccountService;

    @Autowired
    private CallAccountsOperationService callAccountsOperationService;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SellerService sellerService;

    @Value("${CRM.base_url}")
    private String crmBaseUrl;

    @Value("${CRM.callback_url}")
    private String crmCallbackUrl;

    //开户成功
    private final static Integer CREATE_SUCCESS = 0;
    //开户失败
    private final static Integer CREATE_FAILED = 1;

    //Crm 商户入住开户操作
    public void callAccounts(CRMSystemReq info, Integer sellerId) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //1.调用账务系统 开立商户额度
                String businessId = info.getCompany().getMerchantCode();
                RiskInfo risk = info.getRisk();
                CRMInfo crm = info.getCrm();
                if (!callAccountsOperationService.serviceSuccess(businessId, ForeignInterfaceServiceType.DESCRIBE_120.toString())) {
                    try {
                        merchantCreditAccountService.initMerchantAccount(risk.getApprovedDate(), businessId, risk.getFirstMonthQuota(), risk.getMonthQuota(), crm.getSignDateStart(), risk.getTotalQuota());
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_120.toString(), "1", "商户[" + businessId + "]信用账户开户成功");
                    } catch (Exception e) {
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_120.toString(), "2", "商户[" + businessId + "]信用账户开户失败");
                        log.error("商户入住调用账务系统-信用账户开户失败：{}", e.getMessage());
                        //发送失败信息
                        resultCRM(sellerId, businessId, crm.getSerialNumber(), CREATE_FAILED, "信用账户开户失败", "商户入住调用账务系统-信用账户开户失败" + e.getMessage());
                        return;
                    }
                }
                //2.调用账务系统 开立商户账户
                ShopInfo shop = info.getShop();
                String shopName = shop.getShopName();
                if (!callAccountsOperationService.serviceSuccess(businessId, ForeignInterfaceServiceType.DESCRIBE_130.toString())) {
                    try {
                        merchantCapitalAccountService.initAccount(businessId, shopName);
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_130.toString(), "1", "商户[" + businessId + "]资金账户开户成功");
                    } catch (Exception e) {
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_130.toString(), "2", "商户[" + businessId + "]资金账户开户失败");
                        log.error("商户入住调用账务系统-资金开户失败：{}", e.getMessage());
                        //发送失败信息
                        resultCRM(sellerId, businessId, crm.getSerialNumber(), CREATE_FAILED, "资金账户开户失败", "商户入住调用账务系统-资金账户开户失败" + e.getMessage());
                        return;
                    }
                }
                //3.调用账务系统 绑定商户银行卡
                BankcardInfo bankcard = info.getBankcard();
                String bankCardNo = bankcard.getBankNo();
                Integer bankCode = bankcard.getBankCode();
                String name = bankcard.getName();
                String isPublic = bankcard.getIsPublic();
                String certNo = bankcard.getCertNo();
                String bankPhone = bankcard.getBankPhone();
                String payeeBankAssociatedCode = bankcard.getPayeeBankAssociatedCode();
                String payeeBankFullName = bankcard.getPayeeBankFullName();

                if (!callAccountsOperationService.serviceSuccess(businessId, ForeignInterfaceServiceType.DESCRIBE_140.toString())) {
                    try {
                        if (isPublic.equals("1")) {
                            merchantCapitalAccountService.bindBankcardPub(bankCardNo, bankCode, name, businessId, payeeBankAssociatedCode, payeeBankFullName);
                        } else if (isPublic.equals("2")) {
                            merchantCapitalAccountService.bindBankcardPri(bankCardNo, bankCode, name, certNo, bankPhone, businessId);
                        } else {
                            throw new RuntimeException("商户绑卡未知卡类型-对公or对私");
                        }
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_140.toString(), "1", "商户[" + businessId + "]资金账户绑卡成功");
                    } catch (Exception e) {
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_140.toString(), "2", "商户[" + businessId + "]资金账户绑卡失败");
                        log.error("商户入住调用账务系统-资金绑卡失败：{}", e.getMessage());
                        //发送失败信息
                        resultCRM(sellerId, businessId, crm.getSerialNumber(), CREATE_FAILED, "资金账户绑卡失败", "商户入住调用账务系统-资金账户绑卡失败" + e.getMessage());
                        return;
                    }
                }
                //发送成功信息
                resultCRM(sellerId, businessId, crm.getSerialNumber(), CREATE_SUCCESS, "成功", "商户入住成功");
            }
        });
    }

    //导入数据初始化开户方法
    public void initDataCallAccounts(CRMSystemReq info) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //1.调用账务系统 开立商户额度
                String businessId = info.getCompany().getMerchantCode();
                RiskInfo risk = info.getRisk();
                CRMInfo crm = info.getCrm();

                // 调用状态、调用描述
                String status = null;
                String statusDesc = null;
                if (!callAccountsOperationService.serviceSuccess(businessId, ForeignInterfaceServiceType.DESCRIBE_120.toString())) {
                    try {
                        merchantCreditAccountService.initMerchantAccount(risk.getApprovedDate(), businessId, risk.getFirstMonthQuota(), risk.getMonthQuota(), crm.getSignDateStart(), risk.getTotalQuota());
                        status = "1";
                        statusDesc = "商户[" + businessId + "]信用账户开户成功";
                    } catch (Exception e) {
                        status = "2";
                        statusDesc = "商户[" + businessId + "]信用账户开户失败，描述：" + e.getMessage();
                        sellerService.updateAuditStatus(businessId,SellerStatus.OPEN_ACCOUNT_FAILED.getCode());
                        return;
                    } finally {
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_120.toString(), status, statusDesc);
                    }
                }
                //2.调用账务系统 开立商户账户
                ShopInfo shop = info.getShop();
                String shopName = shop.getShopName();
                if (!callAccountsOperationService.serviceSuccess(businessId, ForeignInterfaceServiceType.DESCRIBE_130.toString())) {
                    try {
                        merchantCapitalAccountService.initAccount(businessId, shopName);
                        status = "1";
                        statusDesc = "商户[" + businessId + "]资金账户开户成功";
                    } catch (Exception e) {
                        status = "2";
                        statusDesc = "商户[" + businessId + "]资金账户开户失败，描述：" + e.getMessage();
                        sellerService.updateAuditStatus(businessId,SellerStatus.OPEN_ACCOUNT_FAILED.getCode());
                        return;
                    } finally {
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_130.toString(), status, statusDesc);
                    }
                }
                //3.调用账务系统 绑定商户银行卡
                BankcardInfo bankcard = info.getBankcard();
                String bankCardNo = bankcard.getBankNo();
                Integer bankCode = bankcard.getBankCode();
                String name = bankcard.getName();
                String isPublic = bankcard.getIsPublic();
                String certNo = bankcard.getCertNo();
                String bankPhone = bankcard.getBankPhone();
                String payeeBankAssociatedCode = bankcard.getPayeeBankAssociatedCode();
                String payeeBankFullName = bankcard.getPayeeBankFullName();
                if (!callAccountsOperationService.serviceSuccess(businessId, ForeignInterfaceServiceType.DESCRIBE_140.toString())) {
                    try {
                        if (isPublic.equals("1")) {
                            // 对公绑卡，姓名为空，传商铺名称
                            merchantCapitalAccountService.bindBankcardPub(bankCardNo, bankCode, name, businessId, payeeBankAssociatedCode, payeeBankFullName);
                        } else if (isPublic.equals("2")) {
                            merchantCapitalAccountService.bindBankcardPri(bankCardNo, bankCode, name, certNo, bankPhone, businessId);
                        } else {
                            throw new RuntimeException("初始化数据 >> 商户资金账户绑卡未知卡类型");
                        }
                        status = "1";
                        statusDesc = "商户[" + businessId + "]资金账户绑卡成功";
                        sellerService.updateAuditStatus(businessId,SellerStatus.OPEN_ACCOUNT_SUCCESS.getCode());
                    } catch (Exception e) {
                        status = "2";
                        statusDesc = "商户[" + businessId + "]资金账户绑卡失败，描述：" + e.getMessage();
                        sellerService.updateAuditStatus(businessId,SellerStatus.OPEN_ACCOUNT_FAILED.getCode());
                        return;
                    } finally {
                        callAccountsOperationService.addOperation(businessId, ForeignInterfaceServiceType.DESCRIBE_140.toString(), status, statusDesc);
                    }
                }
            }
        });
    }


    /**
     * 开户结果返回给CRM
     *
     * @param centreCode      核心商户商编（sellerId）
     * @param sellerCode      商户CODE
     * @param serialNumber    流水号
     * @param type            开户结果 0 成功 1 失败
     * @param approvalOpinion 开户意见
     * @param riskSuggestion  开户备注
     */
    private void resultCRM(Integer centreCode, String sellerCode, String serialNumber, Integer type, String approvalOpinion, String riskSuggestion) {
        //如果商户开户失败，则修改商户状态为开户失败状态
        if(1 == type) {
            sellerService.updateAuditStatus(centreCode, SellerStatus.OPEN_ACCOUNT_FAILED.getCode());
        }
        //发送回调
        Map<String, Object> params = Maps.newHashMap();
        params.put("centreCode", centreCode.toString());
        params.put("serialNumber", serialNumber);
        params.put("type", type);
        params.put("approvalOpinion", approvalOpinion);
        params.put("riskSuggestion", riskSuggestion);
        params.put("enterTime", DateUtil.formatDate(DateUtil.getDate(), "yyyy-MM-dd HH:mm:ss"));
        //请求
        String url = crmBaseUrl + crmCallbackUrl;
        log.info("CRM商户入住BD系统回调[url:{},params:{}]-->开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(params), "application/json", 60000);
        long e = System.currentTimeMillis();
        log.info("CRM商户入住BD系统回调-->结束,耗时【{}ms】,返回结果：【{}】", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("CRM商户入住BD系统回调返回结果为空");
        }
        //返回报文格式：{"code": "01"}
        JSONObject jsonObject = JSONObject.parseObject(res);
        if (!("01".equals(jsonObject.getString("code")))) {//01代表成功
            //添加日志
            log.error("商户信用账户开户失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        log.info("CRM商户入住BD系统回调成功，[params:{},result:{}]", JSON.toJSONString(params), res);

        //添加日志
        systemLogSupport.sellerEnterLogSave(sellerCode, ForeignInterfaceType.RES.getCode(), ForeignInterfaceServiceType.DESCRIBE_100.toString(), url, JSON.toJSONString(params), res, riskSuggestion);
    }


}
