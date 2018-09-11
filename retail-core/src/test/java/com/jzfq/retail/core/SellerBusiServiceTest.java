package com.jzfq.retail.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.SellerBusiService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @Title: SellerBusiServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月23日 20:08
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class SellerBusiServiceTest {

    @Autowired
    private SellerBusiService sellerBusiService;


    @Test
    public void generateQRCodeTest() {
        System.out.println(sellerBusiService.getOrderQRCode("LNFBX-31-20180717-01"));
    }

    @Test
    public void getSimpleOrderInfoByOrderSnTest(){
        System.out.println(sellerBusiService.getSimpleOrderInfoByOrderSn("LNFBX-31-20180717-00"));
    }

    @Test
    public void getOrderRepaymentsTest(){
        sellerBusiService.getOrderRepayments(298,1000,0,1,20);
    }

    @Test
    public void findOrderListBySellerTest(){
        ListResultRes<Map<String, Object>> zsc111 = sellerBusiService.findOrderListBySeller(1, 10, null, "zsc111");
        String s = JSON.toJSONString(zsc111,SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty).toString();
        System.out.println(s);
    }

    @Test
    public void findCateBrandInfoTest(){
        List<Map<String, Object>> cateBrandInfo = sellerBusiService.findCateBrandInfo(1307);
        System.out.println(JSON.toJSON(cateBrandInfo));
    }
}
