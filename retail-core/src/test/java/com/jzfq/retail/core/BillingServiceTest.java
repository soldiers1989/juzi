package com.jzfq.retail.core;

import com.jzfq.retail.core.call.service.BillingService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: BillingServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月10日 14:19
 * @Description: 账务系统账单接口测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class BillingServiceTest {

    @Autowired
    private BillingService billingService;

    @Test
    public void getRepaymentTest(){
        System.out.println(billingService.getRepayment("XLS001051808090000",2));
    }
}
