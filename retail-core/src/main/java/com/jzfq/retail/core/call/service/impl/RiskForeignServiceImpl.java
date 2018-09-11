package com.jzfq.retail.core.call.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.http.HttpClientTool;
import com.jzfq.retail.common.util.http.RetryHttpUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ErrorRetryTaskService;
import com.jzfq.retail.core.call.domain.AssetEntryCapital;
import com.jzfq.retail.core.call.domain.OrderCheck;
import com.jzfq.retail.core.call.domain.RiskReceive;
import com.jzfq.retail.core.call.domain.RiskResult;
import com.jzfq.retail.core.call.service.RiskForeignService;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @title: RiskForeignServiceImpl
 * @description:
 * @company: 北京桔子分期电子商务有限公司
 * @author: Liu Wei
 * @date: 2018/7/2 15:53
 */
@Slf4j
@Service
public class RiskForeignServiceImpl implements RiskForeignService {

    @Value("${risk.base_url}")
    private String FK_URL;

    @Value("${risk.base.contact_url}")
    private String FK_CONTACT_URL;

    @Value("${risk.order_check}")
    private String FK_ORDER_CHECK;

    @Value("${risk.front_receive}")
    private String FK_FRONT_RECEIVE;

    @Value("${risk.contact_receive}")
    private String FK_CONTACT_RECEIVE;

    @Autowired
    private ErrorRetryTaskService errorRetryTaskService;

    @Autowired
    private SystemLogSupport systemLogSupport;

    /**
     * 进件前查看用户是否有进件资格（风控重构 替换checkMakeOrder接口）  准入接口
     * {
     * "gpsCity":"北京市",
     * "cardAddress":"四川省大竹县庙坝镇街道739号",
     * "amount":3,
     * "commodityChannel":1,
     * "rmsCustomerType":1,
     * "idCard":"513029198510273968",
     * "mobile":"15011259641",
     * "generalCity":"北京市",
     * "version":"v2.0.6",
     * "rmsProductType":11,
     * "generalCityCode":"110100",
     * "customerName":"杜鹃",
     * "orderTime":1,
     * "clientType":1,
     * "financialProductId":6,
     * "customerId":210001661,
     * "registCode":"V00025",
     * "gpsCityCode":"110000",
     * "operationType":2,
     * "merchantType":"4",
     * "gpsAddress":"北京市朝阳区小营北路靠近和泰大厦(小营北路)",
     * "age":32,
     * "channelId":2
     * }
     *
     * @param orderCheck
     * @return
     * @throws Exception
     */
    @Override
    public String fkOrderCheck(OrderCheck orderCheck) throws Exception {
        String url = FK_URL + FK_ORDER_CHECK;
        log.info("请求风控系统-调用准入接口:[url:{},params:{}]", url, JSON.toJSONString(orderCheck));
        long s = System.currentTimeMillis();
        String ss = null;
        try {
            Date reqTime = DateUtil.getDate();
            ss = RetryHttpUtil.postStrWithJSON(FK_URL + FK_ORDER_CHECK, JSON.toJSONString(orderCheck));
            //成功记录日志 call_risk_operation_log  int type, String serviceType, String url, String reqParam, String resParam, Date reqTime, Date resTime, String remark
            systemLogSupport.callRiskOperationLogSave(orderCheck.getOrderSn(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_190.getCode()), FK_URL + FK_ORDER_CHECK, JSON.toJSONString(orderCheck), ss, reqTime, DateUtil.getDate(), "风控准入成功");
        } catch (Exception e) {
            e.printStackTrace();log.error("请求风控系统-调用准入接口异常: {}", e);
            //TODO 失败5次后后入库
            errorRetryTaskService.add(ErrorRetryTaskStatus.TYPE_1.getCode(), FK_URL + FK_ORDER_CHECK, JSON.toJSONString(orderCheck), e.getMessage(), ErrorRetryTaskStatus.RETRY_COUNT.getCode(), 0, ErrorRetryTaskStatus.STATUS_ERROR.getCode());

        }
        long e = System.currentTimeMillis();
        log.info("请求风控系统-调用准入接口:[url:{},params:{},result:{}],耗时[{}ms]", url, JSON.toJSONString(orderCheck), ss, e - s);
        if (StringUtils.isBlank(ss)) {
            log.error("请求风控系统-调用准入接口返回为空");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0027);
        }

