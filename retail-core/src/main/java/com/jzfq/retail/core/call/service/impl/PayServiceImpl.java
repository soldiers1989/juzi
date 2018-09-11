package com.jzfq.retail.core.call.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.MD5Coding;
import com.jzfq.retail.common.util.http.HttpClientTool;
import com.jzfq.retail.common.util.http.HttpClientUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.call.domain.BankCardResult;
import com.jzfq.retail.core.call.domain.WXPay;
import com.jzfq.retail.core.call.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Title: PersonCreditAccountService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月09日 15:03
 * @Description: 支付系统操作接口
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Value("${pay.base_url}")
    private String BASE_URL;

    @Value("${pay.find_bank_card}")
    private String FIND_BANK_CARD;

    @Value("${pay.params.application}")
    private String application;

    @Value("${pay.params.wxSignKey}")
    private String wxSignKey;

    @Value("${pay.wx_pay}")
    private String wxPayUrl;

    private final static String BIND_TYPE = "order";

    //微信小程序或公众号支付 payChannel 固定值
    private final static String WX_PAY_CHANNEL = "swiftpassWx";

    /**
     * 获取个人绑卡信息 - 请求丁浩 核心支付接口
     *
     * @param customerId  用户id
     * @param capitalSide 资方编码
     */
    @Override
    public BankCardResult getBankCard(String customerId, String capitalSide) {
        String url = BASE_URL + FIND_BANK_CARD;
        Map<String, String> map = Maps.newHashMap();
        map.put("customerId", customerId);// 用户ID
        map.put("application", application);// shangcheng或baitiao
        map.put("capitalSide", capitalSide);// 资方编码
        map.put("bindType", BIND_TYPE);// 绑卡类型

        log.info("请求核心支付-查看用户绑卡接口：[url:{},params:{}]", url, map);
        String strResult = HttpClientUtil.getServiceResponseAsString(url, map);
        log.info("请求核心支付-查看用户绑卡接口：[url:{},params:{}],返回参数：[result:{}]", url, map, strResult);
        /*{
            "code": "000000",
            "msg": "操作成功",
            "data": {
                "cardNo": "6217931601047758",
                "phoneNumber": "18310188656",
                "certNo": "410724198810083517",
                "customerName": "杜鹏飞",
                "picture": "http://jzfq-fms-test.oss-cn-beijing.aliyuncs.com/img/1488268982782.png?Expires=1803628978&OSSAccessKeyId=LTAIi16Aj2Lcaqiv&Signature=aBCYlYDIOdbnteijI+cNS6PK4FI=",
                "backImg": "http://jzfq-fms-test.oss-cn-beijing.aliyuncs.com/img/1486980543547.png?Expires=1802340541&OSSAccessKeyId=LTAIi16Aj2Lcaqiv&Signature=5hHzYMxIzoRV2ghY78c2uca84B0=",
                "customerId": "210001030",
                "id": 7,
                "bankName": "上海浦东发展银行",
                "isDefault": "Y",
                "isSupport": "Y",
                "paySuppot": null
        }
        }*/
        if (StringUtils.isBlank(strResult)) {
            //throw new RuntimeException("请求核心支付-查看用户绑卡接口返回为空");
//            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0002);
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(strResult);
        if (!jsonObject.getString("code").equals("000000")) {
            log.error("请求核心支付-查看用户绑卡接口错误：[code:{},msg:{}]", jsonObject.getString("code"), jsonObject.getString("msg"));
            throw new RuntimeException("请求核心支付-查看用户绑卡接口繁忙");
        }
        BankCardResult bankCardResult = jsonObject.getObject("data", BankCardResult.class);
        return bankCardResult;
    }


    @Override
    public String wxPaymentConfirm(WXPay pay) {
        //默认值
        pay.setApplication(application);
        pay.setPayChannel(WX_PAY_CHANNEL);
        //获取签名
        String signStr = createSign(JSONObject.parseObject(JSON.toJSONString(pay)), Lists.newArrayList("tradeName", "sign", "payChannel"), wxSignKey);
        //添加签名
        pay.setSign(signStr);
        //请求地址
        String reqUrl = BASE_URL + wxPayUrl;
        //请求json参数
        Map<String, Object> reqParamMap = JSONObject.parseObject(JSON.toJSONString(pay)).getInnerMap();
        log.info("请求核心支付-微信小程序或公众号支付接口：[url:{},params:{}]", reqUrl, reqParamMap);
        String res = HttpClientTool.sendPostMap(reqUrl, reqParamMap);
        log.info("请求核心支付-微信小程序或公众号支付接口：[url:{},params:{}],返回参数：[result:{}]", reqUrl, reqParamMap, res);
        if (StringUtils.isBlank(res)) {
            throw new RuntimeException("请求核心支付-微信小程序或公众号支付接口返回为空");
        }
        JSONObject jsonObject = JSON.parseObject(res);
        if (!jsonObject.getString("code").equals("000000")) {
            log.error("请求核心支付-微信小程序或公众号支付接口错误：[code:{},msg:{}]", jsonObject.getString("code"), jsonObject.getString("msg"));
            throw new RuntimeException("请求核心支付-微信小程序或公众号支付接口繁忙");
        }
        //获取需要返回的值
        String result = jsonObject.getJSONObject("data").getJSONObject("swiftpassWxReturn").getString("url");
        //返回
        return result;
    }

    /**
     * 获取签名
     *
     * @param jsonObject
     * @param unSignKeyList
     * @param key
     * @return
     */
    private static String createSign(JSONObject jsonObject, List<String> unSignKeyList, String key) {
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.putAll(jsonObject);
        // 拼原String
        StringBuilder sb = new StringBuilder();
        // 删除不需要参与签名的属性
        if (null != unSignKeyList) {
            for (String str : unSignKeyList) {
                treeMap.remove(str);
            }
        }
        // 连接
        Iterator iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
            if (entry.getValue() != null && String.valueOf(entry.getValue()).trim().length() > 0) {
                sb.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5Coding.encodeForWx(sb.toString()).toUpperCase();
        log.info("微信小程序或公众号支付签名内容：{}，生成签名：{}", sb.toString(), sign);
        return sign;
    }
}
