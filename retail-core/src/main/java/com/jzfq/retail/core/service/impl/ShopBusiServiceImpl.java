package com.jzfq.retail.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.vo.WxLoginInfo;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.CheckRegisterReq;
import com.jzfq.retail.bean.vo.req.CommitOrderReq;
import com.jzfq.retail.bean.vo.req.CommitPayReq;
import com.jzfq.retail.bean.vo.req.IntoConfirmOrderReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.common.util.IPUtil;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.*;
import com.jzfq.retail.core.call.domain.AssetBankCapital;
import com.jzfq.retail.core.call.domain.AssetBankCapitalResult;
import com.jzfq.retail.core.call.domain.BankCardResult;
import com.jzfq.retail.core.call.domain.PersonAuthenticationResult;
import com.jzfq.retail.core.call.service.AssetPlatformService;
import com.jzfq.retail.core.call.service.PayService;
import com.jzfq.retail.core.call.service.PersonCreditAccountService;
import com.jzfq.retail.core.call.service.PersonalCreditAccountService;
import com.jzfq.retail.core.config.GPSHandler;
import com.jzfq.retail.core.config.MemberCenterHandler;
import com.jzfq.retail.core.dao.OrdersBaseMapper;
import com.jzfq.retail.core.dao.OrdersTradeMapper;
import com.jzfq.retail.core.dao.ProductGoodsMapper;
import com.jzfq.retail.core.dao.ScanCodeRecordMapper;
import com.jzfq.retail.core.dao.manual.OrdersBaseManualMapper;
import com.jzfq.retail.core.datasource.TargetDataSource;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * import java.util.Map;
 * <p>
 * /**
 *
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月6日 14:43
 * @Description: 商户端小程序service接口
 */
@Slf4j
@Service
public class ShopBusiServiceImpl implements ShopBusiService {

    @Value("${spring.redis.timeout}")
    private int REDIS_TIMEOUT;

    @Autowired
    private MemberCenterHandler memberCenterHandler;

    @Autowired
    private OrdersBaseMapper ordersBaseMapper;

    @Autowired
    private OrdersBaseService ordersBaseService;

    @Autowired
    private OrdersUserService ordersUserService;

    @Autowired
    private OrdersBaseManualMapper ordersBaseManualMapper;

    @Autowired
    private OrdersTradeMapper ordersTradeMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private PayService payService;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private GPSHandler gpsHandler;

    @Autowired
    private PersonCreditAccountService personCreditAccountService;

    @Autowired
    private PersonalCreditAccountService personalCreditAccountService;

    @Autowired
    private AssetPlatformService assetPlatformService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private OpenidRecordService openidRecordService;

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ScanCodeRecordMapper scanCodeRecordMapper;

    @Autowired
    private OrderRiskService orderRiskService;

    @Autowired
    private SellerAddressService sellerAddressService;

    @Autowired
    private OrdersProductService ordersProductService;

    @Autowired
    private OrdersTradeService ordersTradeService;

    @Autowired
    private SellerSingleCreditService sellerSingleCreditService;

    @Autowired
    private OrderQRCodeService orderQRCodeService;

    @Autowired
    private SellerTermService sellerTermService;

    @Autowired
    private ProductInterestFreePeriodService productInterestFreePeriodService;

    @Autowired
    private ProductGoodsMapper productGoodsMapper;

