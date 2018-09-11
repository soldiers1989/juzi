package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.ScanCodeRecord;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.exception.TouchCodeException;

import java.util.Map;

/**
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月6日 14:43
 * @Description: 商户端小程序service接口
 */
public interface ShopBusiService {

    /**
     * 调用后台查询是否注册
     *
     * @param req
     * @return
     */
    void checkRegister(CheckRegisterReq req) throws Exception;

    /**
     * 进入确认订单接口
     *
     * @param req
     * @return
     */
    Map<String, Object> intoConfirmOrder(IntoConfirmOrderReq req) throws Exception;

    /**
     * 点击提交订单接口
     *
     * @param req
     */
    void commitOrder(CommitOrderReq req) throws Exception;

    /**
     * 进入信用支付
     * 小程序-商户端-传递订单号，后台查询订单的相关信息及是否绑卡和是否已设置交易密码的信息，并返回小程序
     *
     * @param orderSn 订单编号
     * @param lng     经度
     * @param lat     纬度
     * @return
     * @throws Exception
     */
    Map<String, Object> intoOrderPay(String orderSn, Double lng, Double lat) throws Exception;

    /**
     * 输入交易密码后提交
     *
     * @param req
     * @return
     * @throws Exception
     */
    void commitPay(CommitPayReq req);

    /**
     * 认证成功后回调
     *
     * @param wxCode
     * @return
     */
    void authSucBack(String wxCode);

    /**
     * TODO:缺少注释
     *
     * @param scanCodeRecord
     */
    void saveScanCodeRecord(ScanCodeRecord scanCodeRecord);

    /**
     * 通过微信code和筛选条件获取订单列表
     *
     * @param page
     * @param pageSize
     * @param status
     * @param wxcode
     * @return
     */
    ListResultRes<Map<String, Object>> findOrderListByWxcode(Integer page, Integer pageSize, Integer[] status, String wxcode) throws TouchCodeException;

}
