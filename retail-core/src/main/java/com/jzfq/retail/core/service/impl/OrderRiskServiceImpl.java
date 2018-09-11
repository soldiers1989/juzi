package com.jzfq.retail.core.service.impl;

import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.entity.score.MemberScoreFraudmetrix;
import com.juzifenqi.usercenter.service.score.IMemberScoreFraudMetrixService;
import com.jzfq.auth.core.api.JzfqAuthApi;
import com.jzfq.auth.core.api.dto.JzfqAuthInfoDto;
import com.jzfq.auth.core.api.entiy.AuthBasicDetail;
import com.jzfq.auth.core.api.entiy.AuthCompanyDetail;
import com.jzfq.auth.core.api.entiy.AuthIdentityDetail;
import com.jzfq.auth.core.api.entiy.AuthResult;
import com.jzfq.auth.core.api.vo.JsonResult;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.common.enmu.OrderStatus;
import com.jzfq.retail.common.enmu.SellerStoreTypeEnum;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.CommonUtil;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.*;
import com.jzfq.retail.core.call.domain.OrderCheck;
import com.jzfq.retail.core.call.domain.RiskReceive;
import com.jzfq.retail.core.call.service.RiskForeignService;
import com.jzfq.retail.core.config.MemberCenterHandler;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.dao.manual.OrdersBaseManualMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: OrderRiskServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月08日 11:45
 * @Description: 订单风控操作实现 -准进 -进件
 */
@Slf4j
@Service
public class OrderRiskServiceImpl implements OrderRiskService {

    @Value("${fk.channelId}")
    private int FK_CHANNALID;

    @Autowired
    private RiskForeignService riskForeignService;

    @Autowired
    private OrdersBaseManualMapper ordersBaseManualMapper;

    @Autowired
    private OrdersTradeMapper ordersTradeMapper;

    @Autowired
    private OrdersBaseMapper ordersBaseMapper;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private OrdersProductService ordersProductService;

    @Autowired
    private SellerAddressService sellerAddressService;

    @Autowired
    private MemberCenterHandler memberCenterHandler;

    @Autowired
    private SellerStoreMapper sellerStoreMapper;

    @Autowired
    private OrdersBaseService ordersBaseService;

    @Autowired
    private OrdersUserService ordersUserService;

    @Override
    public void riskCheck(OrdersBase ordersBase, SellerAddress sellerAddress, OrdersUser ordersUser, Integer isSingleCredit) throws Exception {
        MemberInfo member = memberCenterHandler.getMemberById(ordersBase.getMemberId());
        OrderCheck orderCheck = new OrderCheck();
        orderCheck.setOrderId(ordersBase.getId());
        orderCheck.setOrderSn(ordersBase.getOrderSn());
        orderCheck.setGpsCity(sellerAddress.getCityName()); //; ("gpsCity":"北京市",
        orderCheck.setCardAddress(sellerAddress.getAddressInfo()); //; ("cardAddress":"四川省大竹县庙坝镇街道739号",
        orderCheck.setAmount(ordersBase.getMoneyOrder()); //; ("amount":3,
        orderCheck.setIdCard(member.getIdcard()); //; ("idCard":"513029198510273968",
        orderCheck.setMobile(member.getMobile()); //; ("mobile":"15011259641",
        orderCheck.setGeneralCity(sellerAddress.getCityName()); //; ("generalCity":"北京市",
        orderCheck.setGeneralCityCode(sellerAddress.getCityCode()); //; ("generalCityCode":"110100",
        orderCheck.setCustomerName(member.getRealName()); //; ("customerName":"杜鹃",
        orderCheck.setClientType(6); //; ("clientType":1, 设备类型 1.IOS；2.安卓；3.H5；4.PC；5.微信；6.微信小程序；(必传)
        orderCheck.setOrderTime(1);//当前城市下单次数 默认1


        boolean isCZBT = this.isCZBT(sellerAddress.getSellerId());

        if (isSingleCredit == 0) {//0循环额度，1单笔授信
            //判断是否是车主白条，确认是否选择5
            orderCheck.setFinancialProductId(isCZBT?5:1); //;  金融产品 1.信用钱包；2.现金贷；3.单笔商户；4.小额现金(十露盘)；(必传);5.车险首
            orderCheck.setRmsProductType(13);//NEW_RETAIL_LOOP(13,"新零售循环交易订单"), NEW_RETAIL_ONE(14,"新零售单笔交易订单");
        } else {
            orderCheck.setFinancialProductId(isCZBT?5:3); //;  金融产品 1.信用钱包；2.现金贷；3.单笔商户；4.小额现金(十露盘)；(必传);5.车险首
            orderCheck.setRmsProductType(14);
        }
        orderCheck.setRegistCode(member.getReference());
        orderCheck.setCustomerId(member.getId()); //; ("customerId":210001661,
        orderCheck.setOperationType(2); //;1是.认证；2是.交易；3是.提额；4.是提现；
        orderCheck.setGpsAddress(sellerAddress.getAddressInfo()); //; ("gpsAddress":"北京市朝阳区小营北路靠近和泰大厦(小营北路)",
        orderCheck.setAge(CommonUtil.getAgeByIdcard(ordersUser.getIdCard())); //; ("age":32,
        orderCheck.setChannelId(FK_CHANNALID); //; ("channelId":2 渠道 1.桔子分期；2.车主白条；(必传)  风控会对新零售新增一个channelId 5，7.25日测试时还没有加上 暂时传1
        orderCheck.setRmsCustomerType(0);//客户类型  0 白领，1 非工薪 2 车主

        String riskToken = riskForeignService.fkOrderCheck(orderCheck);

        //需要把风控准入返回的token入库
        ordersBase.setRiskToken(riskToken);
        ordersBaseManualMapper.updateRiskTokenById(ordersBase);
    }

