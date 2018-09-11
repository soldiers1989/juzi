package com.jzfq.retail.core;

import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.service.member.IMemberShopService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;

/**
 * @Title: ShopBusiServcieTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 20:44
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class ShopBusiServcieTest {

    @Autowired
    private IMemberShopService memberShopService;


    @Test
    public void getMemberByIdTest(){
        ServiceResult<MemberInfo> result = memberShopService.getMemberById(210001033);
        System.out.println(result.toString());
        System.out.println(result.getResult().getBalancePwd());
    }


}
