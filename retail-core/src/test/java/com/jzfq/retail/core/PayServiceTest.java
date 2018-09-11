package com.jzfq.retail.core;

import com.jzfq.retail.core.call.domain.BankCardResult;
import com.jzfq.retail.core.call.domain.WXPay;
import com.jzfq.retail.core.call.service.PayService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: PayServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月19日 14:26
 * @Description: 支付测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class PayServiceTest {

    @Autowired
    private PayService payService;

    @Test
    public void getBankCardTest() {
        BankCardResult bankCardResult = payService.getBankCard("210001444", "23");
    }

    @Test
    public void wxPaymentConfirm() {

        WXPay pay = new WXPay();
        pay.setOpenId("111");
        pay.setOrderId("123abc");
        pay.setTradeName("测试产品");
        pay.setAmount("100");
        pay.setCustomerId("123");
        pay.setPayType("repay");
        pay.setPeriod("1");
        pay.setSource("xcx");

        String s = payService.wxPaymentConfirm(pay);

        System.out.println(s);

    }
}