    @Override
    public void riskReceive(OrdersBase ordersBase, MemberInfo memberInfo, Integer isSingleCredit, Integer productCategory, Double longitud, Double latitude) {
        try {
            //查询订单产品
            OrdersProduct ordersProduct = ordersProductService.getByOrderSn(ordersBase.getOrderSn());
            //查询订单交易
            OrdersTradeQuery queryTrade = new OrdersTradeQuery();
            queryTrade.createCriteria().andOrderIdEqualTo(ordersBase.getId());
            List<OrdersTrade> ordersTradeList = ordersTradeMapper.selectByExample(queryTrade);
            OrdersTrade ordersTrade = ordersTradeList.get(0);

            //商户地址
            SellerAddress sellerAddress = sellerAddressService.getBySellerId(ordersBase.getSellerId());
            //查询商户
            Seller seller = sellerMapper.selectByPrimaryKey(ordersBase.getSellerId());

            //风控进件赋值
            RiskReceive riskReceive = new RiskReceive();
            riskReceive.setOrderId(ordersBase.getId());
            riskReceive.setToken(ordersBase.getRiskToken());
            riskReceive.setOrderSn(ordersBase.getOrderSn());
            RiskReceive.Loan loan = new RiskReceive.Loan();
            loan.setFirstPay(ordersBase.getDownpaymentAmount());
            loan.setFirstPayRatio(ordersBase.getFirstpayratio());
            loan.setLoanAmount(ordersBase.getMoneyOrder());
            loan.setMonthPay(ordersTrade.getMonthlyPrincipal());
            loan.setProductName(ordersProduct.getProductName());
            loan.setRemark(ordersBase.getRemark());
            //订单类型 NEW_RETAIL_LOOP(13,"新零售循环交易订单"), NEW_RETAIL_ONE(14,"新零售单笔交易订单");
            if (ordersBase.getCreditFlag() == 0) {//0循环额度，1单笔授信
                loan.setLoanType(13);
            } else {
                loan.setLoanType(14);
            }
            loan.setRegistCode(memberInfo.getReference());
            loan.setRate(ordersBase.getMonthRatio());
            //用户中心获取同盾分
            ServiceResult<MemberScoreFraudmetrix> memberScoreFraudmetrixResult = memberCenterHandler.getLastScoreByChannel(ordersBase.getMemberId());
            if (!memberScoreFraudmetrixResult.getSuccess() || memberScoreFraudmetrixResult.getResult() == null) {
                loan.setTdScore("0");//同盾分(非桔盾分)
            } else {
                MemberScoreFraudmetrix memberScoreFraudmetrix = memberScoreFraudmetrixResult.getResult();
                loan.setTdScore(memberScoreFraudmetrix.getScore());//同盾分(非桔盾分)
            }
            loan.setContractAmount(ordersBase.getMoneyOrder());
            loan.setAccrual(ordersTrade.getMoneyIntegral());
            loan.setFirstPayRatio(ordersBase.getFirstpayratio());
            loan.setLoanPeriod(ordersTrade.getTerm());

            RiskReceive.Product product = new RiskReceive.Product();
            //渠道
            product.setChannelId(FK_CHANNALID); //; ("channelId":2 渠道 1.桔子分期；2.车主白条；(必传)  风控会对新零售新增一个channelId 5，7.25日测试时还没有加上 暂时传1

            boolean isCZBT = this.isCZBT(sellerAddress.getSellerId());

            //金融产品 1.信用钱包；2.现金贷；3.单笔商户；4.小额现金(十露盘)；(必传);5.车险首
            if (isSingleCredit == 0) {//0循环额度，1单笔授信
                product.setFinancialProductId(isCZBT?5:1);
            } else {
                product.setFinancialProductId(isCZBT?5:3);
            }
            product.setOperationType(2);//;1是.认证；2是.交易；3是.提额；4.是提现；
            product.setClientType(6);//1, 设备类型 1.IOS；2.安卓；3.H5；4.PC；5.微信；6.微信小程序；(必传)
            product.setCommodityType(9);//0.全部；1.3C；2.轻奢；3.3C-低；4.3C-中；5.3C-高；6.家电；7.美妆；8.服饰；9.健身,10.食品；
            product.setCustomerType(0);

            JsonResult<JzfqAuthInfoDto> res = memberCenterHandler.getInfoByUserId(ordersBase.getMemberId());

            JzfqAuthInfoDto jzfqAuthInfoDto = res.getResult();

            RiskReceive.Person person = new RiskReceive.Person();
            person.setCertType(0);
            person.setCertCardNo(memberInfo.getIdcard());
            person.setMobile(memberInfo.getMobile());
            person.setDistributionWay(0);
            //"receiptProvinceCode":110000,
            person.setReceiptProvinceCode(sellerAddress.getProvinceCode());
            //"certType":0,
            person.setCertType(0);
            //"receiptDistrictCode":110105,
            person.setReceiptDistrictCode(sellerAddress.getAreaCode());
            //"sex":1,
            AuthIdentityDetail authIdentityDetail = jzfqAuthInfoDto.getAuthIdentityDetail();
            if (authIdentityDetail != null) {
                person.setSex(authIdentityDetail.getSex());
                //"commodityNum":1,
                person.setCommodityNum(1);
                //"mobile":"17600718129",
                //"receiptPhone":"17600718129",
                person.setReceiptPhone(authIdentityDetail.getMobile());
                //"debitBank":"中国银行",
                person.setDebitBank(authIdentityDetail.getBankNo());
                //"effectBeginDate":"2016-06-02",
                String validTime = authIdentityDetail.getCertValidTime();
                String[] valids_ = validTime.split("-");
                person.setEffectBeginDate(valids_[0].replace(".", "-"));
                //"receiptCity":"北京市",
                person.setReceiptCity(authIdentityDetail.getCityName());
                //"birthDate":"1989-07-10 00:00:00.0",
                person.setBirthDate(authIdentityDetail.getBirthday());
                //"effectEndDate":"2036-06-02",
                person.setEffectEndDate(valids_[1].replace(".", "-"));
                //"receiptProvince":"北京",
                person.setReceiptProvince(sellerAddress.getProvinceName());
                //"receiptName":"张建政",
                person.setReceiptName(authIdentityDetail.getName());
                //"receiptCityCode":110100,
                person.setReceiptCityCode(sellerAddress.getCityCode());
                person.setCertCardNo(authIdentityDetail.getCertNo());
                //"registAddress":"河北省河间市沙洼乡后洪雁村107号",
                person.setRegistAddress(authIdentityDetail.getCertAddress());
                //"receiptAddress":"北京,北京市,朝阳区小营北路和泰大厦",
                person.setReceiptAddress(authIdentityDetail.getAccurate());
                //person.setWifiMac();
                person.setMinzu(authIdentityDetail.getEthnicity());
            }

            //"receiptDistrict":"朝阳区",
            person.setReceiptDistrict(sellerAddress.getAreaName());

            person.setMerchantNo(seller.getSellerCode());
            AuthBasicDetail authBasicDetail = jzfqAuthInfoDto.getAuthBasicDetail();
            if (authBasicDetail != null) {
                person.setHomeAddress(authBasicDetail.getLiveAddress());
                person.setHomeCity(authBasicDetail.getLiveC());
                person.setHomeCityCode(authBasicDetail.getLiveCCode());
                person.setHomeDistrict(authBasicDetail.getLiveA());
                person.setHomeDistrictCode(authBasicDetail.getLiveACode());
                person.setHomeProvince(authBasicDetail.getLiveP());
                person.setHomeProvinceCode(authBasicDetail.getLivePCode());
            }


            RiskReceive.Task task = new RiskReceive.Task();
            task.setOrderNo(ordersBase.getOrderSn());
            task.setApplyTime(DateUtil.getDate("yyyy-MM-dd"));
            task.setCustomerName(memberInfo.getRealName());
            task.setChannel("小程序");//渠道
            task.setProductCategory(ordersTrade.getCapitalId());//资方
            task.setUuid(memberInfo.getId());//客户id
            task.setCustomerType(0);//客户类型  0 白领，1 非工薪 2 车主
            task.setTdFlag(0);//默认0

            task.setSignFlag("0");
            task.setIs3cOther(0);
            task.setIs3cFirst(0);
            task.setLng(longitud);
            task.setLat(latitude);
            task.setIsJxl("0");
            task.setIs3c(0);
            task.setGpsDistrict(sellerAddress.getAreaName());
            task.setGpsDistrictCode(sellerAddress.getAreaCode());
            task.setGpsProvince(sellerAddress.getProvinceName());
            task.setGpsProvinceCode(sellerAddress.getProvinceCode());
            task.setGpsCity(sellerAddress.getCityName());//应该传客户的地址，但是新零售没有，只能传商户所在的city，7.27约会讨论
            task.setGpsCityCode(sellerAddress.getCityCode());
            task.setApplyTime(DateUtil.formatting(ordersBase.getConfirmTime()));
            task.setGpsAddress(sellerAddress.getAddressInfo());

            List<RiskReceive.Contacts> contactsList = new ArrayList<RiskReceive.Contacts>(); //联系人
            List<RiskReceive.Images> imagesList = new ArrayList<RiskReceive.Images>(); //图片
            RiskReceive.Images imagesmages1 = new RiskReceive.Images();
            imagesmages1.setSmallUrl(jzfqAuthInfoDto.getAuthIdentityDetail().getCertBackImageUrl());
            imagesmages1.setType("0");
            imagesmages1.setUrl(jzfqAuthInfoDto.getAuthIdentityDetail().getCertBackImageUrl());
            imagesList.add(imagesmages1);
            RiskReceive.Images imagesmages2 = new RiskReceive.Images();
            imagesmages2.setSmallUrl(jzfqAuthInfoDto.getAuthIdentityDetail().getCertFrontImageUrl());
            imagesmages2.setType("1");
            imagesmages2.setUrl(jzfqAuthInfoDto.getAuthIdentityDetail().getCertFrontImageUrl());
            imagesList.add(imagesmages2);

            String f1 = jzfqAuthInfoDto.getAuthLinkDetail().getLinkInfoF();//联系人1
            String f2 = jzfqAuthInfoDto.getAuthLinkDetail().getLinkInfoT();//联系人2
            log.info("联系人1：{}，联系人2：{}", f1, f2);
            String[] f1_ = f1.split("\\|\\|");
            String[] f2_ = f2.split("\\|\\|");
            RiskReceive.Contacts contacts1 = new RiskReceive.Contacts();
            contacts1.setRelation(f1_[0]);
            contacts1.setName(f1_[1]);
            contacts1.setPhone(f1_[2]);
            contactsList.add(contacts1);
            RiskReceive.Contacts contacts2 = new RiskReceive.Contacts();
            contacts2.setRelation(f2_[0]);
            contacts2.setName(f2_[1]);
            contacts2.setPhone(f2_[2]);
            contactsList.add(contacts2);

            riskReceive.setContactsList(contactsList);
            riskReceive.setImagesList(imagesList);

            RiskReceive.Profession profession = new RiskReceive.Profession();

            AuthCompanyDetail authCompanyDetail = jzfqAuthInfoDto.getAuthCompanyDetail();
            if (authCompanyDetail != null) {
                profession.setCompanyCity(authCompanyDetail.getWorkC());
                profession.setCompanyDistrict(authCompanyDetail.getWorkA());
                profession.setCompanyDistrictCode(authCompanyDetail.getWorkACode());
                profession.setCompanyPhone(authCompanyDetail.getCompanyPhone());
                profession.setCompanyProvinceCode(authCompanyDetail.getWorkPCode());
                profession.setCompanyName(authCompanyDetail.getCompanyName());
                profession.setSalaryDay(String.valueOf(authCompanyDetail.getPayDate()));
                profession.setCompanyCityCode(authCompanyDetail.getWorkCCode());
                profession.setCompanyProvince(authCompanyDetail.getWorkP());
            }

            riskReceive.setLoan(loan);
            riskReceive.setProduct(product);
            riskReceive.setPerson(person);
            riskReceive.setTask(task);
            riskReceive.setProfession(profession);

            //风控进件
            riskForeignService.newRiskPushOrder(riskReceive);
            //记录成功
            ordersBase.setOrderState(OrderStatus.ORDER_STATE_135.getCode());
            ordersBaseMapper.updateByPrimaryKey(ordersBase);
            //订单提交日志记录
            //TODO:缺少订单操作人信息
            systemLogSupport.orderLogSave(null, null, ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.getMsgByCode(ordersBase.getOrderState()), ordersBase.getOrderState());
        } catch (Exception e) {
            log.error("风控进件异常: {}", e);
            //此处不需要往上抛异常，因为风控进件失败，但是扣减信用额度成功，如果抛出异常，订单状态会回滚，那么用户还会再次支付扣减信用额度，这是不允许的。2018-8-8 liuwei
            //throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0039);
        }
    }

