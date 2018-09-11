package com.jzfq.retail.core;

import com.jzfq.retail.core.call.service.PersonalCreditAccountService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @Title: PersonalCreditAccountServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月19日 15:34
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class PersonalCreditAccountServiceTest {


    @Autowired
    private PersonalCreditAccountService personalCreditAccountService;


    @Test
    public void debitTest(){
        personalCreditAccountService.debit(210001444,new BigDecimal("1000"),"LNFBX-31-20180717-00");
    }
}
