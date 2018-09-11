package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.vo.req.SellerLoginReq;
import com.jzfq.retail.core.api.exception.TouchCodeException;

import java.util.Map;

/**
 * @Title: SellerLoginService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 17:24
 * @Description: 商户登录操作接口
 */
public interface SellerLoginService {


    /**
     * 小程序商户端商户登录
     *
     * @param req
     * @return
     */
    Map<String, Object> login(SellerLoginReq req) throws TouchCodeException;

    /**
     * 登录失败处理
     *
     * @param openId      微信openID
     * @param sellerLogin 商户登录编号
     * @param loginStatus 登录状态
     */
    boolean loginHandle(String openId, String sellerLogin, boolean loginStatus);

}