    /**
     * 调用后台查询是否 绑定、认证
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    @TargetDataSource(name = "master")
    public void checkRegister(CheckRegisterReq req) throws TouchCodeException {
        // 整个业务代码需要保存扫码记录，新启线程，不受本方法事务影响
        ScanCodeRecord scanCodeRecord = new ScanCodeRecord();
        TransferUtil.transfer(scanCodeRecord, req);
        scanCodeRecord.setCreateTime(DateUtil.getDate());

        //调用丁鹏用户中心
        ServiceResult<WxLoginInfo> mbi = memberCenterHandler.validateChatLogin_(req.getWxCode());
        if (mbi == null) {
            // 保存扫码记录
            scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0007.getMsg());
            saveScanCodeRecord(scanCodeRecord);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0007);
        }
        log.info("调用户中心接口：mbi-getSuccess：" + mbi.getSuccess());
        log.info("调用户中心接口：mbi-getCode：" + mbi.getCode());

        if (!mbi.getSuccess() && (mbi.getCode().equals("WX_ERR:210003") || mbi.getCode().equals("WX_ERR:210004"))) {
            // 保存扫码记录
            scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0001.getMsg());
            saveScanCodeRecord(scanCodeRecord);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0001);
        }
        if (!mbi.getSuccess() || mbi.getResult() == null) {
            // 保存扫码记录
            scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0005.getMsg());
            saveScanCodeRecord(scanCodeRecord);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0005);
        }
        log.info("调用户中心接口：mbi-getResult：" + mbi.getResult().toString());
        String openId = mbi.getResult().getOpenid();
        scanCodeRecord.setOpenId(openId);

        //IsConnecte 判断是否绑定
        if (!mbi.getResult().getIsConnected()) {
            // 保存扫码记录
            scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0002.getMsg());
            saveScanCodeRecord(scanCodeRecord);
            //需要记录用户的来源为新零售扫码店用户
            openidRecordService.saveOpenidRecord(openId, MemberAuthType._1.getCode());//
            //TODO 保存到数据库  另外提供接口给小程序
            log.info("新零售-小程序-客户端验证用户是否登录结果:用户未绑定，openID：{}", openId);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0002);
        }
        Integer memberId = mbi.getResult().getMemberId();
        //调用核心是否认证
        PersonAuthenticationResult jsob = personCreditAccountService.checkPersonAuthentication(String.valueOf(memberId));
        if (jsob == null) {
            // 保存扫码记录
            scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0006.getMsg());
            saveScanCodeRecord(scanCodeRecord);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0006);
        }
        log.info("调用核心是否认证结果：{}，memberID：{}", jsob.getCode(), memberId);
        openidRecordService.saveOrUpdate(openId, memberId, jsob.getCode());
        switch (jsob.getCode()) {//返回状态码 0未认证，1认证审核中，2，认证审核成功，3认证审核失败
            case 0:
                // 保存扫码记录
                scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0003.getMsg());
                saveScanCodeRecord(scanCodeRecord);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0003);
            case 1:
                // 保存扫码记录
                scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0017.getMsg());
                saveScanCodeRecord(scanCodeRecord);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0017);
            case 2:
                //保存订单用户信息
                saveOrderUser(mbi.getResult(), req);
                log.info("新零售-小程序-客户端验证用户结果：成功，openID：{}，memberID：{}", openId, mbi.getResult().getMemberId());
                // 保存扫码记录
                scanCodeRecord.setRemark("客户端验证用户结果：成功");
                saveScanCodeRecord(scanCodeRecord);
                break;
            case 3:
                // 保存扫码记录
                scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0018.getMsg());
                saveScanCodeRecord(scanCodeRecord);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0018);
            default:
                // 保存扫码记录
                scanCodeRecord.setRemark(TouchApiCode.TOUCH_API_CODE_0006.getMsg());
                saveScanCodeRecord(scanCodeRecord);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0006);
        }
    }

    /**
     * 商城用户小程序通过扫描二维码查询订单、同时返回分期数，和分期还款计划详情
     * 1、先判断此二维码是否有效且没有被其他人使用
     * 2、再查询订单信息
     * 3、查询分期数
     * 4、试算分期还款详情
     * 5、根据规则判断是单笔授信还是信用额度
     * 选择单笔授信需满足3个条件，
     * 1、商户开通了单笔授信；
     * 2、此商品设置了单笔授信的最低价格；
     * 3、单笔授信的价格超过了循环额度目前最大支付值
     * 4、循环额度目前最大支付值=循环额度的剩余额度+首付金额
     *
     * @param req
     * @return
     */
    @Override
    public Map<String, Object> intoConfirmOrder(IntoConfirmOrderReq req) throws Exception {
        //0、查询订单判断订单是否二期订单
        OrdersBase ordersBase = ordersBaseService.getByOrderSn(req.getOrderSn());
        if (ordersBase == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0011);
        }
        if(ordersBase.getOrderType().equals(OrderType._1.getCode())) {
            //1、先判断此二维码是否有效且没有被其他人使用
            OrderQRCode orderQRCode = orderQRCodeService.getOrderQRCodeByOrderSn(req.getOrderSn());
            if (orderQRCode == null) {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0009);
            }
            if (DateUtil.compareTo(DateUtil.getDate(), orderQRCode.getLastTime()) != -1) {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0010);
            }
        }

        //判断商户是否冻结
        if (sellerService.sellerIsFrozen(ordersBase.getSellerId())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0012);
        }
        //判断是否在指定位置确认订单
        gpsHandler.GPSValid(GpsCheckFlag.USER_CONFIRM_ORDER, ordersBase.getSellerId(), req.getLat(), req.getLng(), "该客户下单未在指定的范围内");
        //判断用户是否为正常状态
        PersonAuthenticationResult personAuthenticationResult = personCreditAccountService.checkPersonAuthentication(ordersBase.getMemberId() + "");
        if (!personAuthenticationResult.getCode().equals(AuthStatus.ORDER_SOURCE_2.getCode())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0003);
        }
        //9、查询商品名称、
        OrdersProduct ordersProduct = ordersProductService.getByOrderSn(ordersBase.getOrderSn());

        //分期试算
        List<SellerTermEx> termExList = periodTrial(ordersBase, ordersProduct);

        //5、根据规则判断是单笔授信还是信用额度
        Integer isSingleCredit = checkSingle(ordersBase, personAuthenticationResult); //0循环额度，1单笔授信
        //需要把订单确认时计算的使用循环额度还是单笔授信记录到系统
        ordersBase.setCreditFlag(isSingleCredit);
        ordersBaseManualMapper.updateCreditFlagById(ordersBase);
        //6、查询商户地址
        SellerAddress sellerAddress = sellerAddressService.getBySellerId(ordersBase.getSellerId());
        //7、查询订单用户
        OrdersUser ordersUser = ordersUserService.getByOrderSn(ordersBase.getOrderSn());

        //8、查询商铺信息
        Seller seller = sellerService.getSellerById(ordersBase.getSellerId());
        //10、调用风控准入接口
        orderRiskService.riskCheck(ordersBase, sellerAddress, ordersUser, isSingleCredit);

        //封装返回信息
        Map<String, Object> map = Maps.newHashMap();
        //订单
        map.put("order", ordersBase);
        //分期试算
        map.put("termList", termExList);
        //使用单笔授信、循环额度的标识
        map.put("isSingleCredit", isSingleCredit);
        //商户信息
        Map<String, Object> sellerMap = Maps.newHashMap();
        sellerMap.put("sellerAddress", sellerAddress.getAddressInfo());
        sellerMap.put("sellerName", seller.getSellerName());
        //商户信息添加
        map.put("seller", sellerMap);
        //订单商品信息
        map.put("ordersProduct", ordersProduct);
        return map;
    }

    /**
     * 分期试算
     * @param ordersBase
     * @param ordersProduct
     * @return
     */
    private List<SellerTermEx> periodTrial(OrdersBase ordersBase, OrdersProduct ordersProduct) {
        Integer sellerId = ordersBase.getSellerId();
        List<SellerTermEx> termExList = null;
        //3、查询分期数  试算分期还款详情 - 从刘志硕处获取
        List<SellerTerm> terms = sellerTermService.getListBySellerId(ordersBase.getSellerId());
        //获取免息期数
        List<Integer> periods = productInterestFreePeriodService.getInterestFrees(ordersProduct.getProductId());
        if(ordersBase.getOrderType().equals(OrderType._1.getCode())){
            //一期扫码店分期试算
            termExList = Lists.newArrayList();
            try {
                for (SellerTerm term : terms) {
                    SellerTermEx ex = new SellerTermEx();
                    TransferUtil.transfer(ex, term);
                    //解析ob，获取试算分期数据 term.getMonthRate();使用折后费率计算
                    Double termRate = term.getAfterDiscountRate().doubleValue();
                    ex.setIsInterestFree(false);
                    JSONObject ob = personCreditAccountService.getPlanTrial(new BigDecimal("0"), ordersBase.getMoneyOrder(), term.getTerm(), termRate);
                    //每期对应的还款详情
                    ex.setOb(ob);
                    termExList.add(ex);
                }
            } catch (Exception e) {
                log.error("一期扫码店分期试算异常："+e);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0014);
            }
        }else if(ordersBase.getOrderType().equals(OrderType._2.getCode())){
            //二期便利店分期试算
            termExList = Lists.newArrayList();
            try {
                for (Integer term : periods) {
                    SellerTermEx ex = new SellerTermEx();
                    ex.setSellerId(sellerId);
                    ex.setTerm(term);
                    ex.setMonthRate(0.00);
                    ex.setAfterDiscountRate(new BigDecimal(0.00));
                    ex.setDiscount(new BigDecimal(0.00));
                    //分期首付比例默认为0
                    ex.setIsInterestFree(true);
                    JSONObject ob = personCreditAccountService.getPlanTrial(new BigDecimal("0"), ordersBase.getMoneyOrder(), term, 0D);
                    //每期对应的还款详情
                    ex.setOb(ob);
                    termExList.add(ex);
                }
            } catch (Exception e) {
                log.error("二期便利店分期试算异常："+e);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0014);
            }
        }
        return termExList;
    }


    /**
     * 小程序点击提交订单
     *
     * @param req
     */
    @Override
    @Transactional
    public void commitOrder(CommitOrderReq req) throws TouchCodeException {
        //通过微信code获取用户对象
        MemberInfo member = memberCenterHandler.getMemberByWxCode(req.getWxCode());
        String memberName = member.getRealName();
        String idCard = member.getIdcard();
        //3、更新订单记录中的member_id及期数等信息
        OrdersBase ordersBase = ordersBaseService.getByOrderSn(req.getOrderSn());
        if (ordersBase == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0011);
        }
        ordersBase.setFirstpayratio(req.getFirstPayRatio());
        ordersBase.setOrderSn(req.getOrderSn());
        ordersBase.setMemberId(member.getId());//会员ID 从用户中心获取
        ordersBase.setMemberName(memberName);//会员名称 从用户中心获取
        ordersBase.setMakeUser(memberName);
        ordersBase.setTakeCode("");
        ordersBase.setOrderState(OrderStatus.ORDER_STATE_110.getCode());
        ordersBase.setDownpaymentAmount(new BigDecimal("0"));//首付金额
        ordersBase.setMonthlyAmount(req.getMonthPrincipal().add(req.getMonthService()));//月供金额 通过试算得出
        ordersBase.setConfirmTime(DateUtil.getDate());//会员下单时间
        ordersBase.setIsFullPayment(0);//是否全额订单(1：是，0：不是)
        ordersBase.setMonthRatio(req.getRatio());
        //ordersBase.setStagingMoney();//分期总金额
        ordersBase.setIsDel(0);//是否删除(0否，1是)
        //订单取货方式
        ordersBase.setTakeFlag(OrderTakeFlag.EXTRACTION.getCode());
        ordersBaseService.update(ordersBase);
        //保存订单交易
        OrdersTrade ordersTrade = ordersTradeService.getByOrderSn(req.getOrderSn());
        if (ordersTrade != null && ordersTrade.getOrderId() != null) {
            //修改
            setOrdersTrade(ordersTrade, ordersBase, req);
            ordersTradeMapper.updateByPrimaryKey(ordersTrade);
        } else {
            //新增
            ordersTrade = new OrdersTrade();
            ordersTrade.setPaymentStatus(0);//付款状态：0 买家未付款 1 买家已付款
            setOrdersTrade(ordersTrade, ordersBase, req);
            ordersTradeMapper.insert(ordersTrade);
        }
        //订单提交日志记录
        systemLogSupport.orderLogSave(member.getId(), memberName, ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.getMsgByCode(ordersBase.getOrderState()), ordersBase.getOrderState());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> intoOrderPay(String orderSn, Double lng, Double lat) throws Exception {
        //查询订单
        OrdersBase ordersBase = ordersBaseService.getByOrderSn(orderSn);
        if (ordersBase == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0011);
        }
        //查询订单状态是否在待支付
        if (!ordersBase.getOrderState().equals(OrderStatus.ORDER_STATE_110.getCode())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0015);
        }
        //获取该订单的商户
        Seller seller = sellerService.getSellerById(ordersBase.getSellerId());
        if (seller == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0025);
        }
        //判断是否在指定位置支付
        gpsHandler.GPSValid(GpsCheckFlag.USER_PAY_ORDER, ordersBase.getSellerId(), lat, lng, "该客户支付未在指定的范围内");
        //判断商户是否已经冻结
        if (sellerService.sellerIsFrozen(ordersBase.getSellerId())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0012);
        }
        //判断用户是否为正常状态-已经激活信用额度
        PersonAuthenticationResult personAuthenticationResult = personCreditAccountService.checkPersonAuthentication(ordersBase.getMemberId() + "");
        if (!personAuthenticationResult.getCode().equals(AuthStatus.ORDER_SOURCE_2.getCode())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0016);
        }
        //调用账户系统 判断是否设置交易密码
        ServiceResult<MemberInfo> serviceResult = memberCenterHandler.getServiceResultById(ordersBase.getMemberId());

        //查询订单用户信息
        OrdersUser ordersUser = ordersUserService.getByOrderSn(orderSn);
        //查询商户地址
        SellerAddress sellerAddress = sellerAddressService.getBySellerId(seller.getId());
        //获取订单产品信息
        OrdersProduct ordersProduct = ordersProductService.getByOrderSn(orderSn);
        //获取订单Trade信息
        OrdersTrade ordersTrade = ordersTradeService.getByOrderSn(orderSn);
        //调用资匹获取资匹id
        AssetBankCapitalResult assetId = this.getAssetId(ordersBase.getMoneyOrder(), ordersBase.getMemberId(), ordersUser.getIdCard(), ordersBase.getOrderSn(), sellerAddress.getProvinceName(), ordersTrade.getTerm());
        //保存资匹ID
        ordersTrade.setCapitalId(Integer.parseInt(assetId.getCapital()));
        ordersTradeMapper.updateByPrimaryKey(ordersTrade);
        //###从核心获取用户银行卡信息接口
        BankCardResult bankCardResult = payService.getBankCard(ordersBase.getMemberId().toString(), assetId.getCapital());
        //###根据规则再次判断是单笔授信还是信用额度 0循环额度，1单笔授信
        Integer isSingleCredit = checkSingle(ordersBase, personAuthenticationResult);
        if (!isSingleCredit.equals(ordersBase.getCreditFlag())) {
            //如果再次判断的额度标识与上次判断的不一致 更新额度标识
            ordersBase.setCreditFlag(isSingleCredit);
            ordersBaseManualMapper.updateCreditFlagById(ordersBase);
            //调用风控准入
            orderRiskService.riskCheck(ordersBase, sellerAddress, ordersUser, isSingleCredit);
        }
        //拼装返回数据
        Map<String, Object> map = Maps.newHashMap();
        //orderBase数据返回
        Map<String, Object> orderBaseMap = Maps.newHashMap();
        orderBaseMap.put("downpaymentAmount", ordersBase.getDownpaymentAmount());
        orderBaseMap.put("firstpayratio", ordersBase.getFirstpayratio());
        orderBaseMap.put("moneyOrder", ordersBase.getMoneyOrder());
        orderBaseMap.put("mounthlyAmount", "");
        orderBaseMap.put("orderSn", ordersBase.getOrderSn());
        orderBaseMap.put("orderState", ordersBase.getOrderState());
        orderBaseMap.put("sellerId", ordersBase.getSellerId());
        orderBaseMap.put("stagingMoney", ordersBase.getStagingMoney());
        //商户名称
        orderBaseMap.put("sellerName", seller.getSellerName());
        //订单基本信息
        map.put("ordersBase", orderBaseMap);
        //是否设置交易密码 获取用户对象，看交易密码字段是否为空  1已设置 0未设置
        boolean isSetPass = (serviceResult.getResult() == null || StringUtils.isBlank(serviceResult.getResult().getBalancePwd()));
        map.put("balancePwd", isSetPass ? "0" : "1");
        //String address = sellerAddress.getProvinceName() + sellerAddress.getCityName() + sellerAddress.getAreaName() + sellerAddress.getAddressInfo();
        map.put("sellerAddress", sellerAddress.getAddressInfo());
        //产品名称
        map.put("productName", ordersProduct.getProductName());
        map.put("specInfo", ordersProduct.getSpecInfo());
        //期数、月供本金、月服务费
        map.put("term", ordersTrade.getTerm());
        map.put("monthlyPrincipal", ordersTrade.getMonthlyPrincipal());
        map.put("monthlyService", ordersTrade.getMonthlyService());
        map.put("capitalId", ordersTrade.getCapitalId());
        //创建时间
        map.put("createTime", DateUtil.formatDate(ordersBase.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("confirmTime", DateUtil.formatDate(ordersBase.getConfirmTime(), "yyyy-MM-dd HH:mm:ss"));
        //银行卡信息
        String cardNo = null;//银行卡号
        String bankName = null;//银行名称
        String certNo = null;//身份证号码
        if (bankCardResult != null && !StringUtils.isEmpty(bankCardResult.getCardNo())) {
            cardNo = bankCardResult.getCardNo();
            bankName = bankCardResult.getBankName();
            certNo = bankCardResult.getCertNo();
        }
        map.put("cardNo", cardNo);//是否绑卡 cardNo 是否为空
        map.put("bankName", bankName);
        map.put("certNo", certNo);
        return map;
    }

    /**
     * 小程序自动跳转到输入交易密码界面，输入密码后提交到后台，后台验证密码是否正确，
     * 如果正确，后台扣减信用额度，扣减成功后，返回小程序成功消息，返回订单号；否则返回密码错误消息给小程序。
     */
    @Override
    @Transactional
    public void commitPay(CommitPayReq req) {
        //获取订单信息
        OrdersBase ordersBase = ordersBaseService.getByOrderSn(req.getOrderSn());
        //获取订单Trade信息
        OrdersTrade ordersTrade = ordersTradeService.getByOrderSn(req.getOrderSn());
        MemberInfo member = memberCenterHandler.getMemberById(ordersBase.getMemberId());
        //验证交易密码是否正确
        ServiceResult<Boolean> checkVal = memberCenterHandler.checkBalancePwdByMemberId(req.getTradePassword(), ordersBase, member);

        if (checkVal.getResult()) {
            //交易密码正确处理
            if (ordersBase.getCreditFlag() != 0 && ordersBase.getCreditFlag() != 1) {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0046);
            }
            //如果是循环授信则进行循环额度扣减 0循环额度，1单笔授信
            if (ordersBase.getCreditFlag() == 0) {
                //调用信用扣减接口
                personalCreditAccountService.debit(ordersBase.getMemberId(), ordersBase.getMoneyOrder(), ordersBase.getOrderSn());
            }
            // 冻结库存
            goodsStockService.forzenStock(ordersBase);
            //记录成功
            ordersBase.setPaymentTime(DateUtil.getDate());//记录会员支付时间
            ordersBase.setOrderState(OrderStatus.ORDER_STATE_130.getCode());
            ordersBaseMapper.updateByPrimaryKey(ordersBase);

            //订单提交日志记录
            systemLogSupport.orderLogSave(member.getId(), member.getName(), ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.getMsgByCode(ordersBase.getOrderState()), ordersBase.getOrderState());
            //调用风控进进件
            orderRiskService.riskReceive(ordersBase, member, ordersBase.getCreditFlag(), ordersTrade.getCapitalId(), req.getLongitude(), req.getLatitude());
        } else {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0035);
        }
    }

    /**
     * 认证成功后回调
     *
     * @param wxCode
     * @return
     */
    @Override
    public void authSucBack(String wxCode) {
        String openId = memberCenterHandler.wxAuthorizing4Business(wxCode);
        //更新openIdRecord状态
        openidRecordService.updateOpenIdStatus(openId);
    }

    @Override
    public ListResultRes<Map<String, Object>> findOrderListByWxcode(Integer page, Integer pageSize, Integer[] status, String wxcode) throws TouchCodeException {
        //获取member_id
        ServiceResult<WxLoginInfo> mbi = memberCenterHandler.validateChatLogin(wxcode);
        Integer member = mbi.getResult().getMemberId();
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> pages = ordersBaseManualMapper.findOrderListByMember(status, member);
        return ListResultRes.newListResult(pages.getResult(), pages.getTotal(), pages.getPageNum(), pages.getPageSize());
    }

    //-----处理方法-----

    /**
     * 进入支付- 获取资匹id
     *
     * @param amount        订单金额->moneyOrder
     * @param customerId    用户id->memberId
     * @param certNo        身份证号->idCard
     * @param orderId       订单编号->orderSn
     * @param orderProvince 订单城市->provinceName
     * @param period        订单期数->term
     * @return
     */
    private AssetBankCapitalResult getAssetId(BigDecimal amount, Integer customerId, String certNo, String orderId, String orderProvince, Integer period) {
        //资产绑卡银行卡信息
        AssetBankCapital assetBankCapital = new AssetBankCapital();
        assetBankCapital.setAmount(amount);
        assetBankCapital.setApplication(BankCapitalType.CAPITAL_TYPE_SC.getRetCode());
        List<String> list = Lists.newArrayList("1", "2");
        assetBankCapital.setAuthList(list);
        assetBankCapital.setCustomerId(customerId);
        assetBankCapital.setCertNo(certNo);
        assetBankCapital.setOrderId(orderId);
        assetBankCapital.setOrderProvince(orderProvince);
        assetBankCapital.setOrderType(1);
        assetBankCapital.setPeriod(period);
        assetBankCapital.setPeriodType(1);
        assetBankCapital.setProductCode(ProductCodeType.PRODUCT_CODE_TYPE_02.getRetCode());
        //调用资匹平台-资匹绑卡
        return assetPlatformService.bankCapital(assetBankCapital);
    }

    /**
     * 保存订单用户信息、订单交易信息
     *
     * @param res
     * @param req
     */
    private void saveOrderUser(WxLoginInfo res, CheckRegisterReq req) throws TouchCodeException {
        MemberInfo member = memberCenterHandler.getMemberById(res.getMemberId());
        //3、更新订单记录中的member_id及期数等信息
        OrdersBase ordersBase_ = ordersBaseManualMapper.findEntityByOrderSN(req.getOrderSn());
        //如果订单或这不在待确认状态中返回异常
        if (ordersBase_ == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0043);
        }
        if (ordersBase_.getOrderState() > OrderStatus.ORDER_STATE_110.getCode()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0045);
        }
        log.info("订单状态：{};原订单member_id:{};新扫码member_id:{};", ordersBase_.getOrderState(), ordersBase_.getMemberId(), res.getMemberId());
        if (ordersBase_.getOrderState().equals(OrderStatus.ORDER_STATE_110.getCode())) {
            boolean flag = !ordersBase_.getMemberId().equals(res.getMemberId());
            log.info("判断标准：{}", flag);
            if (flag) {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0044);
            }
            return;
        }
        //更新订单信息
        ordersBase_.setMemberId(res.getMemberId());//会员ID 从用户中心获取
        ordersBase_.setMemberName(member.getRealName());//会员名称 从用户中心获取
