package com.jzfq.retail.core.call.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.jzfq.retail.common.enmu.ForeignInterfaceServiceType;
import com.jzfq.retail.common.enmu.ForeignInterfaceStatus;
import com.jzfq.retail.common.enmu.ForeignInterfaceType;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.call.domain.WithdrawCash;
import com.jzfq.retail.core.call.service.WithdrawAccountService;
import com.jzfq.retail.core.service.CallAccountsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: RiskForeignServiceImpl
 * @description:
 * @company: 北京桔子分期电子商务有限公司
 * @author: Liu XueLiang
 * @date: 2018/8/31 17:34
 */
@Slf4j
@Service
public class WithdrawAccountServiceImpl implements WithdrawAccountService {

    @Value("${accounts.base_url}")
    private String BASE_URL;

    @Value("${accounts.merchant_account.application}")
    private String APPLICATION;

    @Value("${accounts.merchant_account.checkConfirm}")
    private String CHECK_CONFIRM;

    @Value("${accounts.merchant_account.withdrawCash}")
    private String WITHDRAW_CASH;

    @Value("${accounts.merchant_account.getMerchantAccount}")
    private String GET_MERCHANT_ACCOUNT;

    @Autowired
    private CallAccountsLog allAccountsLog;

    @Override
    public void checkConfirm(String orderSn) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("application", APPLICATION);
        params.put("orderIds", orderSn);
        String url = BASE_URL + CHECK_CONFIRM;
        log.info("调用商户订单核账[url:{},params:{}]-->开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
        String res = HttpClientUtil.sendPostMap(url, params);
        long e = System.currentTimeMillis();
        log.info("调用商户订单核账-->结束,耗时[{}ms],返回结果:[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1400);
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            log.error("调用商户订单核账-失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1401 + "," + jsonObject.getString("msg"));
        }
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        JSONObject jb = jsonArray.getJSONObject(0);
        String status = jb.getString("status");
        if("F".equals(status)){
            log.error("调用商户订单核账-失败，orderId:" + jb.getString("orderId") + "，msg:" + jb.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1401 + "," + jb.getString("msg"));
        }
        //添加日志
        allAccountsLog.addLog(orderSn, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_280.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "调用商户订单核账接口成功");
    }

    @Override
    public void withdrawCash(WithdrawCash withdrawCash) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("application", APPLICATION);
        params.put("amount", withdrawCash.getAmount());
        params.put("businessId", withdrawCash.getBusinessId());
        params.put("merchant", withdrawCash.getMerchant());
        params.put("withdrawId", withdrawCash.getWithdrawId());
        String url = BASE_URL + WITHDRAW_CASH;
        log.info("调用商户账户提现[url:{},params:{}]-->开始", url, JSON.toJSONString(withdrawCash));
        long s = System.currentTimeMillis();
//        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(withdrawCash), "application/json", 60000);
        String res = HttpClientUtil.sendPostMap(url, params);
        long e = System.currentTimeMillis();
        log.info("调用商户账户提现-->结束,耗时[{}ms],返回结果:[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1402);
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            log.error("调用商户账户提现-失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1403, "," + jsonObject.getString("msg"));
        }
        //添加日志
        allAccountsLog.addLog(withdrawCash.getWithdrawId(), ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_290.toString(), url, JSON.toJSONString(withdrawCash), res, ForeignInterfaceStatus.SUCCESS.getCode(), "调用商户账户提现接口成功");
    }

    @Override
    public Map<String, Object> getMerchantAccount(String businessIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("application", APPLICATION);
        params.put("businessIds", businessIds);
        String url = BASE_URL + GET_MERCHANT_ACCOUNT;
        log.info("调用商户资金账户查询[url:{},params:{}]-->开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
        String res = HttpClientUtil.sendPostMap(url, params);
        long e = System.currentTimeMillis();
        log.info("调用商户资金账户查询-->结束,耗时[{}ms],返回结果:[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1404);
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            log.error("调用商户资金账户查询-失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1405);
        }
        JSONArray result1 = jsonObject.getJSONArray("result");
        Map<String, Object> result = new HashMap<>();
        for(int i=0;i<result1.size();i++){
            JSONObject jb = result1.getJSONObject(i);
            String sellerCode = (String) jb.get("businessId");
            Double validAmount = Double.parseDouble(jb.get("validAmount").toString());
            result.put(sellerCode, validAmount);
        }
        return result;
    }
}
