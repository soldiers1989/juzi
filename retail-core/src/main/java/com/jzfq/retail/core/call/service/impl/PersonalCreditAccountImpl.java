package com.jzfq.retail.core.call.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.common.util.http.RetryHttpUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ErrorRetryTaskService;
import com.jzfq.retail.core.call.service.PersonalCreditAccountService;
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
 * @Title: PersonalCreditAccountImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月04日 17:44
 * @Description: 账务系统-个人信用账户调用操作实现
 */
@Slf4j
@Service
public class PersonalCreditAccountImpl implements PersonalCreditAccountService {

    @Value("${accounts.base_url}")
    private String baseUrl;

    @Value("${accounts.params.application}")
    private String application;

    @Value("${accounts.personal_credit_account.get_account_info}")
    private String getAccountInfoUrl;

    @Value("${accounts.personal_credit_account.recover}")
    private String recoverUrl;

    @Value("${accounts.personal_credit_account.debit}")
    private String debitUrl;

    @Autowired
    private ErrorRetryTaskService errorRetryTaskService;

    @Autowired
    private CallAccountsLog allAccountsLog;

    @Override
    public Map<String, Object> getAccountInfo(String certNo, String customerId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("application", application);
        params.put("certNo", certNo);
        params.put("customerId", customerId);
        String url = baseUrl + getAccountInfoUrl;
        log.info("调用个人信用账户-获取账户额度信息[url:{},params:{}]-->开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(params), "application/json", 60000);
        long e = System.currentTimeMillis();
        log.info("调用个人信用账户-获取账户额度信息-->结束,耗时[{}ms],返回结果:[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0038);
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            log.error("调用个人信用账户-获取账户额度信息失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0038);
        }
        Map<String, Object> result = jsonObject.getObject("result", Map.class);
        return result;
    }

    /**
     * 下单扣减信用额度
     * @param customerId 用户id
     * @param money      扣减金额
     * @param orderId    订单编号 -因为哪笔订单号冻结金额，传相关的订单号
     */
    @Override
    public void debit(Integer customerId, BigDecimal money, String orderId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("application", application);
        params.put("customerId", String.valueOf(customerId));
        params.put("orderId", orderId);
        params.put("money", money.toString());
        String url = baseUrl + debitUrl;
        log.info("调用个人信用账户-下单扣减信用额度接口[url:{},params:{}]-->开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
//        String res = HttpClientUtil.sendPost(url, JSON.toJSONString(params), HttpClientUtil.CONTENT_APPLICATION_FORM, 60000);
        String res = HttpClientUtil.sendPostMap(url, params);

        long e = System.currentTimeMillis();

        log.info("调用个人信用账户-下单扣减信用额度接口-->结束,耗时[{}ms],返回结果:[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0036);
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //添加日志
            allAccountsLog.addLog(orderId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_220.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.FAILED.getCode(), "调用个人信用账户-下单扣减信用额度接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            log.error("调用个人信用账户-下单扣减信用额度接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0036);
        }
        //添加日志
        allAccountsLog.addLog(orderId, ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_220.toString(), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "调用个人信用账户-下单扣减信用额度接口成功");
    }

    /**
     * 恢复信用额度
     *
     * @param customerId  身份证号 -
     * @param mdType  恢复额度的原因 -还款007；取消订单008；退货009；
     * @param money   恢复金额
     * @param orderId 订单号
     */
    @Override
    public void recoverByCertNo(String customerId, String mdType, BigDecimal money, String orderId) {
        Map<String, String> params = Maps.newHashMap();
        params.put("application", application);
        params.put("customerId", customerId);
//        params.put("certNo", certNo);
        params.put("mdType", mdType);//还款007；取消订单008；退货009；
        params.put("money", money.toString());
        params.put("orderId", orderId);
        String url = baseUrl + recoverUrl;
        log.info("调用个人信用账户-恢复额度接口[url:{},params:{}]-->开始", url, JSON.toJSONString(params));
        long s = System.currentTimeMillis();
        String res = null;
        try {
//            res = RetryHttpUtil.postStrWithJSON(url, JSON.toJSONString(params));
            res = RetryHttpUtil.post(url, params);
            //成功记录日志
            allAccountsLog.addLog(orderId, ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_210.getCode()), url, JSON.toJSONString(params), res, ForeignInterfaceStatus.SUCCESS.getCode(), "调用个人信用账户-恢复额度成功记录日志");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用个人信用账户-恢复额度接口: {}", e);
            //TODO 失败5次后后入库
            errorRetryTaskService.add(ErrorRetryTaskStatus.TYPE_10.getCode(), url, JSON.toJSONString(params), e.getMessage(), ErrorRetryTaskStatus.RETRY_COUNT.getCode(), 0, ErrorRetryTaskStatus.STATUS_ERROR.getCode());
        }

        long e = System.currentTimeMillis();
        log.info("调用个人信用账户-恢复额度接口-->结束,耗时[{}ms],返回结果:[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0037);
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            log.error("调用个人信用账户-恢复额度接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0037);
        }
    }
}
