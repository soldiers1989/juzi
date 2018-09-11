package com.jzfq.retail.core;

import com.jzfq.retail.common.enmu.GpsCheckFlag;
import com.jzfq.retail.core.api.service.GpsCheckRuleService;
import com.jzfq.retail.core.config.CoreApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: ShopBusiServcieTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 20:44
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class GpsCheckRuleServiceTest {

    @Autowired
    private GpsCheckRuleService gpsCheckRuleService;


    @Test
    public void testIsCheckGps(){
        Boolean isCheck = gpsCheckRuleService.isCheckGps(GpsCheckFlag.SELLER_CREATE_ORDER);
        log.info("是否校验：{}",isCheck);
        Boolean isCheck2 = gpsCheckRuleService.isCheckGps(GpsCheckFlag.USER_REGISTER_AND_AUTHENTICATION);
        log.info("是否校验：{}",isCheck2);
        Boolean isCheck3 = gpsCheckRuleService.isCheckGps(GpsCheckFlag.USER_CONFIRM_ORDER);
        log.info("是否校验：{}",isCheck3);
        Boolean isCheck4 = gpsCheckRuleService.isCheckGps(GpsCheckFlag.USER_PAY_ORDER);
        log.info("是否校验：{}",isCheck4);
    }


}
