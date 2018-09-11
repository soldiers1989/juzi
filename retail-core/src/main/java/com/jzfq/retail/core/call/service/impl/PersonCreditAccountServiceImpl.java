package com.jzfq.retail.core.call.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jzfq.retail.common.enmu.OrderErrCode;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.http.HttpClientTool;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.call.domain.PersonAuthenticationResult;
import com.jzfq.retail.core.call.service.PersonCreditAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: PersonCreditAccountService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月09日 15:03
 * @Description: 个人信用账户操作接口
 */
@Slf4j
@Service
public class PersonCreditAccountServiceImpl implements PersonCreditAccountService {

    @Value("${coresys.base_url}")
    private String BASE_URL;

    @Value("${coresys.fms_url}")
    private String FMS_URL;

    @Value("${coresys.plan_trial}")
    private String PLAN_TRIAL;

    @Value("${coresys.params.application}")
    private String application;

    /**
     * 判断个人用户是否认证
     */
    @Override
    public PersonAuthenticationResult checkPersonAuthentication(String customerId) {
        Map<String, String> params = new HashMap<>();
        params.put("customerId", customerId);// 用户ID
        params.put("application", application);// shangcheng或baitiao
        String url = BASE_URL + FMS_URL;
        log.info("请求核心接口-判断用户是否认证：[url={},params={}]", url, params);
        String res = HttpClientTool.sendPost(url, params);//HttpClientUtil.sendPost(url, JSON.toJSONString(params), "x-www-form-urlencoded", 60000);
        log.info("请求核心接口-判断用户是否认证返回：[url={},params={},result={}]", url, params, res);
        /*{
         "result": {
                 "date": "2017-11-03 18:11:59",
                 "validAmount": "9000.00",
                 "code": "2",    //0未认证 1认证审核中  2认证审核成功  3认证审核失败
                 "name": "认证审核成功",
                 "creditAmount": "9000.00",
                 "applyDate": "2017-11-03 15:43:58",
                 "activated" : "",  // 1：已激活  2：未激活  3：冻结
                 },
         "msg": "正常返回",
         "code": "100000",
         "success": true
         }*/
        if (StringUtils.isBlank(res)) {
            throw new RuntimeException("请求核心接口-判断用户是否认证返回为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            log.error("请求核心接口-判断用户是否认证失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0006);
        }
        PersonAuthenticationResult jsonResult = jsonObject.getObject("result", PersonAuthenticationResult.class);
        return jsonResult;
    }

    /**
     * 分期试算
     *
     * @param firstPayRate 首付比例
     * @param itemPrice    订单价格
     * @param period       分期数
     * @param rate         月息
     * @return
     */
    @Override
    public JSONObject getPlanTrial(BigDecimal firstPayRate, BigDecimal itemPrice, Integer period, Double rate) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("behead", "0");
        params.put("firstPayRate", firstPayRate.toString());
        params.put("itemPrice", itemPrice.toString());
        params.put("period", period.toString());
        params.put("type", "2");
        params.put("rate", rate.toString());
        //请求地址
        String url = BASE_URL + PLAN_TRIAL;
        // 处理传递参数
        long s = System.currentTimeMillis();
        log.info("请求核心接口-分期试算：[url:{},params:{}]", url, params);
        String res = HttpClientTool.sendPost(url, params);//HttpClientUtil.sendJsonPost(url, JsonMapper.toJsonString(sb));
        long e = System.currentTimeMillis();
        log.info("请求核心接口-分期试算返回：[url:{},params:{}]，耗时[{}ms],接口返回：[{}]", url, params, e - s, res);
        if (StringUtils.isBlank(res)) {
            log.error("请求核心接口-分期试算返回为空");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0014);
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            log.error("请求核心接口-分期试算失败：code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            //throw new RuntimeException("请求核心接口-分期试算失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0006);
        }
        JSONObject result = jsonObject.getJSONObject("result");
        if (result == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0006);
        }
        //periodMoney = result.getBigDecimal("loanAmount");//分期金额
        //BigDecimal serviceMoney = result.getBigDecimal("mInterest");//月服务费
        // BigDecimal perAmount = result.getBigDecimal("mPrincipal");//月本金
        //monthPayment = result.getBigDecimal("perdPrcpAmt");//月供 = 月本金 + 月服务费
        /*
        {
            "period": 12,
                "mPrincipal": 274.00, //月本金
                "actualAmount": 3288.00,
                "behead": 0,
                "perdPrcpAmt": 313.46, //月供 = 月本金 + 月服务费
                "mInterest": 39.46, //月服务费
                "type": 2,
                "repayPlans": [{
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 1,
                    "repayDay": "2018-08-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 2,
                    "repayDay": "2018-09-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 3,
                    "repayDay": "2018-10-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 4,
                    "repayDay": "2018-11-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 5,
                    "repayDay": "2018-12-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 6,
                    "repayDay": "2019-01-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 7,
                    "repayDay": "2019-02-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 8,
                    "repayDay": "2019-03-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 9,
                    "repayDay": "2019-04-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 10,
                    "repayDay": "2019-05-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 11,
                    "repayDay": "2019-06-09"
        }, {
            "repayMoney": 313.46,
                    "monthPrincipal": 274.00,
                    "monthInterest": 39.46,
                    "curPeriod": 12,
                    "repayDay": "2019-07-09"
        }],
            "loanAmount": 3288.00,  //分期金额
                "sumInterest": 473.52
        }
        */
        return result;
    }
}
