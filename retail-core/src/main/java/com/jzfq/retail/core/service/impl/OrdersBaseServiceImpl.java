package com.jzfq.retail.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.entity.score.MemberScoreFraudmetrix;
import com.juzifenqi.usercenter.service.score.IMemberScoreFraudMetrixService;
import com.jzfq.auth.core.api.JzfqAuthApi;
import com.jzfq.auth.core.api.dto.JzfqAuthInfoDto;
import com.jzfq.auth.core.api.entiy.AuthResult;
import com.jzfq.auth.core.api.vo.JsonResult;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.CapitalBackReq;
import com.jzfq.retail.bean.vo.req.OrderListReq;
import com.jzfq.retail.bean.vo.req.RiskCallbackReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.CommonUtil;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.*;
import com.jzfq.retail.core.call.domain.AssetEntryCapital;
import com.jzfq.retail.core.call.domain.BankCardResult;
import com.jzfq.retail.core.call.service.AssetPlatformService;
import com.jzfq.retail.core.call.service.PayService;
import com.jzfq.retail.core.call.service.PersonalCreditAccountService;
import com.jzfq.retail.core.call.service.RiskForeignService;
import com.jzfq.retail.core.config.MemberCenterHandler;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.dao.manual.OrderLogManualMapper;
import com.jzfq.retail.core.dao.manual.OrdersBaseManualMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:02
 * @Description: 订单接口实现
 */
@SuppressWarnings("ALL")
@Slf4j
@Service
public class OrdersBaseServiceImpl implements OrdersBaseService {

    @Autowired
    private OrdersBaseMapper ordersBaseMapper;

    @Autowired
    private OrdersBaseManualMapper ordersBaseManualMapper;

    @Autowired
    private AssetPlatformService assetPlatformService;

    @Autowired
    private PayService payService;

    @Autowired
    private OrdersTradeMapper ordersTradeMapper;

    @Autowired
    private OrdersUserMapper ordersUserMapper;

    @Autowired
    private OrdersProductMapper ordersProductMapper;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private OrderLogManualMapper orderLogManualMapper;

    @Autowired
    private SellerAddressMapper sellerAddressMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private OrdersTradeMapper rdersTradeMapper;

    @Autowired
    private SellerTermMapper sellerTermMapper;

    @Autowired
    private SellerTermService sellerTermService;

    @Autowired
    private JzfqAuthApi jzfqAuthApi;

    @Autowired
    private IMemberScoreFraudMetrixService iMemberScoreFraudMetrixService;

    @Autowired
    private ProductCoreMapper productCoreMapper;

    @Autowired
    private ProductCoreService productCoreService;

    @Autowired
    private RiskForeignService riskForeignService;

    @Autowired
    private OrdersTradeService ordersTradeService;

    @Autowired
    private SellerSettlePointMapper sellerSettlePointMapper;

    @Autowired
    private PersonalCreditAccountService personalCreditAccountService;

    @Autowired
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SellerAddressService sellerAddressService;

    @Autowired
    private OrdersProductService ordersProductService;

    @Autowired
    private OrdersUserService ordersUserService;

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private MemberCenterHandler memberCenterHandler;

    @Autowired
    private OrdersSettleService ordersSettleService;

    @Autowired
    private OrderRiskService orderRiskService;

