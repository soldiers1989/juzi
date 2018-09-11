package com.jzfq.retail.core.call.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jzfq.retail.common.enmu.ErrorRetryTaskStatus;
import com.jzfq.retail.common.enmu.ForeignInterfaceServiceType;
import com.jzfq.retail.common.enmu.ForeignInterfaceType;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.http.HttpClientTool;
import com.jzfq.retail.common.util.http.RetryHttpUtil;
import com.jzfq.retail.core.api.service.ErrorRetryTaskService;
import com.jzfq.retail.core.call.domain.AssetBankCapital;
import com.jzfq.retail.core.call.domain.AssetBankCapitalResult;
import com.jzfq.retail.core.call.domain.AssetEntryCapital;
import com.jzfq.retail.core.call.service.AssetPlatformService;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Title: AssetPlatformServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月04日 13:49
 * @Description: 资产平台系统调用操作接口实现
 */
@Slf4j
@Service
public class AssetPlatformServiceImpl implements AssetPlatformService {

    @Value("${assets.base_url}")
    private String baseUrl;

    @Value("${assets.params.application}")
    private String application;

    @Value("${assets.bank_capital}")
    private String bankCapitalUrl;

    @Value("${assets.entry_capital}")
    private String entryCapitalUrl;

    @Value("${assets.close_order}")
    private String closeOrderUrl;

    @Autowired
    private ErrorRetryTaskService errorRetryTaskService;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Override
    public AssetBankCapitalResult bankCapital(AssetBankCapital bankCapital) {
        String url = baseUrl + bankCapitalUrl;
        long s = System.currentTimeMillis();
        log.info("调用资产平台资金路由-绑卡[url:{},params:{}]-->开始", url, JSON.toJSONString(bankCapital));
        String res = HttpClientTool.sendPost(url, JSON.toJSONString(bankCapital), "application/json");
        long e = System.currentTimeMillis();
        log.info("调用资产平台资金路由-绑卡-->结束,耗时[{}ms],接口返回：[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用资产平台资金路由-绑卡接口返回值为空");
        }
        JSONObject jsonObject = JSON.parseObject(res);
        Boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //成功记录日志
            systemLogSupport.callAssetsOperationLogSave(bankCapital.getOrderId(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_160.getCode()), url, JSON.toJSONString(bankCapital), res, DateUtil.getDate(), DateUtil.getDate(), "调用资产平台资金路由-绑卡接口错误：code:" + jsonObject.getString("code") + "，msg:" + jsonObject.getString("msg"));
            throw new RuntimeException("调用资产平台资金路由-绑卡接口错误：code:" + jsonObject.getString("code") + "，msg:" + jsonObject.getString("msg"));
        }
        AssetBankCapitalResult result = jsonObject.getObject("result", AssetBankCapitalResult.class);
        systemLogSupport.callAssetsOperationLogSave(bankCapital.getOrderId(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_160.getCode()), url, JSON.toJSONString(bankCapital), res, DateUtil.getDate(), DateUtil.getDate(), "调用资产平台资金路由-绑卡接口成功");
        return result;
    }

    @Override
    public void entryCapital(AssetEntryCapital entryCapital) {
        String url = baseUrl + entryCapitalUrl;
        long s = System.currentTimeMillis();
        log.info("调用资产平台资金路由-进件[url:{},params:{}]-->开始", url, JSON.toJSONString(entryCapital));
        String res = HttpClientTool.sendPost(url, JSON.toJSONString(entryCapital), "application/json");
        long e = System.currentTimeMillis();
        log.info("调用资产平台资金路由-进件-->结束,耗时[{}ms],接口返回：[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用资产平台资金路由-进件接口返回值为空");
        }
        JSONObject jsonObject = JSON.parseObject(res);
        Boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            //记录日志
            systemLogSupport.callAssetsOperationLogSave(entryCapital.getOrderId(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_170.getCode()), url, JSON.toJSONString(entryCapital), res, DateUtil.getDate(), DateUtil.getDate(), "调用资产平台资金路由-进件接口错误：code:" + jsonObject.getString("code") + "，msg:" + jsonObject.getString("msg"));
            throw new RuntimeException("调用资产平台资金路由-进件接口错误：code:" + jsonObject.getString("code") + "，msg:" + jsonObject.getString("msg"));
        }
        //成功记录日志
        systemLogSupport.callAssetsOperationLogSave(entryCapital.getOrderId(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_170.getCode()), url, JSON.toJSONString(entryCapital), res, DateUtil.getDate(), DateUtil.getDate(), "调用资产平台资金路由-进件接口成功");

    }

    @Override
    public void closeOrder(String orderId) {
        String url = baseUrl + closeOrderUrl + "?orderId=" + orderId;
        long s = System.currentTimeMillis();
        log.info("调用资产平台退货/闭单接口[url:{},params:{}]-->开始,订单号:{}", url, "orderId:" + orderId);
        //String res = HttpClientUtil.sendGet(url + "?orderId=" + orderId);
        String res = null;
        try {
            res = RetryHttpUtil.getBySSL(url);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用资产平台退货/闭单接口异常：{}", e);
            //TODO 失败5次后后入库
            errorRetryTaskService.add(ErrorRetryTaskStatus.TYPE_9.getCode(), url, orderId, e.getMessage(), ErrorRetryTaskStatus.RETRY_COUNT.getCode(), 0, ErrorRetryTaskStatus.STATUS_ERROR.getCode());
        }

        long e = System.currentTimeMillis();
        log.info("调用资产平台退货/闭单接口-->结束,耗时[{}ms],接口返回：[{}]", e - s, res);
        if (StringUtils.isEmpty(res)) {
            throw new RuntimeException("调用资产平台退货/闭单接口返回值为空");
        }
        JSONObject jsonObject = JSON.parseObject(res);
        Boolean success = jsonObject.getBoolean("success");
        if (!(success && "100000".equals(jsonObject.getString("code")))) {
            throw new RuntimeException("调用资产平台退货/闭单接口错误：code:" + jsonObject.getString("code") + "，msg:" + jsonObject.getString("msg"));
        }
        //成功记录日志
        systemLogSupport.callAssetsOperationLogSave(orderId, ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_180.getCode()), url, orderId, res, DateUtil.getDate(), DateUtil.getDate(), "调用资产平台退货/闭单接口成功记录日志");

    }
}
