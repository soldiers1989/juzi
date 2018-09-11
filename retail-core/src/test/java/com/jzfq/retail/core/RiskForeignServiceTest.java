package com.jzfq.retail.core;

import com.jzfq.retail.common.util.CommonUtil;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.call.domain.AssetEntryCapital;
import com.jzfq.retail.core.call.domain.OrderCheck;
import com.jzfq.retail.core.call.domain.RiskReceive;
import com.jzfq.retail.core.call.service.RiskForeignService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: RiskForeignServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 21:03
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class RiskForeignServiceTest {

    @Autowired
    private RiskForeignService riskForeignService;

    private static Integer isSingleCredit = 1;

    @Test
    public void fkOrderCheckTest() throws Exception{
        OrderCheck orderCheck = new OrderCheck();
        orderCheck.setGpsCity("北京市"); //; ("gpsCity":"北京市",
        orderCheck.setCardAddress("四川省大竹县庙坝镇街道739号"); //; ("cardAddress":,
        orderCheck.setAmount(new BigDecimal(3)); //; ("amount":3,
        orderCheck.setIdCard("513029198510273968"); //; ("idCard":"513029198510273968",
        orderCheck.setMobile("15011259641"); //; ("mobile":"15011259641",
        orderCheck.setGeneralCity("北京市"); //; ("generalCity":"北京市",
        orderCheck.setGeneralCityCode("110100"); //; ("generalCityCode":"110100",
        orderCheck.setCustomerName("杜鹃"); //; ("customerName":"杜鹃",
        orderCheck.setClientType(6); //; ("clientType":1, 设备类型 1.IOS；2.安卓；3.H5；4.PC；5.微信；6.微信小程序；(必传)
        orderCheck.setOrderTime(10);
        if (isSingleCredit == 0) {//0循环额度，1单笔授信
            orderCheck.setFinancialProductId(1); //;  金融产品 1.信用钱包；2.现金贷；3.单笔商户；4.小额现金(十露盘)；(必传);5.车险首
        } else {
            orderCheck.setFinancialProductId(3); //;  金融产品 1.信用钱包；2.现金贷；3.单笔商户；4.小额现金(十露盘)；(必传);5.车险首
        }

        orderCheck.setCustomerId(210001661); //; ("customerId":210001661,
        orderCheck.setOperationType(2); //;1是.认证；2是.交易；3是.提额；4.是提现；
        orderCheck.setGpsAddress("北京市朝阳区小营北路靠近和泰大厦(小营北路)"); //; ("gpsAddress":"北京市朝阳区小营北路靠近和泰大厦(小营北路)",
        orderCheck.setAge(CommonUtil.getAgeByIdcard("513029198510273968")); //; ("age":32,
        orderCheck.setChannelId(1); //; ("channelId":2 渠道 1.桔子分期；2.车主白条；(必传)
        String token = riskForeignService.fkOrderCheck(orderCheck);
    }

    @Test
    public void newRiskPushOrderTest() throws Exception {
        //风控进件赋值
        RiskReceive riskReceive = new RiskReceive();
        riskReceive.setToken("eff579b75f83fecfab84d3b8802fa586");

        RiskReceive.Loan loan = new RiskReceive.Loan();
        loan.setContractAmount(new BigDecimal(1000.00));
        loan.setFirstPay(new BigDecimal(0.00));
        loan.setFirstPayRatio(0.00);
        loan.setLoanAmount(new BigDecimal(1000.00));
        loan.setLoanPeriod(10);
        loan.setMonthPay(new BigDecimal(100.00));
        loan.setProductName("花花健身");


        RiskReceive.Product product = new RiskReceive.Product();
        product.setChannelId(5);
        product.setFinancialProductId(1);
        product.setOperationType(1);
        product.setClientType(1);

        RiskReceive.Person person = new RiskReceive.Person();
        person.setCertType(0);
        person.setCertCardNo("513029198510273968");
        person.setMobile("15043997986");

        RiskReceive.Task task = new RiskReceive.Task();
        task.setOrderNo("LNFBX-31-20180717-00");
        task.setApplyTime(DateUtil.getDate("yyyy-MM-dd"));
        task.setCustomerName("刘书宏");
        task.setCustomerType(0);
        task.setChannel("小程序");
        task.setProductCategory(4);
        task.setUuid(210001661);

        riskReceive.setLoan(loan);
        riskReceive.setProduct(product);
        riskReceive.setPerson(person);
        riskReceive.setTask(task);
        //风控进件
        riskForeignService.newRiskPushOrder(riskReceive);
    }

    @Test
    public void getEntryTelbooksTest() {
        List<AssetEntryCapital.EntryTelbooks> list = riskForeignService.getEntryTelbooks("131121199508071423", "田孟", "15810714776", "334455");
        System.out.println(list.size());
    }
}
