package com.jzfq.retail.core;

import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.service.member.IMemberShopService;
import com.jzfq.retail.core.config.CoreApplication;
import com.jzfq.retail.core.rocketmq.MQProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: ShopBusiServcieTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 20:44
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class RocketMQTest {

    @Autowired
    private MQProducer mqProducer;

    @Value("${topic.zp.entry.order}")
    private String zpTopic;

    @Test
    public void testProducer(){
        String content = "{\"amount\":156.00,\"application\":\"SC\",\"authList\":[1,9,4,3],\"bankCardId\":\"3242490\",\"bankCardNumber\":\"6217000010093139767\",\"bankCustomer\":\"史静静\",\"bankId\":\"CCB\",\"bankName\":\"中国建设银行\",\"bankPhone\":\"18601935978\",\"behead\":0,\"callbackUrl\":\"/foreign/order/capitalBack\",\"capitalCode\":0,\"contacts\":[{\"contractsName\":\"唐唐\",\"contractsPhone\":\"18612421178\"},{\"contractsName\":\"唐山\",\"contractsPhone\":\"18601935623\"}],\"couponAmount\":0,\"entryMerchant\":{\"merchantId\":\"XLS378\"},\"entryPerson\":{\"age\":30,\"certAddress\":\"河南省商丘市梁园区谢集镇史庄村30号\",\"certBackImageUrl\":\"https://oss-jzfq-auth.juzifenqi.com/webank/mall-xcx-o3ya05DqUP4kS7JfU9oG996QwTkc-1533092399772.jpg?OSSAccessKeyId=LTAIxisKGg3IkLtw\",\"certFrontImageUrl\":\"https://oss-jzfq-auth.juzifenqi.com/webank/mall-xcx-o3ya05DqUP4kS7JfU9oG996QwTkc-1533092387015.jpg?OSSAccessKeyId=LTAIxisKGg3IkLtw\",\"certNo\":\"411402198804067680\",\"certValidEnd\":\"2036-06-15\",\"certValidStart\":\"2016-06-15\",\"certValidTime\":\"2016.06.15-2036.06.15\",\"companyAddress\":\"时代凌宇大厦\",\"companyDuty\":\"1\",\"companyEntryTime\":\"2018-08-01 00:00:00.0\",\"companyName\":\"北京桔子分期科技有限公司\",\"companyPhone\":\"18601935978\",\"companyWorkArea\":\"朝阳区\",\"companyWorkAreaCode\":\"110105\",\"companyWorkCity\":\"北京市\",\"companyWorkCityCode\":\"110100\",\"companyWorkPro\":\"北京\",\"companyWorkProCode\":\"110000\",\"customerId\":210001723,\"customerName\":\"史静静\",\"customerPhone\":\"18601935978\",\"juziScore\":20,\"sex\":0},\"entryTelbooks\":[{\"contactsName\":\"黄飞衡\",\"contactsPhone\":\"13242349738\"},{\"contactsName\":\"徐浩然\",\"contactsPhone\":\"17768124090\"},{\"contactsName\":\"谭阳琨\",\"contactsPhone\":\"17721861926\"},{\"contactsName\":\"李东明\",\"contactsPhone\":\"17688146533\"},{\"contactsName\":\"方婷\",\"contactsPhone\":\"17647551796\"},{\"contactsName\":\"赵歆远\",\"contactsPhone\":\"17647335679\"},{\"contactsName\":\"郑先生\",\"contactsPhone\":\"17359338696\"},{\"contactsName\":\"柯秀强\",\"contactsPhone\":\"17319124335\"},{\"contactsName\":\"李润雨\",\"contactsPhone\":\"17314984916\"},{\"contactsName\":\"陈小婷\",\"contactsPhone\":\"15959202927\"},{\"contactsName\":\"张淦\",\"contactsPhone\":\"15907235800\"},{\"contactsName\":\"王露\",\"contactsPhone\":\"15641692279\"},{\"contactsName\":\"闫超\",\"contactsPhone\":\"15640260687\"},{\"contactsName\":\"董英涛\",\"contactsPhone\":\"15606956881\"},{\"contactsName\":\"李尊丰\",\"contactsPhone\":\"15554478882\"},{\"contactsName\":\"郑奇\",\"contactsPhone\":\"15543651296\"},{\"contactsName\":\"张皖欣\",\"contactsPhone\":\"15390561875\"},{\"contactsName\":\"王希予\",\"contactsPhone\":\"15320030809\"},{\"contactsName\":\"蔡建平\",\"contactsPhone\":\"15211488063\"},{\"contactsName\":\"董涛\",\"contactsPhone\":\"18943659900\"},{\"contactsName\":\"位刘建\",\"contactsPhone\":\"15205791557\"},{\"contactsName\":\"徐洋洋\",\"contactsPhone\":\"18941132437\"},{\"contactsName\":\"陶杨威\",\"contactsPhone\":\"15090829264\"},{\"contactsName\":\"杨航\",\"contactsPhone\":\"18871266686\"},{\"contactsName\":\"崔林涛\",\"contactsPhone\":\"15029106494\"},{\"contactsName\":\"唐棕皓\",\"contactsPhone\":\"18782718790\"},{\"contactsName\":\"张福信\",\"contactsPhone\":\"15000708221\"},{\"contactsName\":\"肖扬\",\"contactsPhone\":\"18723470885\"},{\"contactsName\":\"李瑞\",\"contactsPhone\":\"13992213417\"},{\"contactsName\":\"宋凯\",\"contactsPhone\":\"18710053576\"},{\"contactsName\":\"宫雪\",\"contactsPhone\":\"13977007610\"},{\"contactsName\":\"冯洋\",\"contactsPhone\":\"18622477858\"},{\"contactsName\":\"李跃\",\"contactsPhone\":\"13942442542\"},{\"contactsName\":\"唐\",\"contactsPhone\":\"18612421178\"},{\"contactsName\":\"王鑫浩\",\"contactsPhone\":\"13834262364\"},{\"contactsName\":\"魏潢杰\",\"contactsPhone\":\"18601919519\"},{\"contactsName\":\"吴国兴\",\"contactsPhone\":\"13715400892\"},{\"contactsName\":\"李荣辉\",\"contactsPhone\":\"18402966735\"},{\"contactsName\":\"朱灵湖\",\"contactsPhone\":\"13676681216\"},{\"contactsName\":\"王国帅\",\"contactsPhone\":\"18364517476\"},{\"contactsName\":\"小刘\",\"contactsPhone\":\"13599498905\"},{\"contactsName\":\"郭海涛\",\"contactsPhone\":\"18270705850\"},{\"contactsName\":\"梁广维\",\"contactsPhone\":\"13549454042\"},{\"contactsName\":\"余苏苏\",\"contactsPhone\":\"18217330257\"},{\"contactsName\":\"陈志东\",\"contactsPhone\":\"13505900199\"},{\"contactsName\":\"黄立云\",\"contactsPhone\":\"18159046637\"},{\"contactsName\":\"金磊\",\"contactsPhone\":\"13451603770\"},{\"contactsName\":\"高健\",\"contactsPhone\":\"17782278310\"}],\"freeInterest\":0,\"goodsAddress\":\"八王寺街53-2号（2门）\",\"goodsArea\":\"大东区\",\"goodsAreaCode\":\"230103\",\"goodsCity\":\"沈阳市\",\"goodsCityCode\":\"230100\",\"goodsName\":\"史静静\",\"goodsPhone\":\"18601935978\",\"goodsProvince\":\"辽宁省\",\"goodsProvinceCode\":\"210000\",\"loanPurpose\":\"DAILY_SHOPPING\",\"num\":1,\"orderId\":\"XLS013071808150017\",\"orderType\":2,\"paymentRatio\":0.0,\"period\":3,\"price\":156.00,\"productCode\":\"03\",\"productId\":\"020101\",\"productRate\":0.01500000,\"rate\":0.015,\"repayDate\":\"2018-09-15 20:21:40\",\"serviceFee\":\"0.000\",\"storeOrderTime\":\"2018-08-15 20:21:40\",\"title\":\"Shiseido 资生堂 超强防水防晒喷雾\"}";
        mqProducer.send(zpTopic, content, null);

    }


}