    @Override
    public void supplementRiskReceive(String orderSn) {
        //获取订单基本信息
        OrdersBaseQuery ordersBaseQuery = new OrdersBaseQuery();
        ordersBaseQuery.createCriteria().andOrderSnEqualTo(orderSn);
        List<OrdersBase> orderList = ordersBaseMapper.selectByExample(ordersBaseQuery);
        OrdersBase ordersBase = orderList.get(0);
        if (ordersBase == null || !ordersBase.getOrderState().equals(OrderStatus.ORDER_STATE_130.getCode())) {
            throw new RuntimeException("订单风控进件补单失败");
        }
        //获取订单Trade信息
        OrdersTradeQuery queryTrade = new OrdersTradeQuery();
        queryTrade.createCriteria().andOrderIdEqualTo(ordersBase.getId());
        List<OrdersTrade> ordersTradeList = ordersTradeMapper.selectByExample(queryTrade);
        OrdersTrade ordersTrade = ordersTradeList.get(0);

        //获取订单用户信息
        MemberInfo member = memberCenterHandler.getMemberById(ordersBase.getMemberId());

        //获取商户地址信息
        SellerAddress sellerAddress = sellerAddressService.getBySellerId(ordersBase.getSellerId());
        //风控进件
        riskReceive(ordersBase, member, ordersBase.getCreditFlag(), ordersTrade.getCapitalId(), sellerAddress.getLongitude(), sellerAddress.getLatitude());
    }

    @Override
    public void supplementRiskCheck(String orderSn, Integer isSingleCredit) throws Exception {
        //订单信息
        OrdersBase ordersBase = ordersBaseService.getByOrderSn(orderSn);
        if (ordersBase == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0011);
        }
        //查询商户地址
        SellerAddress sellerAddress = sellerAddressService.getBySellerId(ordersBase.getSellerId());
        //查询订单用户
        OrdersUser ordersUser = ordersUserService.getByOrderSn(ordersBase.getOrderSn());
        //准进补单
        this.riskCheck(ordersBase, sellerAddress, ordersUser, isSingleCredit);
    }


    //======================
    @Override
    public boolean isCZBT(Integer sellerId){
        boolean bool = false;
        SellerStoreQuery SSQ = new SellerStoreQuery();
        SSQ.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerStore> list = sellerStoreMapper.selectByExample(SSQ);
        if(!list.isEmpty()){
            SellerStore sellerStore = list.get(0);
            bool= sellerStore.getStoreType().equals(SellerStoreTypeEnum.STORE_TYPE_7.getCode());
        }
        return bool;
    }

}
