package com.jzfq.retail.core;

import com.jzfq.retail.core.call.service.PersonCreditAccountService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @Title: PersonCreditAccountServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 19:37
 * @Description: 个人资金账户测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class PersonCreditAccountServiceTest {

    @Autowired
    private PersonCreditAccountService personCreditAccountService;

    @Test
    public void checkPersonAuthenticationTest() {
        /**
         * {"result":{"date":"2018-05-22 18:32:03","validAmount":"974846.15","code":"2","name":"认证审核成功","creditAmount":"1000000.00","applyDate":"2018-05-22 10:43:01","activated":1},"msg":"正常返回","code":"100000","success":true}
         */
        personCreditAccountService.checkPersonAuthentication("210001444");
    }

    @Test
    public void getPlanTrialTest() throws Exception {
        personCreditAccountService.getPlanTrial(new BigDecimal("30"), new BigDecimal("5000"), Integer.valueOf(12), Double.valueOf(0.12));
    }
}
