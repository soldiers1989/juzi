package com.jzfq.retail.core.call.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jzfq.retail.common.util.http.HttpClientTool;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.call.domain.GetRepaymentResult;
import com.jzfq.retail.core.call.service.BillingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Title: BillingServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月04日 15:36
 * @Description:
 */
@Slf4j
@Service
public class BillingServiceImpl implements BillingService {

    @Value("${accounts.base_url}")
    private String baseUrl;

    @Value("${accounts.params.application}")
    private String application;

    @Value("${accounts.billing.repayments}")
    private String repaymentsUrl;

    @Value("${accounts.billing.get_repayment}")
    private String getRepaymentUrl;


    @Override
    public Map<String, Object> repayments(Integer customerId, String certNo, Integer indays, Integer state, Integer currentPage, Integer pageSize) {
        String url = baseUrl + repaymentsUrl;
        Map<String, String> params = Maps.newHashMap();
        params.put("appChannel", application);
        params.put("customerId", customerId == null ? null : customerId.toString());
        params.put("certNo", certNo);
        params.put("indays", indays == null ? null : indays.toString());
        params.put("state", state == null ? null : state.toString());
        params.put("currentPage", currentPage == null ? null : currentPage.toString());
        params.put("pageSize", pageSize == null ? null : pageSize.toString());
        log.info("调用新账务系统账单-查询账单接口[url:{},params:{}]----->开始", url, JSON.toJSONString(params));
        String res = HttpClientTool.sendPost(url, params);  //HttpClientUtil.sendPost(url, JSON.toJSONString(params), HttpClientUtil.CONTENT_APPLICATION_JSON, 60000);
        log.info("调用新账务系统账单-查询账单接口----->结束,返回结果：{}", res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用新账务系统账单-查询账单接口返回空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            throw new RuntimeException("调用新账务系统账单-查询账单接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        Map<String, Object> result = jsonObject.getObject("result", Map.class);
        return result;
    }


    @Override
    public GetRepaymentResult getRepayment(String orderId, Integer period) {
        String url = baseUrl + getRepaymentUrl;
        Map<String, String> params = Maps.newHashMap();
        params.put("orderId", orderId);
        params.put("period", period == null ? null : period.toString());
        log.info("调用新账务系统账单-根据订单号和期数查询还款计划接口[url:{},params:{}]----->开始", url, JSON.toJSONString(params));
        String res = HttpClientTool.sendPost(url, params);  //HttpClientUtil.sendPost(url, JSON.toJSONString(params), HttpClientUtil.CONTENT_APPLICATION_JSON, 60000);
        log.info("调用新账务系统账单-根据订单号和期数查询还款计划接口----->结束,返回结果：{}", res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用新账务系统账单-根据订单号和期数查询还款计划接口返回空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            throw new RuntimeException("调用新账务系统账单-根据订单号和期数查询还款计划接口失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
        }
        GetRepaymentResult result = jsonObject.getObject("result", GetRepaymentResult.class);
        return result;
    }
}
