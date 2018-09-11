package com.jzfq.retail.core.call.service;

import com.jzfq.retail.core.call.domain.GetRepaymentResult;

import java.util.Map;

/**
 * @Title: BillingService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月04日 11:33
 * @Description: 账单系统调用操作接口
 */
public interface BillingService {

    /**
     * 查询近七日待还 30日待还和全部待还
     *
     * @param customerId  用戶id
     * @param certNo      身份证号
     * @param indays      近N天待还 -全部待还不需要传；
     * @param state       还款计划状态 -0未还和逾期；1已还
     * @param currentPage 当前页
     * @param pageSize    每页多少条
     * @return 返回分页的还款记录
     */
    Map<String, Object> repayments(Integer customerId, String certNo, Integer indays, Integer state, Integer currentPage, Integer pageSize);


    /**
     * 根据顶大好和期数查询还款计划
     *
     * @param orderId 订单号
     * @param period  还款期数
     * @return
     */
    GetRepaymentResult getRepayment(String orderId, Integer period);
}