    @Override
    public List<OrdersBase> getAllList(OrdersBase search) {
        List<OrdersBase> result = ordersBaseManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<OrdersBase> getList(Integer page, Integer pageSize, OrdersBase search) {
        PageHelper.startPage(page, pageSize);
        Page<OrdersBase> listPage = ordersBaseManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    public ListResultRes<Map<String, Object>> getOrderBaseRelatedInfoList(Integer page, Integer pageSize, OrderListReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = ordersBaseManualMapper.findOrderBaseRelatedInfoList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(OrdersBase ordersBase) {
        ordersBaseMapper.insert(ordersBase);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrdersBase ordersBase) {
        ordersBaseMapper.updateByPrimaryKey(ordersBase);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        OrdersBase ordersBase = ordersBaseMapper.selectByPrimaryKey(id);
        if (ordersBase == null) {
            throw new RuntimeException("要删除的订单数据不存在。");
        }
        ordersBaseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OrdersBase getOrderById(Integer id) {
        OrdersBase ordersBase = ordersBaseMapper.selectByPrimaryKey(id);
        if (ordersBase == null) {
            throw new RuntimeException("要查询的订单数据不存在。");
        }
        return ordersBaseMapper.selectByPrimaryKey(id);
    }

    @Override
    public OrdersBase getByOrderSn(String orderSn) {
        return ordersBaseManualMapper.findEntityByOrderSN(orderSn);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderComplete(String orderSn, boolean isFinished) {
        if (isFinished) {
            OrdersBase ordersBase = this.getByOrderSn(orderSn);
            boolean isUpdate = ordersBase != null
                    && (ordersBase.getOrderState().equals(OrderStatus.ORDER_STATE_160.getCode()) || ordersBase.getOrderState().equals(OrderStatus.ORDER_STATE_170.getCode()))
                    && !ordersBase.getOrderState().equals(OrderStatus.ORDER_STATE_180.getCode());
            if (isUpdate) {
                ordersBase.setOrderState(OrderStatus.ORDER_STATE_180.getCode());
                ordersBase.setFinishTime(new Date());
                ordersBaseMapper.updateByPrimaryKey(ordersBase);
                log.info("订单[{}]还款完成修改订单状态为已完成", orderSn);
                //TODO:缺少订单操作人信息
                systemLogSupport.orderLogSave(0, "root", ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.ORDER_STATE_180.getMessage(), OrderStatus.ORDER_STATE_180.getCode());
                //添加回调日志
                systemLogSupport.callAssetsOperationLogSave(orderSn, 1, String.valueOf(ForeignInterfaceServiceType.DESCRIBE_270.getCode()), null, null, orderSn + ":" + isFinished, DateUtil.getDate(), DateUtil.getDate(), "订单还款完成回调日志记录");
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void riskCallback(RiskCallbackReq req) {
        Integer orderState = Integer.parseInt(req.getStatus());
        String orderSn = req.getOrderSn();
        OrdersBase ordersBase = this.getByOrderSn(orderSn);
        if (ordersBase.getOrderState() != OrderStatus.ORDER_STATE_135.getCode()) {
            throw new RuntimeException(orderSn + "订单状态：" + ordersBase.getOrderState() + "，当前不是可以进行风控审核的状态，请联系管理员。");
        }
        //风控回调记录风控回调的日志
        systemLogSupport.callRiskOperationLogSave(orderSn, 1, String.valueOf(ForeignInterfaceServiceType.DESCRIBE_250.getCode()), null, String.valueOf(orderState), null, DateUtil.getDate(), DateUtil.getDate(), "风控审核回调，140成功，150失败，风控返回原因：" + req.toString());
        if (ordersBase != null) {
            //修改订单状态
            ordersBase.setOrderState(orderState);
            //更新状态
            this.update(ordersBase);
            //添加订单操作日志
            systemLogSupport.orderLogSave(null, null, ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.getMsgByCode(orderState), orderState);
            log.info("调用订单[{}]完成操作传入状态[{}]修改成功", orderSn, orderState);
            OrdersProduct ordersProduct = ordersProductService.getByOrderSn(orderSn);
            //进件处理

            if (orderState == OrderStatus.ORDER_STATE_150.getCode()) {
                //交易复核失败
                // 二期店中店订单扣减恢复库存
                goodsStockService.unFrozenStock(ordersBase, ordersProduct);
                //1、调用资匹取消接口
                assetPlatformService.closeOrder(orderSn);
                if (ordersBase.getCreditFlag() == 0) {
                    //循环额度调用恢复额度接口，单笔授信不需要调用
                    //调用核心恢复额度接口
                    personalCreditAccountService.recoverByCertNo(ordersBase.getMemberId().toString(), "008", ordersBase.getMoneyOrder(), orderSn);
                }
            } else if (orderState == OrderStatus.ORDER_STATE_140.getCode()) {
                //交易复核通过
                try {
                    //1、调用资匹生成还款计划接口，成功失败则记录状态，
                    AssetEntryCapital entryCapital = new AssetEntryCapital();
                    entryCapital.setAmount(ordersBase.getMoneyOrder());
                    entryCapital.setCouponAmount(new BigDecimal("0"));
                    entryCapital.setApplication(BankCapitalType.CAPITAL_TYPE_SC.getRetCode());

                    //查询商户扣点率
                    SellerSettlePointQuery sellerSettlePointQuery = new SellerSettlePointQuery();
                    sellerSettlePointQuery.createCriteria().andSellerIdEqualTo(ordersBase.getSellerId());
                    List<SellerSettlePoint> sellerSettlePointList = sellerSettlePointMapper.selectByExample(sellerSettlePointQuery);
                    if (sellerSettlePointList == null || sellerSettlePointList.size() == 0) {
                        throw new TouchCodeException();
                    }
                    BigDecimal point = new BigDecimal(new Double(sellerSettlePointList.get(0).getSettlePoint()).toString());
                    entryCapital.setServiceFee(ordersBase.getMoneyOrder().multiply(point).toString());//商户服务费

//                    double d = 301353.05;
//                    System.out.println(new BigDecimal(new Double(d).toString()));
//                    System.out.println(new BigDecimal("301353.05"));
//                    System.out.println(new BigDecimal("301353.895898895455898954895989"));

                    //获取订单交易
                    OrdersTrade ordersTrade = ordersTradeService.getByOrderSn(orderSn);
                    //查询订单用户
                    OrdersUser ordersUser = ordersUserService.getByOrderId(ordersBase.getId());
                    //2、通过member_id 到用户中心获取身份证号码等其他信息
                    MemberInfo member = memberCenterHandler.getMemberById(ordersBase.getMemberId());
                    AuthResult authResult = new AuthResult();
                    authResult.setUserId(ordersBase.getMemberId());
                    authResult.setChannel("1");
                    JsonResult<JzfqAuthInfoDto> res = jzfqAuthApi.getInfoByUserId(authResult);
                    if (!res.getCode().equals("SUCCESS")) {
                        throw new BusinessException("获取认证中心数据失败");
                    }
                    JzfqAuthInfoDto jzfqAuthInfoDto = res.getResult();

                    //从核心获取用户银行卡信息接口
                    BankCardResult bankCardResult = payService.getBankCard(ordersBase.getMemberId().toString(), ordersTrade.getCapitalId().toString());
                    entryCapital.setBankCardNumber(bankCardResult.getCardNo());
                    entryCapital.setBankCustomer(bankCardResult.getCustomerName());
                    entryCapital.setBankCardId(bankCardResult.getId());//咨询丁浩id确定是bankid
                    entryCapital.setBankId(bankCardResult.getBankCode());
                    entryCapital.setBankName(bankCardResult.getBankName());
                    entryCapital.setBankPhone(bankCardResult.getPhoneNumber());
                    entryCapital.setBehead(0);    //是否砍头息	number	必填 1是、0否
                    entryCapital.setCallbackUrl("/foreign/order/capitalBack");//资匹成功后回调接口
                    entryCapital.setCapitalCode(ordersTrade.getCapitalId());

                    AssetEntryCapital.EntryPerson entryPerson = new AssetEntryCapital.EntryPerson();
                    entryPerson.setAge(CommonUtil.getAgeByIdcard(ordersUser.getIdCard()).intValue());

                    entryPerson.setCertBackImageUrl(jzfqAuthInfoDto.getAuthIdentityDetail().getCertBackImageUrl());
                    entryPerson.setCertFrontImageUrl(jzfqAuthInfoDto.getAuthIdentityDetail().getCertFrontImageUrl());

                    entryPerson.setCertNo(jzfqAuthInfoDto.getAuthIdentityDetail().getCertNo());
                    String valid = jzfqAuthInfoDto.getAuthIdentityDetail().getCertValidTime();
                    String[] v = valid.split("-");
                    entryPerson.setCertValidStart(v[0].replace(".", "-"));
                    entryPerson.setCertValidEnd(v[1].replace(".", "-"));

                    ServiceResult<MemberScoreFraudmetrix> memberScoreFraudmetrixResult = iMemberScoreFraudMetrixService.getLastScoreByChannel(ordersBase.getMemberId(), 1);
                    if (!memberScoreFraudmetrixResult.getSuccess() || memberScoreFraudmetrixResult.getResult() == null) {
                        entryPerson.setJuziScore(0);//同盾分(非桔盾分)
                    } else {
                        MemberScoreFraudmetrix memberScoreFraudmetrix = memberScoreFraudmetrixResult.getResult();
                        entryPerson.setJuziScore(memberScoreFraudmetrix.getScore() == null ? 0 : Integer.parseInt(memberScoreFraudmetrix.getScore()));//同盾分(非桔盾分)
                    }

                    entryPerson.setCertValidTime(valid);
                    entryPerson.setCustomerId(ordersBase.getMemberId());
                    entryPerson.setCustomerName(ordersUser.getName());
                    entryPerson.setCustomerPhone(member.getMobile());
                    entryPerson.setCertAddress(jzfqAuthInfoDto.getAuthIdentityDetail().getCertAddress());
                    entryPerson.setSex(CommonUtil.getGenderByIdCard(jzfqAuthInfoDto.getAuthIdentityDetail().getCertNo()));

                    entryPerson.setCompanyName(jzfqAuthInfoDto.getAuthCompanyDetail().getCompanyName());
                    entryPerson.setCompanyPhone(jzfqAuthInfoDto.getAuthCompanyDetail().getCompanyPhone());
                    entryPerson.setCompanyEntryTime(jzfqAuthInfoDto.getAuthCompanyDetail().getEntryTime());
                    entryPerson.setCompanyWorkArea(jzfqAuthInfoDto.getAuthCompanyDetail().getWorkA());
                    entryPerson.setCompanyWorkAreaCode(jzfqAuthInfoDto.getAuthCompanyDetail().getWorkACode());
                    entryPerson.setCompanyWorkCity(jzfqAuthInfoDto.getAuthCompanyDetail().getWorkC());// 工作地址市非必填
                    entryPerson.setCompanyWorkCityCode(jzfqAuthInfoDto.getAuthCompanyDetail().getWorkCCode());// 工作地址市代码非必填
                    entryPerson.setCompanyWorkPro(jzfqAuthInfoDto.getAuthCompanyDetail().getWorkP());// 工作地址省非必填
                    entryPerson.setCompanyWorkProCode(jzfqAuthInfoDto.getAuthCompanyDetail().getWorkPCode());// 工作地址省代码非必填
                    entryPerson.setCompanyDuty(jzfqAuthInfoDto.getAuthCompanyDetail().getCompanyDuty());
                    entryPerson.setCompanyAddress(jzfqAuthInfoDto.getAuthCompanyDetail().getCompanyAddress());

                    entryCapital.setEntryPerson(entryPerson);

                    entryCapital.setLoanPurpose("DAILY_SHOPPING");//婚宴------> 婚庆喜宴 WEDDING_BANQUET 旅游 ------>旅行远足 HIKING 教育------> 进修学习 FURTHER_STUDY 家电 ------>日常购物 DAILY_SHOPPING
                    entryCapital.setNum(1);
                    entryCapital.setOrderId(ordersBase.getOrderSn());
                    entryCapital.setOrderType(2);//7.26和张盼盼、张颖菁确认传2
                    log.info("ordersBase.getFirstpayratio()：" + ordersBase.getFirstpayratio());
                    entryCapital.setPaymentRatio(ordersBase.getFirstpayratio());
                    entryCapital.setPeriod(ordersTrade.getTerm());
                    entryCapital.setPrice(ordersBase.getMoneyOrder());
                    //添加认证项
                    entryCapital.setAuthList(getAuthSteps(jzfqAuthInfoDto));

                    //根据用户选择的期数查询出核心的基础费率（核心的基础费率已维护在新零售数据库中）
                    int type = 0;
                    if (ordersBase.getCreditFlag() == 1) {//0循环额度，1单笔授信
                        type = 1;
                    }
                    List<ProductCore> productCores = productCoreService.findAll(ordersTrade.getTerm(), type);

                    if (productCores == null || productCores.size() == 0) {
                        throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0047);
                    }
                    ProductCore productCore = productCores.get(0);
                    entryCapital.setProductId(productCore.getPrductId());//资匹的产品费率id

                    entryCapital.setProductRate(productCore.getMonthRate());
                    entryCapital.setRate(ordersBase.getMonthRatio());//此值是小程序获取的sellerTerm表的afterDiscountRate，传递到后台保存到 orders_base 表的monthratio

                    entryCapital.setStoreOrderTime(DateUtil.formatDateTime(ordersBase.getConfirmTime()));
                    entryCapital.setTitle(ordersProduct.getProductName());

                    entryCapital.setGoodsName(member.getRealName());

                    entryCapital.setProductCode("03");//01现金贷  02线上分期 03线下分期 04车险

                    boolean isCZBT = orderRiskService.isCZBT(ordersBase.getSellerId());
                    if(isCZBT){
                        entryCapital.setProductCode("04");//01现金贷  02线上分期 03线下分期 04车险
                    }

                    SellerAddress sellerAddress = sellerAddressService.getBySellerId(ordersBase.getSellerId());
                    entryCapital.setGoodsProvince(sellerAddress.getProvinceName());
                    entryCapital.setGoodsArea(sellerAddress.getAreaName());
                    entryCapital.setGoodsPhone(member.getMobile());
                    entryCapital.setGoodsAddress(sellerAddress.getAddressInfo());
                    entryCapital.setGoodsCity(sellerAddress.getCityName());

                    if(ordersBase.getOrderType().equals(OrderType._1.getCode())){
                        //一期扫码店
                        //根据seller_id,term获取分期信息表
                        SellerTerm sellerTerm = sellerTermService.getSellerTermByParams(ordersBase.getSellerId(), ordersTrade.getTerm());
                        if (sellerTerm.getMonthRate().compareTo(0.00) == 0) {
                            entryCapital.setFreeInterest(1);
                        }else{
                            entryCapital.setFreeInterest(0);
                        }
                    } else{
                        //二期便利店 都免息 后期不都免息则需要再次调整表结构
                        entryCapital.setFreeInterest(1);//分期信息表的折后费率本系统 0免息  100不免息   是否免息 1是 0 否
                    }




                    //获取首期还款日
                    entryCapital.setRepayDate(DateUtil.formatDateTime(DateUtil.calculateActualDateAfterMonths(ordersBase.getConfirmTime(), 1, DateUtil.getDay(ordersBase.getConfirmTime()))));

                    List<AssetEntryCapital.Contacts> contactsList = new ArrayList<>();
                    AssetEntryCapital.Contacts contacts1 = new AssetEntryCapital.Contacts();
                    String f1 = jzfqAuthInfoDto.getAuthLinkDetail().getLinkInfoF();//联系人1
                    String f2 = jzfqAuthInfoDto.getAuthLinkDetail().getLinkInfoT();//联系人2
                    log.info("联系人1：{}，联系人2：{}", f1, f2);
                    String[] f1_ = f1.split("\\|\\|");
                    String[] f2_ = f2.split("\\|\\|");
                    contacts1.setContractsName(f1_[1]);
                    contacts1.setContractsPhone(f1_[2]);
                    contactsList.add(contacts1);
                    AssetEntryCapital.Contacts contacts2 = new AssetEntryCapital.Contacts();
                    contacts2.setContractsName(f2_[1]);
                    contacts2.setContractsPhone(f2_[2]);
                    contactsList.add(contacts2);
                    entryCapital.setContacts(contactsList);

                    //获取商户
                    Seller seller = sellerMapper.selectByPrimaryKey(ordersBase.getSellerId());
                    AssetEntryCapital.EntryMerchant entryMerchant = new AssetEntryCapital.EntryMerchant();
                    entryMerchant.setMerchantId(seller.getSellerCode());
                    entryCapital.setEntryMerchant(entryMerchant);

                    entryCapital.setGoodsProvinceCode(sellerAddress.getProvinceCode());//goodsProvinceCode  收货省编码
                    entryCapital.setGoodsCityCode(sellerAddress.getCityCode());//goodsCityCode  收货市编码
                    entryCapital.setGoodsAreaCode(sellerAddress.getAreaCode());//goodsAreaCode  收货区编码

                    // 获取通讯录
                    entryCapital.setEntryTelbooks(riskForeignService.getEntryTelbooks(member.getIdcard(), member.getRealName(), member.getMobile(), orderSn));
                    // 资匹进件
                    entryCapital.setEntryTelbooks(riskForeignService.getEntryTelbooks(member.getIdcard(), member.getRealName(), member.getMobile(), orderSn));

                    assetPlatformService.entryCapital(entryCapital);
                    //如果订单状态是成功，且订单类型是2、便利店订单，则进行生成随机提货码
                    if (orderState == OrderStatus.ORDER_STATE_140.getCode() && ordersBase.getOrderType().equals(OrderType._2.getCode())) {
                        ordersBase.setTakeCode(this.generateTakeCode());
                    }
                    //推送资匹进件成功，改状态为155
                    ordersBase.setOrderState(OrderStatus.ORDER_STATE_155.getCode());
                    this.update(ordersBase);
                    //记录订单操作日志
                    systemLogSupport.orderLogSave(null, null, ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.ORDER_STATE_155.getMessage(), OrderStatus.ORDER_STATE_155.getCode());

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("调用资匹进件失败:{}", e);
                    throw new RuntimeException("调用资匹进件失败。");
                }

            }

        }
    }

    private List<Integer> getAuthSteps(JzfqAuthInfoDto jd) {
        List<Integer> authSteps = new ArrayList<>();
        if (jd.getAuthIdentityDetail() != null) {
            authSteps.add(1);//身份认证
        }
        if (jd.getAuthBasicDetail() != null) {
            authSteps.add(2);//基本信息
        }
        if (jd.getAuthCompanyDetail() != null) {
            authSteps.add(9);//单位信息
        }
        if (jd.getAuthFaceDetail() != null) {
            authSteps.add(4);//face
        }
        if (jd.getAuthLinkDetail() != null) {
            authSteps.add(3);//联系人
        }
        if (jd.getZhimaData() != null) {
            authSteps.add(6);//芝麻
        }
        if (jd.getOperatorData() != null) {
            authSteps.add(7);//运行商
        }
        if (jd.getJdData() != null) {
            authSteps.add(8);//京东
        }
        if (jd.getAuthIousDetail() != null) {
            authSteps.add(5);//白条
        }
        return authSteps;
    }

    /**
     * 资匹回调
     *
     * @param req
     */
    @Override
    public void capitalBack(CapitalBackReq req) {
        OrdersBase ordersBase = this.getByOrderSn(req.getOrderId());
        if (ordersBase != null) {
            ordersBase.setOrderState(OrderStatus.ORDER_STATE_155.getCode());
            //更新订单状态
            ordersBaseMapper.updateByPrimaryKey(ordersBase);
            OrdersTradeQuery queryTrade = new OrdersTradeQuery();
            queryTrade.createCriteria().andTermEqualTo(ordersBase.getId());
            List<OrdersTrade> ordersTradeList = ordersTradeMapper.selectByExample(queryTrade);
            OrdersTrade ordersTrade = ordersTradeList.get(0);
            ordersTrade.setCapitalOrderId(req.getCapitalOrderId());
            ordersTrade.setContractNum(req.getContractNum());
            ordersTrade.setCapitalId(req.getCapitalCode());
            //更新交易信息
            ordersTradeMapper.updateByPrimaryKey(ordersTrade);
            //添加订单操作日志
            systemLogSupport.orderLogSave(null, null, ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.getMsgByCode(ordersBase.getOrderState()), ordersBase.getOrderState());

            try {
                //添加订单结算 记录
                ordersSettleService.create(ordersBase.getId(), ordersBase.getOrderSn(), new BigDecimal("0"), new BigDecimal("0"));
            } catch (Exception e) {
                log.info("订单[{}]资匹成功回调，添加订单结算失败:{}", ordersBase.getOrderSn(), e.getMessage());
            }
        }
        systemLogSupport.callAssetsOperationLogSave(ordersBase.getOrderSn(), ForeignInterfaceType.REQ.getCode(), String.valueOf(ForeignInterfaceServiceType.DESCRIBE_230.getCode()), "", JSON.toJSONString(req), "", DateUtil.getDate(), DateUtil.getDate(), "资匹回调成功");
    }

    /**
     * 定时任务，将待收货状态自动变成已收货 155 -》 160
     */
    @Override
    //@TargetDataSource(name = "master")
    public void ChangeOrderStatus() {
        //1、查询所有状态为 155资匹成功，待交货（待收货）  的订单数据
        //2、查询对应的变为155的记录日志的时间超过24小时的订单
        //3、将订单状态改为160已交货（已收货）
        //4、记录日志
        OrdersBaseQuery query = new OrdersBaseQuery();
        query.createCriteria().andOrderStateEqualTo(OrderStatus.ORDER_STATE_155.getCode());
        //1
        List<OrdersBase> list = ordersBaseMapper.selectByExample(query);
        if (list != null && list.size() > 0) {

            //将ordersbase中的id存到list
            List<Integer> ids = list.stream().map(OrdersBase::getId).collect(Collectors.toList());
            //2
            List<Map<String, Object>> listMap = orderLogManualMapper.selectByOrdersId(OrderStatus.ORDER_STATE_155.getCode(), ids, 24);
            //listMap 的 order_id 存到ordersIds
            List<Integer> ordersIds = listMap.stream().map(x -> {
                return (Integer) x.get("order_id");
            }).collect(Collectors.toList());
            //3
            ordersBaseManualMapper.updateStatusByIds(OrderStatus.ORDER_STATE_160.getCode(), ordersIds);
            //4
            systemLogSupport.orderLogBatchSave(listMap, "客户订单24小时后从待收货自动变为已收货", OrderStatus.ORDER_STATE_160.getCode());
        }
    }

    /**
     * 定时任务，修改状态，时间间隔minutes分钟 orgStatus -》 destStatus
     * changeOrderStatus(OrderStatus.ORDER_STATE_110, OrderStatus.ORDER_STATE_200, 48*60);
     */
    @Override
    //@TargetDataSource(name = "master")
    public void changeOrderStatus(int orgStatus, int destStatus, int minutes) {
        OrdersBaseQuery query = new OrdersBaseQuery();
        query.createCriteria().andOrderStateEqualTo(orgStatus);
        //1
        List<OrdersBase> list = ordersBaseMapper.selectByExample(query);
        if (list != null && list.size() > 0) {

            int hours = minutes / 60;
            //将ordersbase中的id存到list
            List<Integer> ids = list.stream().map(OrdersBase::getId).collect(Collectors.toList());
            //2
            List<Map<String, Object>> listMap = orderLogManualMapper.selectByOrdersId(orgStatus, ids, hours);
            //listMap 的 order_id 存到ordersIds
            List<Integer> ordersIds = listMap.stream().map(x -> {
                return (Integer) x.get("order_id");
            }).collect(Collectors.toList());
            //3
            ordersBaseManualMapper.updateStatusByIds(destStatus, ordersIds);
            //4
            String content = String.format("客户订单d%小时后从s%自动变为s%", hours, OrderStatus.getMsgByCode(orgStatus), OrderStatus.getMsgByCode(destStatus));
            systemLogSupport.orderLogBatchSave(listMap, content, destStatus);
        }

    }


    /**
     * 根据商户id修改订单状态  冻结商户操作
     *
     * @param sellerId
     */
    @Override
    public void modifyBySellerId(Integer sellerId) {
        ordersBaseManualMapper.modifyBySellerId(sellerId);
    }

    @Override
    public List<Map<String, Object>> findOrderAndUserList(OrdersBase order) {
        return ordersBaseManualMapper.findOrderAndUserList(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String generateTakeCode() {
        //生成随机码
        String takeCode = this.getRandomTakeCode();
        log.info("生成随机提货码[{}]", takeCode);
        //查询数据库是否已经存在该随机提货码
        while (ordersBaseManualMapper.countNonDeliveryTakeCodeRepeat(takeCode) > 0) {
            String takeCodeOld = takeCode;
            takeCode = this.getRandomTakeCode();
            log.info("生成随机提货码[{}]存在,从新生成提货码[{}]", takeCodeOld, takeCode);
        }
        log.info("生成最终随机提货码[{}]", takeCode);
        return takeCode;
    }

    @Override
    public List<Map<String, Object>> findOrderStatusByQuery(String phone, String orderSn, String memberName) {
        OrdersBaseQuery query = new OrdersBaseQuery();
        OrdersBaseQuery.Criteria criteria = query.createCriteria();
        if (StringUtils.isNoneBlank(phone)) {
            MemberInfo memberByMobile = memberCenterHandler.getMemberByMobile(phone);
            criteria.andMemberIdEqualTo(memberByMobile.getId());
        }
        if (StringUtils.isNotBlank(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }
        if (StringUtils.isNotBlank(memberName)) {
            criteria.andMemberNameEqualTo(memberName);
        }
        List<OrdersBase> ordersBases = ordersBaseMapper.selectByExample(query);
        return ordersBases.stream().map(o -> {
            Map<String, Object> map = new HashedMap();
            map.put("orderSn", o.getOrderSn());
            map.put("orderState", o.getOrderState());
            map.put("deliverTime", o.getDeliverTime());
            map.put("confirmTime", o.getConfirmTime());
            map.put("finishTime", o.getFinishTime());
            map.put("createTime", o.getCreateTime());
            map.put("pickingTime", o.getPickingTime());
            map.put("takeCode", o.getTakeCode());
            return map;
        }).collect(Collectors.toList());
    }

    //===============

    /**
     * 生成随机TakeCode
     *
     * @return
     */
    private String getRandomTakeCode() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 6; i++) {
            result = result * 10 + array[i];
        }
        return String.format("%06d", result);
    }

}