//        ordersBase_.setOrderType(1);//1、普通订单，2、限时抢购订单，3、团购订单，4、竞价定金订单，5、竞价尾款订单，6、积分商城订单，7.企业购订单
        ordersBaseMapper.updateByPrimaryKey(ordersBase_);

        //保存订单用户
        OrdersUser ordersUser = new OrdersUser();
        ordersUser.setOrderId(ordersBase_.getId());
        ordersUser.setOrderSn(ordersBase_.getOrderSn());
        ordersUser.setIdCard(member.getIdcard());
        ordersUser.setMobile(member.getMobile());
        ordersUser.setEmail(member.getEmail());
        ordersUser.setIp(IPUtil.getServerIp());
        ordersUser.setName(member.getRealName());
        ordersUser.setAddressAll(member.getCensusRegisterAddress());
        ordersUser.setAddressInfo(member.getDomiciliaryAddress());

        ordersUser.setUserSource(UserSource.USER_SOURCE_0.getCode());
        //从redis获取是否为未绑定用户的标识
        String openId = res.getOpenid();
        //根据openid查询openid_record表是否有数据，如果有，说明是从新零售小程序绑定注册的用户
        int num = openidRecordService.queryOpenId(openId);
        if (num > 0) {//用户来源 1新零售用户，0商城用户
            ordersUser.setUserSource(UserSource.USER_SOURCE_1.getCode());
        }
        ordersUserService.saveOrderUser(ordersUser);
    }

    /**
     * 保存订单 trade信息
     *
     * @param ordersTrade
     * @param ordersBase
     * @param req
     */
    private void setOrdersTrade(OrdersTrade ordersTrade, OrdersBase ordersBase, CommitOrderReq req) {
        ordersTrade.setOrderId(ordersBase.getId());
        ordersTrade.setOrderSn(ordersBase.getOrderSn());
        ordersTrade.setMoneyProduct(ordersBase.getMoneyOrder());
        ordersTrade.setMonthlyPrincipal(req.getMonthPrincipal());//月供本金
        ordersTrade.setMonthlyService(req.getMonthService());//月供 服务费
        ordersTrade.setTerm(req.getTerm());
    }

    /**
     * 判断是否走单笔授信 //0循环额度，1单笔授信
     *
     * @return
     */
    private Integer checkSingle(OrdersBase ordersBase, PersonAuthenticationResult personAuthenticationResult) {
        //判断商户是否开启了单笔授信
        SellerSingleCredit credit = sellerSingleCreditService.getEntityBySellerId(ordersBase.getSellerId());
        //是否开启单笔授信， 0未开启，1已开启
        if (credit != null && credit.getIsOpen() == 1) {//是否开启单笔授信， 0未开启，1已开启
            //验证此商品是否设置了单笔授信的最低价格
            Dictionary dictionary = dictionaryService.getEntityByCode("LAST_MONEY");
            if (dictionary != null) {//已设定单笔授信的最低价格
                //剩余授信额度
                //单笔授信的价格超过了循环额度目前最大支付值  单笔授信的价格既是订单价格
                BigDecimal vaAmount = new BigDecimal("0");//单笔授信的价格
                if (personAuthenticationResult.getValidAmount() != null) {//循环额度剩余最大支付值
                    vaAmount = personAuthenticationResult.getValidAmount();
                }
                BigDecimal dictVal = new BigDecimal(dictionary.getDictVal());
                //订单的价格必须大于循环额度剩余最大支付值 并且 订单价格大于单笔授信最低起订价
                if (ordersBase.getMoneyOrder().compareTo(vaAmount) > 0 && ordersBase.getMoneyOrder().compareTo(dictVal) > 0) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * 保存扫码记录
     */
    @Override
    public void saveScanCodeRecord(ScanCodeRecord scanCodeRecord) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                scanCodeRecordMapper.insert(scanCodeRecord);
            }
        });
    }

}
