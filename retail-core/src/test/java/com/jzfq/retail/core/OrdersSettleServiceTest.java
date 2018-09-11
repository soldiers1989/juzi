package com.jzfq.retail.core;

import com.alibaba.fastjson.JSON;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.OrdersSettleRes;
import com.jzfq.retail.core.api.service.OrdersSettleService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @Title: OrdersSettleServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月17日 13:52
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class OrdersSettleServiceTest {

    @Autowired
    private OrdersSettleService ordersSettleService;


    @Test
    public void getListTest(){
        ListResultRes<OrdersSettleRes> list = ordersSettleService.getList(1, 10, null);
        System.out.println(JSON.toJSON(list));
    }

    @Test
    public void createTest(){
        ordersSettleService.create(574,"XLS013091808140003",new BigDecimal("0"),new BigDecimal("0"));
    }

}