        /**
         * --------接口认证成功json示例------
         * {
         *       "code": 1,
         *       "data": "认证通过",
         *       "msg": "正常调用",
         *       "token": "19cf1f22e3782dcc97bb653abd047798",
         *       "traceID": ""
         *   }
         * --------认证不通过json示例--------
         * {
         *      "code": 2,
         *       "data": "{\"fore\":\"8\",\"fore_name\":\"未开通城市\",\"result\":\"1\",\"result_name\":\"订单退回\"}",
         *      "msg": "正常调用",
         *       "token": "",
         *       "traceID": ""
         *   }
         *
         */
        JSONObject jsonObject = JSONObject.parseObject(ss);
        Integer code = jsonObject.getInteger("code");
        //接口请求状态码** 1 认证通过 2 认证不通过 3 参数不完整 -1 异常 -60 IP拒绝
        if (code == 1) {
            return jsonObject.getString("token");
        } else if (code == 2) {
            //前端提示判断字段-----0.进件字段缺失；1.近期有拒绝订单；2.当前有在审订单；3.当前有在还订单；4.客户年龄不符；5.客户民族不符；6.客户身份证已过期；7.GPS未抓取；8.未开通城市；9.城市名额今日已满；
            RiskResult.InnerData msg = jsonObject.getObject("data", RiskResult.InnerData.class);
//            Integer type = msg.getFore();//提示状态码
//            String remark = msg.getFore_name();//提示语
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, msg.getFore_name());
            /*if (type == null) {
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, remark);
            } else if (type == 0) {//0.进件字段缺失
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, remark);
            } else if (type == 1) {//1.近期有拒绝订单
                log.error("风控下单检测失败.返回参数:" + ss);
                String date = msg.getDate();
                if (StringUtils.isNotBlank(date) && date.split("-").length > 2) {
                    String[] split = date.split("-");
                    throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0030, "请于" + split[0] + "年" + split[1] + "月" + split[2] + "日后再次下单，给您带来不便敬请谅解");
                } else {
                    throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0028);
                }
            } else if (type == 2) {//2.当前有在审订单
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800002.getErrMsg());
            } else if (type == 3) {//3.当前有在还订单
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800003.getErrMsg());
            } else if (type == 4) {//4.客户年龄不符
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800004.getErrMsg());
            } else if (type == 5) {//5.客户民族不符
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800005.getErrMsg());
            } else if (type == 6) {//6.客户身份证已过期
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800006.getErrMsg());
            } else if (type == 7) {//7.GPS未抓取
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800007.getErrMsg());
            } else if (type == 8) {//8.未开通城市
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800008.getErrMsg());
            } else if (type == 9) {//；9.城市名额今日已满
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_800009.getErrMsg());
            } else {
                log.error("风控下单检测失败.返回参数:" + ss);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0029, OrderErrCode.O_ERR_400000.getErrMsg());
            }*/
        } else {
            log.error("请求风控系统-调用准入接口失败:[code:{},msg:{}]", jsonObject.getInteger("code"), jsonObject.getString("msg"));
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0026);
        }
    }

    /**
     * 新订单进件风控审核（风控重构）
     * {
     * "loan":{
     * "firstPay":0.05,
     * "monthPay":0.33,
     * "loanType":5,
     * "rate":0.0075,
     * "contractAmount":0.94,
     * "accrual":0.01,
     * "productNum":1,
     * "productName":"1",
     * "loanAmount":0.95,
     * "loanPeriod":"3月"
     * },
     * "product":{
     * "clientType":2,
     * "financialProductId":1,
     * "operationType":2,
     * "commodityType":0,
     * "channelId":2
     * },
     * "images":[
     * {
     * "smallUrl":"https://juzibaitiao.oss-cn-beijing.aliyuncs.com/ios/210001661-20180704161705853.jpg",
     * "type":0,
     * "url":"https://juzibaitiao.oss-cn-beijing.aliyuncs.com/ios/210001661-20180704161705853.jpg"
     * },
     * {
     * "smallUrl":"https://juzibaitiao.oss-cn-beijing.aliyuncs.com/ios/210001661-20180704161728579.jpg",
     * "type":0,
     * "url":"https://juzibaitiao.oss-cn-beijing.aliyuncs.com/ios/210001661-20180704161728579.jpg"
     * },
     * {
     * "smallUrl":"https://juzibaitiao.oss-cn-beijing.aliyuncs.com/ios/210001661-20180704161839814.jpg",
     * "type":0,
     * "url":"https://juzibaitiao.oss-cn-beijing.aliyuncs.com/ios/210001661-20180704161839814.jpg"
     * },
     * {
     * "type":0
     * }
     * ],
     * "task":{
     * "signFlag":0,
     * "customerType":2,
     * "orderNo":"jz3812113",
     * "channel":"ios",
     * "isJxl":0,
     * "applyTime":"2018-07-10",
     * "uuid":"210001661",
     * "customerName":"杜鹃",
     * "creditType":2,
     * "productCategory":8,
     * "tdflag":0
     * },
     * "person":{
     * "certType":0,
     * "education":"0",
     * "homeCityCode":"110100",
     * "effectBeginDate":"2012-09-11",
     * "merchantName":"车主白条亚纶商户Test001CRM",
     * "homeProvinceCode":"110000",
     * "certCardNo":"513029198510273968",
     * "merchantId":1150,
     * "merchantGrade":"A+级",
     * "homeCity":"北京市",
     * "email":"",
     * "homeAddress":"时代凌宇大厦",
     * "homeDistrict":"东城区",
     * "commodityChannel":2,
     * "merchantCity":"北京市",
     * "merchantDistrict":"东城区",
     * "merchantProvince":"北京",
     * "sex":0,
     * "mobile":"15011259641",
     * "degree":"0",
     * "debitBank":"",
     * "birthDate":"1985-10-27",
     * "effectEndDate":"2032-09-11",
     * "homeDistrictCode":"110101",
     * "registAddress":"四川省大竹县庙坝镇街道739号",
     * "homeProvince":"北京",
     * "merchantAddress":"背后，",
     * "minzu":"汉"
     * },
     * "contacts":[
     * {
     * "phone":"13801001234",
     * "name":"时代",
     * "relation":1
     * },
     * {
     * "phone":"13801001233",
     * "name":"同事",
     * "relation":8
     * }
     * ],
     * "token":"913b58fb7842a029bcce27024f6c4f3d"
     * }
     *
     * @param riskReceive
     * @return
     * @throws Exception
     */
    @Override
    public void newRiskPushOrder(RiskReceive riskReceive) {
        try {
            String url = FK_URL + FK_FRONT_RECEIVE;
            log.info("请求风控系统-调用进件接口：[url:{},params:{}]", url, JSON.toJSONString(riskReceive));
            long s = System.currentTimeMillis();
            String res = null;
            try {
                res = HttpClientTool.sendPost(url, JSON.toJSONString(riskReceive), "application/json");
            } catch (Exception e) {
                e.printStackTrace();log.error("请求风控系统-调用进件接口异常: {}", e);
                //TODO 失败5次后后入库
                errorRetryTaskService.add(ErrorRetryTaskStatus.TYPE_1.getCode(), url, JSON.toJSONString(riskReceive), e.getMessage(), ErrorRetryTaskStatus.RETRY_COUNT.getCode(), 0, ErrorRetryTaskStatus.STATUS_ERROR.getCode());
            }
            long e = System.currentTimeMillis();
            log.info("请求风控系统-调用进件接口：[url:{},params:{},result:{}],耗时[{}ms]", url, JSON.toJSONString(riskReceive), res, e - s);
            if (org.apache.commons.lang3.StringUtils.isEmpty(res)) {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0040);
            }
            JSONObject jsonObject = JSONObject.parseObject(res);
            Integer code = jsonObject.getInteger("code");
            if (code != 1) {
                //添加日志
                systemLogSupport.callRiskOperationLogSave(riskReceive.getOrderSn(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_110.getCode()), FK_URL + FK_FRONT_RECEIVE, JSON.toJSONString(riskReceive), res, DateUtil.getDate(), DateUtil.getDate(), "风控进件失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0040, jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            }
            //成功记录日志
            systemLogSupport.callRiskOperationLogSave(riskReceive.getOrderSn(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_110.getCode()), FK_URL + FK_FRONT_RECEIVE, JSON.toJSONString(riskReceive), res, DateUtil.getDate(), DateUtil.getDate(), "风控进件成功");
        } catch (Exception e) {
            e.printStackTrace();log.error("请求风控系统-调用进件接口异常: {}", e);
            throw e;
        }
    }

    @Override
    public List<AssetEntryCapital.EntryTelbooks> getEntryTelbooks(String idCard, String name, String mobile, String orderSn) {
        // 返回对象
        List<AssetEntryCapital.EntryTelbooks> list = new ArrayList<>();
        try {
            String url = FK_CONTACT_URL + FK_CONTACT_RECEIVE;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("idCard", idCard);
            paramMap.put("name", name);
            paramMap.put("mobile", mobile);
            log.info("请求风控系统-调用通讯录接口：[url:{},params:{}]", url, JSON.toJSONString(paramMap));
            long s = System.currentTimeMillis();
            String res = null;
            try {
                res = HttpClientTool.sendPost(url, JSON.toJSONString(paramMap), "application/json");
            } catch (Exception e) {
                e.printStackTrace();log.error("请求风控系统-调用通讯接口异常: {}", e);
                //TODO 失败5次后后入库
                errorRetryTaskService.add(ErrorRetryTaskStatus.TYPE_11.getCode(), url, JSON.toJSONString(paramMap), e.getMessage(), ErrorRetryTaskStatus.RETRY_COUNT.getCode(), 0, ErrorRetryTaskStatus.STATUS_ERROR.getCode());
            }
            long e = System.currentTimeMillis();
            log.info("请求风控系统-调用通讯录接口：[url:{},params:{},result:{}],耗时[{}ms]", url, JSON.toJSONString(paramMap), res, e - s);
            if (org.apache.commons.lang3.StringUtils.isEmpty(res)) {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0048);
            }
            JSONObject jsonObject = JSONObject.parseObject(res);
            Integer code = jsonObject.getInteger("code");
            if (code != 1) {
                //添加日志
                systemLogSupport.callRiskOperationLogSave(orderSn, ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_260.getCode()), FK_CONTACT_URL + FK_CONTACT_RECEIVE, JSON.toJSONString(paramMap), res, DateUtil.getDate(), DateUtil.getDate(), "获取通讯录失败，code:" + jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0048, jsonObject.getInteger("code") + "，msg:" + jsonObject.getString("msg"));
            }

            // 解析通讯数据据
            JSONObject attachJson = jsonObject.getJSONObject("attach");
            if(attachJson != null) {
                JSONArray jsonArray = attachJson.getJSONArray("contacts");
                if(jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject contactJson = jsonArray.getJSONObject(i);
                        AssetEntryCapital.EntryTelbooks books = new AssetEntryCapital.EntryTelbooks();
                        books.setContactsName(contactJson.getString("contactsName"));
                        books.setContactsPhone(contactJson.getString("contactsPhone"));
                        list.add(books);
                    }
                }
            }
            //成功记录日志
            systemLogSupport.callRiskOperationLogSave(orderSn, ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_260.getCode()), FK_CONTACT_URL + FK_CONTACT_RECEIVE, JSON.toJSONString(paramMap), res, DateUtil.getDate(), DateUtil.getDate(), "获取通讯录成功");
        } catch (Exception e) {
            e.printStackTrace();log.error("请求风控系统-调用通讯接口异常: {}", e);
            throw e;
        }
        return list;
    }
}
