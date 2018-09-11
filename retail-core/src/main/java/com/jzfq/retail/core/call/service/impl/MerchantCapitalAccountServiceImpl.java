package com.jzfq.retail.core.call.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jzfq.retail.common.enmu.ForeignInterfaceServiceType;
import com.jzfq.retail.common.enmu.ForeignInterfaceStatus;
import com.jzfq.retail.common.enmu.ForeignInterfaceType;
import com.jzfq.retail.common.util.http.HttpClientTool;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.call.service.MerchantCapitalAccountService;
import com.jzfq.retail.core.service.CallAccountsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Title: MerchantCapitalAccountServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月03日 16:12
 * @Description: 资金账户系统调用操作实现
 */
@Slf4j
@Service
public class MerchantCapitalAccountServiceImpl implements MerchantCapitalAccountService {

    @Value("${accounts.base_url}")
    private String baseUrl;

    @Value("${accounts.params.application}")
    private String application;

    @Value("${accounts.merchant_capital_account.init_account}")
    private String initAccount;

    @Value("${accounts.merchant_capital_account.bind_bankcard}")
    private String bindBankcard;

    @Autowired
    private CallAccountsLog callAccountsLog;

    @Override
    public void initAccount(String businessId, String name) {
        String url = baseUrl + initAccount;
        Map<String, String> params = Maps.newHashMap();
        params.put("application", application);
        params.put("businessId", businessId);
        params.put("name", name);
        log.info("调用商户资金账户系统开户接口[url:{},params:{}]----->开始", url, JSON.toJSONString(params));
        String res = HttpClientTool.sendPost(url, params);
        //HttpClientUtil.sendPost(url, JSON.toJSONString(params), "application/json", 60000);
        log.info("调用商户资金账户系统开户接口----->结束,返回结果：{}", res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用商户资金账户系统开户接口返回空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //添加日志
            callAccountsLog.addLog(businessId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_130.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.FAILED.getCode(), "商户资金开户失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new RuntimeException("调用商户资金账户系统开户接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        //添加日志
        callAccountsLog.addLog(businessId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_130.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "商户资金开户成功");
    }

    @Override
    public void bindBankcardPub(String bankCardNo, Integer bankCode, String name, String refId,String payeeBankAssociatedCode, String payeeBankFullName) {
        String url = baseUrl + bindBankcard;
        Map<String, String> params = Maps.newHashMap();
        params.put("application", application);
        params.put("bankCardNo", bankCardNo);
        params.put("bankCode", bankCode.toString());
        params.put("name", name);
        params.put("refId", refId);
        params.put("payeeBankAssociatedCode", payeeBankAssociatedCode);
        params.put("payeeBankFullName", payeeBankFullName);
        params.put("isPublic", "1");
        log.info("调用商户资金账户系统绑卡接口[url:{},params:{}]----->开始", url, JSON.toJSONString(params));
        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(params), "application/json", 60000);
        log.info("调用商户资金账户系统绑卡接口----->结束,返回结果：{}", res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用商户资金账户系统绑卡接口返回空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //添加日志
            callAccountsLog.addLog(refId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_140.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.FAILED.getCode(), "商户资金绑卡失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new RuntimeException("调用商户资金账户系统绑卡接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        //添加日志
        callAccountsLog.addLog(refId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_140.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "商户资金绑卡成功");
    }

    @Override
    public void bindBankcardPri(String bankCardNo, Integer bankCode, String name, String certNo, String bankPhone,String refId) {
        String url = baseUrl + bindBankcard;
        Map<String, String> params = Maps.newHashMap();
        params.put("application", application);
        params.put("bankCardNo", bankCardNo);
        params.put("bankCode", bankCode.toString());
        params.put("name", name);
        params.put("certNo", certNo);
        params.put("bankPhone", bankPhone);
        params.put("isPublic", "0");
        params.put("refId",refId);
        log.info("调用商户资金账户系统绑卡接口[url:{},params:{}]----->开始", url, JSON.toJSONString(params));
        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(params), "application/json", 60000);
        log.info("调用商户资金账户系统绑卡接口----->结束,返回结果：{}", res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用商户资金账户系统绑卡接口返回空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //添加日志
            callAccountsLog.addLog(certNo, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_140.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.FAILED.getCode(), "商户资金绑卡失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new RuntimeException("调用商户资金账户系统绑卡接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        //添加日志
        callAccountsLog.addLog(certNo, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_140.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "商户资金绑卡成功");
    }
}
