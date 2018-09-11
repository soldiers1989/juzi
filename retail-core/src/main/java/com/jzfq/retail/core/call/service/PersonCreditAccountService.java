package com.jzfq.retail.core.call.service;

import com.alibaba.fastjson.JSONObject;
import com.jzfq.retail.core.call.domain.PersonAuthenticationResult;

import java.math.BigDecimal;

/**
 * @Title: PersonCreditAccountService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月09日 15:03
 * @Description: 商户信用账户操作接口
 */
public interface PersonCreditAccountService {

    /**
     * 判断个人用户是否认证
     *
     */
    PersonAuthenticationResult checkPersonAuthentication(String customerId);

    /**
     * 还款分期试算
     *
     */
    JSONObject getPlanTrial(BigDecimal firstPayRate, BigDecimal itemPrice, Integer period, Double rate) throws Exception;
}
