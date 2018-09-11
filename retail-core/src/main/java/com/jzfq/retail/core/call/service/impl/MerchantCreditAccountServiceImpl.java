package com.jzfq.retail.core.call.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jzfq.retail.common.enmu.ForeignInterfaceServiceType;
import com.jzfq.retail.common.enmu.ForeignInterfaceStatus;
import com.jzfq.retail.common.enmu.ForeignInterfaceType;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.call.domain.MerchantAccountInfoResult;
import com.jzfq.retail.core.call.service.MerchantCreditAccountService;
import com.jzfq.retail.core.service.CallAccountsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Title: MerchantCreditAccountServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月03日 15:13
 * @Description: 商户信用账户操作接口实现
 */
@Slf4j
@Service
public class MerchantCreditAccountServiceImpl implements MerchantCreditAccountService {

    @Value("${accounts.base_url}")
    private String baseUrl;

    @Value("${accounts.params.application}")
    private String application;

    @Value("${accounts.merchant_credit_account.init_account}")
    private String initAccount;

    @Value("${accounts.merchant_credit_account.get_account_info}")
    private String getAccountInfoUrl;

    @Autowired
    private CallAccountsLog callAccountsLog;

    @Override
    public void initMerchantAccount(Date approvedDate, String businessId, BigDecimal firstMonthQuota, BigDecimal monthQuota, Date signDateStart, BigDecimal totalQuota) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("application", application);
        params.put("approvedDate", approvedDate);
        params.put("businessId", businessId);
        params.put("firstMonthQuota", firstMonthQuota);
        params.put("monthQuota", monthQuota);
        params.put("signDateStart", signDateStart);
        params.put("totalQuota", totalQuota);
        String url = baseUrl + initAccount;
        log.info("请求商户信用账户开接口[url:{},params:{}]-----开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(params), "application/json", 60000);
        long e = System.currentTimeMillis();
        log.info("请求商户信用账户开户-----结束,耗时【{}ms】,返回结果：【{}】", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("请求商户信用账户开户返回结果为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //添加日志
            callAccountsLog.addLog(businessId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_120.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.FAILED.getCode(), "商户信用账户开户失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new RuntimeException("商户信用账户开户失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        //添加日志
        callAccountsLog.addLog(businessId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_120.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "开户成功");
    }

    @Override
    public MerchantAccountInfoResult getMerchantAccountInfo(String businessId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("application", application);
        params.put("businessId", businessId);
        String url = baseUrl + getAccountInfoUrl;
        log.info("调用获取商户信用账户信息接口[url:{},params:{}]-----开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(params), "application/json", 60000);
        long e = System.currentTimeMillis();
        log.info("调用获取商户信用账户信息接口-----结束,耗时【{}ms】,返回结果：【{}】", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用获取商户信用账户信息接口返回结果为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //添加日志
            callAccountsLog.addLog(businessId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_240.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.FAILED.getCode(), "获取商户信用账户信息失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new RuntimeException("获取商户信用账户信息失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        //添加日志
        callAccountsLog.addLog(businessId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_240.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "获取商户信用账户信息成功");
        //返回额度
        MerchantAccountInfoResult infoResult = jsonObject.getObject("result", MerchantAccountInfoResult.class);
        return infoResult;
    }
}
