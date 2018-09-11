package com.jzfq.retail.core;

import com.jzfq.retail.core.api.service.OrderSnCountService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: OrderSnCountServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月01日 18:37
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class OrderSnCountServiceTest {

    @Autowired
    private OrderSnCountService orderSnCountService;


    @Test
    public void getOrderSnTest() {

        String orderSn = orderSnCountService.getOrderSn(1);
        System.out.println(orderSn);

    }

    @Test
    public void getOrderSnTest2(){
        String orderSn = orderSnCountService.getOrderSn(1);
        System.out.println(orderSn);
    }


}
