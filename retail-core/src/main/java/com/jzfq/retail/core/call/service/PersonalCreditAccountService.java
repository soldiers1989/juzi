package com.jzfq.retail.core.call.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Title: PersonalCreditAccountService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月04日 16:47
 * @Description: 账务系统-个人信用账户调用操作
 */
public interface PersonalCreditAccountService {

    /**
     * 获取账户额度信息（参数二选一填写就可以）
     *
     * @param certNo     身份证
     * @param customerId 用户id  二选一填写就可以
     * @return
     */
    Map<String, Object> getAccountInfo(String certNo, String customerId);

    /**
     * 下单扣减信用额度
     *
     * @param customerId 用户id
     * @param money      扣减金额
     * @param orderId    订单编号 -因为哪笔订单号冻结金额，传相关的订单号
     */
    void debit(Integer customerId, BigDecimal money, String orderId);

    /**
     * 恢复额度
     *
     * @param customerId  用户id -
     * @param mdType  恢复额度的原因 -还款007；取消订单008；退货009；
     * @param money   恢复金额
     * @param orderId 订单号
     */
    void recoverByCertNo(String customerId, String mdType, BigDecimal money, String orderId);

}
