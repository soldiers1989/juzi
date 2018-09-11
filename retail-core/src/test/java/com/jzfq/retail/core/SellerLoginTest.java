package com.jzfq.retail.core;

import com.jzfq.retail.core.api.service.SellerLoginService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: SellerLoginTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月31日 17:06
 * @Description: TODO(用一句话描述该文件做什么)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class SellerLoginTest {

    @Autowired
    private SellerLoginService sellerLoginService;


    @Test
    public void loginCountTest(){
        System.out.println(sellerLoginService.loginHandle("li123","lizhe1993",true));
    }

}
