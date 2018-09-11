package com.jzfq.retail.core.service.impl;

import com.google.common.collect.Maps;
import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.vo.WxLoginInfo;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.CheckRegisterReq;
import com.jzfq.retail.bean.vo.req.GenerateOrderReq;
import com.jzfq.retail.bean.vo.req.GoodsStockReq;
import com.jzfq.retail.bean.vo.req.ScanProductReq;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.common.util.IPUtil;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.*;
import com.jzfq.retail.core.call.domain.GetRepaymentResult;
import com.jzfq.retail.core.call.domain.PersonAuthenticationResult;
import com.jzfq.retail.core.call.domain.WXPay;
import com.jzfq.retail.core.call.service.BillingService;
import com.jzfq.retail.core.call.service.PayService;
import com.jzfq.retail.core.call.service.PersonCreditAccountService;
import com.jzfq.retail.core.config.GPSHandler;
import com.jzfq.retail.core.config.MemberCenterHandler;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.dao.manual.GoodsStockManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class ShopScanServiceImpl implements ShopScanService {


    @Value("${wx.xcx.app_id}")
    private String xcxAppId;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private OrdersUserMapper ordersUserMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrdersProductMapper ordersProductMapper;

    @Autowired
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    private GoodsStockManualMapper goodsStockManualMapper;

    @Autowired
    private ShopBusiService shopBusiService;

    @Autowired
    private OpenidRecordService openidRecordService;

    @Autowired
    private PersonCreditAccountService personCreditAccountService;

    @Autowired
    private GPSHandler gpsHandler;

    @Autowired
    private OrderSnCountService orderSnCountService;

    @Autowired
    private OrdersBaseMapper ordersBaseMapper;

    @Autowired
    private PayService payService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private SellerAddressService sellerAddressService;

    @Autowired
    private MemberCenterHandler memberCenterHandler;

    private final static String WX_PAY_TYPE = "repay";

    private final static String WX_PAY_SOURCE = "xcx";

    /**
     * 小程序店中店扫码获取商品详情
     *
     * @param req
     * @return
     */
    @Override
    public Map<String, Object> queryProductInfo(ScanProductReq req) throws Exception {
        Map<String, Object> map = Maps.newHashMap();

        //0、记录扫码信息
        ScanCodeRecord scanCodeRecord = new ScanCodeRecord();
        scanCodeRecord.setCreateTime(DateUtil.getDate());
        ServiceResult<WxLoginInfo> mbi = memberCenterHandler.validateChatLogin(req.getWxCode());
        scanCodeRecord.setOpenId(mbi.getResult().getOpenid());
        scanCodeRecord.setWxCode(req.getWxCode());
        scanCodeRecord.setOrderSn(req.getSellerId()+"@"+req.getProductId());
        scanCodeRecord.setLatitude(req.getLat());
        scanCodeRecord.setLongitude(req.getLng());
        shopBusiService.saveScanCodeRecord(scanCodeRecord);

        //1、查询商户信息
        Seller seller = sellerMapper.selectByPrimaryKey(req.getSellerId());
        if (seller.getAuditStatus().equals(SellerStatus.ACCOUNT_FROZEN.getCode())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0013);
        }
        //2、查询商户地址
        SellerAddress sellerAddress = sellerAddressService.getBySellerId(req.getSellerId());
        if (sellerAddress == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0057);
        }
        map.put("sellerAddress", sellerAddress);
        //3、查询商品信息
        Product product = productMapper.selectByPrimaryKey(req.getProductId());
        log.info("传入商户：{}，传入商品id：{}， 商品的商户：{}， 商品状态：{}", req.getSellerId(), req.getProductId(), product.getSellerId(), product.getState());
        if (!product.getSellerId().equals(req.getSellerId()) || product.getState().equals(ProductStatus.INVALID.getCode())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0056);
        }

        //4、查询sku && 当前可用库存大于0
        List<Map<String, Object>> goodsStockList = goodsStockManualMapper.selectByProductId(product.getId());
        map.put("goodsStockList", goodsStockList);
        map.put("seller", seller);
        map.put("product", product);
        return map;
    }

    /**
     * 校验sku是否有库存
     *
     * @param req
     * @return
     */
    @Override
    public Map<String, Object> checkGoodsStock(GoodsStockReq req) {
        Map<String, Object> map = Maps.newHashMap();
        GoodsStock goodsStock = goodsStockMapper.selectByPrimaryKey(req.getGoodsStockId());
        if (goodsStock == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0058);
        }
        map.put("count", goodsStock.getCurrentCount());
        map.put("goodsStockId", goodsStock.getId());
        return map;
    }

    /**
     * 校验用户是否注册
     *
     * @param req
     * @return
     */
    @Override
    public void checkRegister(CheckRegisterReq req) {
        // 整个业务代码需要保存扫码记录，新启线程，不受本方法事务影响

        //调用丁鹏用户中心
        ServiceResult<WxLoginInfo> mbi = memberCenterHandler.validateChatLogin(req.getWxCode());

        String openId = mbi.getResult().getOpenid();

        //IsConnecte 判断是否绑定
        if (!mbi.getResult().getIsConnected()) {
            //需要记录用户的来源为新零售扫码店用户
            openidRecordService.saveOpenidRecord(openId, MemberAuthType._2.getCode());
            //TODO 保存到数据库  另外提供接口给小程序
            log.info("新零售-小程序-客户端验证用户是否登录结果:用户未绑定，openID：{}", openId);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0002);
        }
        Integer memberId = mbi.getResult().getMemberId();
        //调用核心是否认证
        PersonAuthenticationResult jsob = personCreditAccountService.checkPersonAuthentication(String.valueOf(memberId));
        if (jsob == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0006);
        }
        log.info("调用核心是否认证结果：{}，memberID：{}", jsob.getCode(), memberId);
        openidRecordService.saveOrUpdate(openId, memberId, jsob.getCode());
        switch (jsob.getCode()) {//返回状态码 0未认证，1认证审核中，2，认证审核成功，3认证审核失败
            case 0:
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0003);
            case 1:
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0017);
            case 2:
                log.info("新零售-小程序-客户端验证用户结果：成功，openID：{}，memberID：{}", openId, mbi.getResult().getMemberId());
                break;
            case 3:
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0018);
            default:
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0006);
        }
    }

    /**
     * 生成订单
     * 0、通过微信code获取用户信息
     * 1、校验用户距离
     * 2、查询商户
     * 3、查询商品
     * 4、查询库存
     * 5、生成订单
     * 6、插入订单产品
     * 7、插入订单用户
     *
     * @param req
     * @return
     */
    @Override
    public Map<String, Object> generateOrder(GenerateOrderReq req) throws TouchCodeException {

        ServiceResult<WxLoginInfo> mbi = memberCenterHandler.validateChatLogin(req.getWxCode());
        //获取用户memberId
        Integer memberId = mbi.getResult().getMemberId();
        String openId = mbi.getResult().getOpenid();
        //2、通过memberId 到用户中心获取身份证号码等其他信息
        MemberInfo member = memberCenterHandler.getMemberById(memberId);

        //校验用户距离
        gpsHandler.GPSValid(GpsCheckFlag.USER_PAY_ORDER, req.getSellerId(), req.getLat(), req.getLng(), "该客户支付未在指定的范围内");
        //查询商户
        Seller seller = sellerMapper.selectByPrimaryKey(req.getSellerId());
        if (seller == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0025);
        }
        //查询商户地址
        SellerAddress sellerAddress = sellerAddressService.getBySellerId(req.getSellerId());
        if (sellerAddress == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0057);
        }

        //查询商品
        Product product = productMapper.selectByPrimaryKey(req.getProductId());
        log.info("传入商户：{}，传入商品id：{}， 商品的商户：{}， 商品状态：{}", req.getSellerId(), req.getProductId(), product.getSellerId(), product.getState());
        if (product == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0056);
        }
        //查询库存
        GoodsStock goodsStock = goodsStockMapper.selectByPrimaryKey(req.getGoodsStockId());
        if (goodsStock == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0058);
        }

        //订单号生成
        String orderSn = orderSnCountService.getOrderSn(req.getSellerId());
        //生成订单
        OrdersBase ordersBase = new OrdersBase();
        ordersBase.setOrderSn(orderSn);
        ordersBase.setSellerId(req.getSellerId());
        ordersBase.setSellerName(seller.getSellerName());
        ordersBase.setMoneyOrder(req.getPrice());
        ordersBase.setMemberId(member.getId());
        ordersBase.setMemberName(member.getRealName());

        ordersBase.setOrderState(OrderStatus.ORDER_STATE_100.getCode());
        ordersBase.setConfirmTime(DateUtil.getDate());
        ordersBase.setOrderType(OrderType._2.getCode());//1扫码店订单，2便利店订单
        ordersBase.setCreateTime(DateUtil.getDate());
        ordersBase.setRemark(req.getRemark());
        ordersBase.setSource(OrderSource.ORDER_SOURCE_5.getCode());
        ordersBase.setTakeCode("");
        ordersBase.setMakeUser(member.getRealName());
        ordersBaseMapper.insert(ordersBase);

        //插入订单产品
        OrdersProduct ordersProduct = new OrdersProduct();
        ordersProduct.setOrdersId(ordersBase.getId());
        ordersProduct.setOrdersSn(orderSn);
        ordersProduct.setSellerId(req.getSellerId());
        ordersProduct.setProductCateId(product.getProductCateId());
        ordersProduct.setProductBrandId(product.getProductBrandId());
        ordersProduct.setProductId(product.getId());
        ordersProduct.setProductName(product.getName1());
        ordersProduct.setProductGoodsId(goodsStock.getProductGoodsId());
        ordersProduct.setMoneyPrice(req.getPrice());
        ordersProduct.setCreateTime(DateUtil.getDate());
        ordersProduct.setNumber(1);
        ordersProductMapper.insert(ordersProduct);

        //插入订单用户
        OrdersUser ordersUser = new OrdersUser();
        ordersUser.setOrderId(ordersBase.getId());
        ordersUser.setOrderSn(orderSn);
        ordersUser.setProvinceId(sellerAddress.getProvinceId());
        ordersUser.setCityId(sellerAddress.getCityId());
        ordersUser.setAreaId(sellerAddress.getAreaId());
        ordersUser.setIp(IPUtil.getServerIp());
        ordersUser.setIdCard(member.getIdcard());
        ordersUser.setName(member.getRealName());
        ordersUser.setMobile(member.getMobile());
        ordersUser.setEmail(member.getEmail());
        ordersUser.setAddressAll(member.getCensusRegisterAddress());
        ordersUser.setAddressInfo(member.getDomiciliaryAddress());

        ordersUser.setUserSource(UserSource.USER_SOURCE_0.getCode());
        //根据openid查询openid_record表是否有数据，如果有，说明是从新零售小程序绑定注册的用户
        int num = openidRecordService.queryOpenId(openId);
        if (num == 1) {//用户来源 1新零售用户，0商城用户
            ordersUser.setUserSource(UserSource.USER_SOURCE_1.getCode());
        }
        ordersUserMapper.insert(ordersUser);
        Map<String, Object> map = Maps.newHashMap();
        map.put("orderSn", orderSn);
        return map;
    }

    @Override
    public String wxRepaymentPay(String wxCode, String orderSn, Integer period) throws TouchCodeException {
        //请求账务系统-获取还款计划
        GetRepaymentResult repayment = billingService.getRepayment(orderSn, period);
        if (repayment == null || StringUtils.isBlank(repayment.getState())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0042);
        }
        if (repayment.getState().equals("5") || repayment.getState().equals("1")) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0071);
        }
        //通过wxCode获取openID
        log.info("请求用户中心-根据微信code获取openId，微信code:{}", wxCode);
        String openId = memberCenterHandler.wxAuthorizing4Business(wxCode);

        log.info("请求用户中心-根据微信code获取openId，微信code:{}，返回结果：{}", wxCode, openId);
        //计算还款金额并转为单位为'分'
        BigDecimal amount = repayment.getInterest().add(repayment.getPrincipal()).add(repayment.getOverDueFee()).multiply(new BigDecimal(100));
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0072);
        }
        WXPay pay = new WXPay();
        //微信小程序APP_ID
        pay.setAppId(xcxAppId);
        //订单编号
        pay.setOrderId(orderSn);
        //商品名称
        pay.setTradeName(repayment.getTitle());
        //还款期数
        pay.setPeriod(period.toString());
        //微信openId
        pay.setOpenId(openId);
        //用户编号
        pay.setCustomerId(repayment.getCustomerId());
        //支付金额
        pay.setAmount(amount.toString());
        //支付类型
        pay.setPayType(WX_PAY_TYPE);
        //支付来源
        pay.setSource(WX_PAY_SOURCE);
        //请求支付
        String result = payService.wxPaymentConfirm(pay);
        return result;
    }

}
