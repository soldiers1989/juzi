package com.jzfq.retail.core.call.service;


/**
 * @Title: MerchantCapitalAccountService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月03日 16:10
 * @Description: 资金账户系统调用操作
 */
public interface MerchantCapitalAccountService {

    /**
     * 资金账户开户
     *
     * @param businessId 商户id
     * @param name       商户名称
     */
    void initAccount(String businessId, String name);

    /**
     * 资金账户绑卡-对公
     *
     * @param bankCardNo              银行卡号
     * @param bankCode                银行编码
     * @param name                    姓名
     * @param refId                   商户号
     * @param payeeBankAssociatedCode 联行号
     * @param payeeBankFullName       支行名称
     */
    void bindBankcardPub(String bankCardNo, Integer bankCode, String name, String refId, String payeeBankAssociatedCode, String payeeBankFullName);

    /**
     * 资金账户绑卡-对私
     *
     * @param bankCardNo 银行卡号
     * @param bankCode   银行编码
     * @param name       姓名
     * @param certNo     身份证号
     * @param bankPhone  预留手机号
     */
    void bindBankcardPri(String bankCardNo, Integer bankCode, String name, String certNo, String bankPhone, String refId);

}
