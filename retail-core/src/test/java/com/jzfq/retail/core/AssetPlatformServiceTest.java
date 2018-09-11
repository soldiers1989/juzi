package com.jzfq.retail.core;

import com.google.common.collect.Lists;
import com.jzfq.retail.common.enmu.BankCapitalType;
import com.jzfq.retail.common.enmu.ProductCodeType;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.call.domain.AssetBankCapital;
import com.jzfq.retail.core.call.domain.AssetBankCapitalResult;
import com.jzfq.retail.core.call.domain.AssetEntryCapital;
import com.jzfq.retail.core.call.service.AssetPlatformService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: AssetPlatformServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月19日 14:25
 * @Description: 资金匹配系统测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class AssetPlatformServiceTest {

    @Autowired
    private AssetPlatformService assetPlatformService;


    @Test
    public void bankCapitalTest() {
        AssetBankCapital assetBankCapital = new AssetBankCapital();
        assetBankCapital.setAmount(new BigDecimal("1000"));
        assetBankCapital.setApplication(BankCapitalType.CAPITAL_TYPE_SC.getRetCode());
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        assetBankCapital.setAuthList(list);
        //查询订单用户
        assetBankCapital.setCertNo("130428199301112953");
        assetBankCapital.setCustomerId(210001444);
        assetBankCapital.setOrderId("LNFBX-31-20180717-00");
        //查询商户地址获取商户的区域传递给资匹获取资匹的信息
        assetBankCapital.setOrderProvince("北京市");
        assetBankCapital.setOrderType(1);
        assetBankCapital.setPeriod(12);
        assetBankCapital.setPeriodType(1);
        assetBankCapital.setProductCode(ProductCodeType.PRODUCT_CODE_TYPE_02.getRetCode());
        //获取资匹ID
        AssetBankCapitalResult assetBankCapitalResult = assetPlatformService.bankCapital(assetBankCapital);
        System.out.println(assetBankCapital.toString());
    }


    @Test
    public void entryCapitalTest() {
        AssetEntryCapital entryCapital = new AssetEntryCapital();
        entryCapital.setAmount(new BigDecimal("1000"));
        entryCapital.setApplication(BankCapitalType.CAPITAL_TYPE_SC.getRetCode());
        entryCapital.setAuthList(Lists.newArrayList(1, 2));
        entryCapital.setBankCardNumber("123123123123123");
        entryCapital.setBankCustomer("李哲");
        entryCapital.setBankId("20148182");//TODO 咨询丁浩id确定是bankid
        entryCapital.setBankName("北京银行");
        entryCapital.setBankPhone("15801372602");
        entryCapital.setBehead(0);    //是否砍头息	number	必填 1是、0否
        entryCapital.setCallbackUrl("/foreign/order/capitalBack");//资匹成功后回调接口
        entryCapital.setCapitalCode(4);
        AssetEntryCapital.EntryPerson entryPerson = new AssetEntryCapital.EntryPerson();
        entryPerson.setAge(18);
        //从什么地方获取
        entryPerson.setCertAddress("北京市昌平区回龙观镇18号");
        entryPerson.setCertBackImageUrl("url");
        entryPerson.setCertFrontImageUrl("url");
        //从什么地方获取
        entryPerson.setCertNo("130428199301112953");
        entryPerson.setCertValidStart("2016-10-19");
        entryPerson.setCertValidEnd("2018-10-19");
        entryPerson.setCertValidTime("2016.10.19-2018.10.19");
        entryPerson.setCustomerId(123124);
        entryPerson.setCustomerName("李哲");
        entryPerson.setCustomerPhone("15801372602");
        //从什么地方获取
        entryPerson.setDegree(0);
        entryPerson.setJuziScore(50);
        //从什么地方获取
        entryPerson.setSex(1);
        entryCapital.setEntryPerson(entryPerson);

        //缺少
        entryCapital.setFreeInterest(0);//是否免息
        entryCapital.setGoodsAddress("收货地址");//收货详细地址goodsAddress不能为空
        entryCapital.setGoodsArea("区");
        entryCapital.setGoodsAreaCode("区code");
        entryCapital.setGoodsCity("市");
        entryCapital.setGoodsCityCode("市code");
        entryCapital.setGoodsName("收货人");
        entryCapital.setGoodsPhone("收货手机号");
        entryCapital.setGoodsProvince("省");
        entryCapital.setGoodsProvinceCode("省code");
        //缺少

        entryCapital.setLoanPurpose("DAILY_SHOPPING");//婚宴------> 婚庆喜宴 WEDDING_BANQUET 旅游 ------>旅行远足 HIKING 教育------> 进修学习 FURTHER_STUDY 家电 ------>日常购物 DAILY_SHOPPING
        entryCapital.setNum(1);
        entryCapital.setOrderId("LNFBX-31-20180717-00");
        entryCapital.setOrderType(1);
        entryCapital.setPaymentRatio(0.5);
        entryCapital.setPeriod(12);
        entryCapital.setPrice(new BigDecimal("123"));
        entryCapital.setProductId("123");//将来得换
        entryCapital.setProductRate(new BigDecimal("123"));
        entryCapital.setRate(0.154D);
        entryCapital.setRepayDate("2018-07-04");
        entryCapital.setStoreOrderTime(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));
        entryCapital.setTitle("催化");
        entryCapital.setProductCode("03");//必填 01现金贷、02上线分期（商城）、03线下分期（白条），04车险

        assetPlatformService.entryCapital(entryCapital);
    }

    @Test
    public void closeOrder() {

    }
}
