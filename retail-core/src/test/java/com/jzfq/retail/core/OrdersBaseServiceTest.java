package com.jzfq.retail.core;

import com.jzfq.retail.core.api.service.OrdersBaseService;
import com.jzfq.retail.core.config.CoreApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: OrdersBaseServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月06日 11:11
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class OrdersBaseServiceTest {

    @Autowired
    private OrdersBaseService ordersBaseService;

    @Test
    public void generateTakeCodeTest() {
        System.out.println(String.format("%06d",111111));
        System.out.println(ordersBaseService.generateTakeCode());
    }
}
