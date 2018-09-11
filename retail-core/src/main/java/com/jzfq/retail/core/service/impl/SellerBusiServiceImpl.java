package com.jzfq.retail.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.domain.Dictionary;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.OrderDeliveryInfoRes;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.common.util.OSSUnitUtil;
import com.jzfq.retail.common.util.QRCodeUtil;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.*;
import com.jzfq.retail.core.call.service.BillingService;
import com.jzfq.retail.core.config.GPSHandler;
import com.jzfq.retail.core.config.MemberCenterHandler;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.dao.manual.*;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月6日 14:43
 * @Description: 商户端小程序service接口
 */
@Slf4j
@Service
public class SellerBusiServiceImpl implements SellerBusiService {

    @Value("${qrcode.logo}")
    private String logoPath;

    @Value("${alibaba.oss.bucket}")
    private String ossBucket;

    @Value("${touch.scan.address}")
    private String touchScanAddress;

    @Value("${touch.test.login}")
    private String testLogin;

    @Autowired
    private OrdersBaseMapper ordersBaseMapper;

    @Autowired
    private OrdersBaseManualMapper ordersBaseManualMapper;

    @Autowired
    private OrderSnCountService orderSnCountService;

    @Autowired
    private OrderQRCodeService orderQRCodeService;

    @Autowired
    private SellerAddressMapper sellerAddressMapper;

    @Autowired
    private OrdersProductMapper ordersProductMapper;

    @Autowired
    private SellerCateBrandRelMapper sellerCateBrandRelMapper;

    @Autowired
    private ProductCateService productCateService;

    @Autowired
    private ProductBrandService productBrandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SellerManualMapper sellerManualMapper;

    @Autowired
    private OrdersTradeMapper ordersTradeMapper;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ProductCateBrandAreasMapper productCateBrandAreasMapper;

    @Autowired
    private SellerImageMapper sellerImageMapper;

    @Autowired
    private OSSUnitUtil ossUnitUtil;

    @Autowired
    private OrdersBaseService ordersBaseService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private GPSHandler gpsHandler;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MemberCenterHandler memberCenterHandler;

    @Autowired
    private OrderImageService orderImageService;

    @Autowired
    private ProductGoodsMapper productGoodsMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private SellerStoreManualMapper sellerStoreManualMapper;

    @Autowired
    private ProductManualMapper productManualMapper;

    @Autowired
    private DictionaryManualMapper dictionaryManualMapper;

    @Autowired
    private OrdersProductService ordersProductService;

    @Autowired
    private ProductImageManualMapper productImageManualMapper;

    /**
     * 商户创建订单
     * TODO: 缺少订单创建人
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderReq req) throws TouchCodeException {
        //查看商户是否冻结
        if (sellerService.sellerIsFrozen(req.getSellerId())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0012);
        }
        //根据商户id查询商户seller_code
        Seller seller = sellerService.getSellerById(req.getSellerId());
        if (seller.getSellerCode().equals(testLogin)) {
            //小程序审核默认登陆账号
        } else {
            //验证GPS
            gpsHandler.GPSValid(GpsCheckFlag.SELLER_CREATE_ORDER, req.getSellerId(), req.getLat(), req.getLng(), "该商户创建订单未在指定的范围内");
        }
        //查询城市区间价位
        SellerAddressQuery SAQuery = new SellerAddressQuery();
        SAQuery.createCriteria().andSellerIdEqualTo(req.getSellerId());
        List<SellerAddress> SAList = sellerAddressMapper.selectByExample(SAQuery);
        if (SAList == null || SAList.size() != 1) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0022);
        }
        SellerAddress sellerAddress = SAList.get(0);
        ProductCateBrandAreasQuery PCBAQuery = new ProductCateBrandAreasQuery();
        PCBAQuery.createCriteria()
                //=分类id
                .andCateIdEqualTo(req.getCateId())
                //=品牌id
                .andBrandIdEqualTo(req.getBrandId())
                //=城市id
                .andAreaIdEqualTo(sellerAddress.getCityCode())
                //=审核通过
                .andStatusEqualTo(ProductCateBrandAreasStatus.AUDITING_SUCCESS.getCode());
        List<ProductCateBrandAreas> PCBAList = productCateBrandAreasMapper.selectByExample(PCBAQuery);
        if (PCBAList == null || PCBAList.size() < 1) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0023);
        }
        ProductCateBrandAreas pcba = PCBAList.get(0);
        if (pcba.getIntervalPriceMin().compareTo(req.getPrice()) > 0 || pcba.getIntervalPriceMax().compareTo(req.getPrice()) < 0) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0024);
        }
        //订单号生成
        String orderSn = orderSnCountService.getOrderSn(req.getSellerId());
        //订单基本信息
        OrdersBase order = new OrdersBase();
        order.setOrderSn(orderSn);
        order.setOrderType(1);
        order.setSellerId(req.getSellerId());
        order.setSellerName(req.getSellerName());
        order.setMakeUser(req.getCustomerName());
        order.setOrderState(OrderStatus.ORDER_STATE_100.getCode());
        order.setMoneyOrder(req.getPrice());
        order.setCreateTime(DateUtil.getDate());
        order.setRemark(req.getRemark());
        order.setSource(OrderSource.ORDER_SOURCE_5.getCode());
        order.setTakeCode("");
        ordersBaseMapper.insert(order); // 保存orderBase

        OrdersProduct product = new OrdersProduct();
        product.setOrdersId(order.getId());
        product.setOrdersSn(orderSn);
        product.setSellerId(req.getSellerId());
        product.setProductCateId(req.getCateId());
        product.setProductBrandId(req.getBrandId());
        product.setProductId(req.getProductId());
        product.setProductName(req.getProductName());
        product.setMoneyPrice(req.getPrice());
        product.setCreateTime(DateUtil.getDate());
        product.setSpecInfo(req.getSpecInfo());
        product.setProLabel(req.getProLabel());
        ordersProductMapper.insert(product);

        //添加图片
        req.getOrderImages().forEach(x -> {
            orderImageService.addOrderImage(order.getId(), orderSn, OrderImageType._1, x.getUrl(), x.getRemark());
        });

        //添加订单操作日志
        systemLogSupport.orderLogSave(null, null, order.getId(), order.getOrderSn(), OrderStatus.getMsgByCode(order.getOrderState()), order.getOrderState());
        return orderSn;
    }

    /**
     * 根据BD后台设置的商户编码获取商户信息
     *
     * @param sellerLogin
     * @return
     */
    @Override
    public Map<String, Object> findSellerInfo(String sellerLogin) throws TouchCodeException {
        Map<String, Object> result = sellerManualMapper.findSellerInfoBySellerLogin(sellerLogin);
        SellerImageQuery SIQ = new SellerImageQuery();
        SIQ.createCriteria()
                .andSellerIdEqualTo((Integer) result.get("sellerId"))
                .andImageTypeEqualTo(SellerImageType.store.name())
                .andImageNameIn(Lists.newArrayList(SellerImageName.IMAGE_NAME_0010.getName(), SellerImageName.IMAGE_NAME_0011.getName(), SellerImageName.IMAGE_NAME_0012.getName()));
        List<String> banners = sellerImageMapper.selectByExample(SIQ).stream().map(SellerImage::getUrl).collect(Collectors.toList());
        result.put("banners", banners);
        return result;
    }

    @Override
    public Map<String, Object> getSimpleOrderInfoByOrderSn(String orderSn) throws TouchCodeException {
        //是否添加位置校验
        Map<String, Object> orderSimpleInfo = ordersBaseManualMapper.findByOrderSn(orderSn);
        return orderSimpleInfo;
    }

    @Override
    @Transactional(rollbackFor = TouchCodeException.class)
    public String getOrderQRCode(String orderSn) throws TouchCodeException {
        try {
            //判断是否存在且未失效
            OrderQRCode qrCode = orderQRCodeService.getOrderQRCodeByOrderSn(orderSn);
            if (qrCode != null && qrCode.getLastTime().after(DateUtil.getDate())) {
                return qrCode.getQrCodeAddr();
            }
            String content = touchScanAddress + orderSn;
            //如果为空或失效则新增一条二维码
            String diskName = "images/QRCode/";
            String url = null;
            String key = orderSn.replace("-", "_") + "_" + System.currentTimeMillis() + ".jpg";
            if (StringUtils.isNotBlank(ossUnitUtil.uploadObject2OSS3(QRCodeUtil.encodeFile(content, logoPath, true), ossBucket, key, diskName))) {
                url = ossUnitUtil.getUrl(diskName + key, ossBucket);
            }
            if (StringUtils.isBlank(url)) {
                log.error("oss上传二维码图片失败，返回地址未开为空");
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0031);
            }
            log.info("oss上传二维码图片地址返回：{}", url);
            //新增二维码对象
            url = url.split("\\?")[0];
            orderQRCodeService.insetOrderQRCode(orderSn, url);
            return url;
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("订单号[{}]生成二维码上传OSS云异常：{}", orderSn, e.getMessage());
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0031);
        }
    }

    @Override
    public ListResultRes<Map<String, Object>> findOrderListBySeller(Integer page, Integer pageSize, OrderSearchReq search, String seller) throws TouchCodeException {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> pages = ordersBaseManualMapper.findOrderListBySeller(search, seller);
        return ListResultRes.newListResult(pages.getResult(), pages.getTotal(), pages.getPageNum(), pages.getPageSize());
    }

    /**
     * 根据订单id获取订单
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findOderInfoById(Integer id) throws TouchCodeException {
        //orders_base:
        OrdersBase base = ordersBaseMapper.selectByPrimaryKey(id);
        if (base == null) {
            throw new RuntimeException("该订单对象不存在或订单号有误");
        }
        OrdersProductQuery query = new OrdersProductQuery();
        query.createCriteria().andOrdersIdEqualTo(id).andOrdersSnEqualTo(base.getOrderSn());
        List<OrdersProduct> ordersProducts = ordersProductMapper.selectByExampleWithBLOBs(query);
        if (ordersProducts != null && ordersProducts.size() != 1) {
            throw new RuntimeException("对象参数异常请联系管理员");
        }
        OrdersProduct product = ordersProducts.get(0);
        OrdersTradeQuery OTQuery = new OrdersTradeQuery();
        OTQuery.createCriteria().andOrderSnEqualTo(base.getOrderSn());
        List<OrdersTrade> OTList = ordersTradeMapper.selectByExample(OTQuery);
        OrdersTrade trade = (OTList != null && OTList.size() > 0) ? OTList.get(0) : new OrdersTrade();
        //返回参数
        Map<String, Object> result = Maps.newHashMap();
        result.put("id", base.getId());
        result.put("sellerId", base.getSellerId());
        result.put("orderSn", base.getOrderSn());
        result.put("productId", product.getProductId());
        result.put("productName", product.getProductName());
        result.put("moneyOrder", base.getMoneyOrder());
        result.put("downpaymentAmount", base.getDownpaymentAmount());
        result.put("monthlyAmount", base.getMonthlyAmount());
        result.put("term", trade.getTerm());
        result.put("confirmTime", base.getConfirmTime() == null ? "" : DateUtil.formatDate(base.getConfirmTime(), "yyyy.MM.dd HH:mm:ss"));
        result.put("paymentTime", base.getPaymentTime() == null ? "" : DateUtil.formatDate(base.getPaymentTime(), "yyyy.MM.dd"));
        result.put("deliverTime", base.getDeliverTime() == null ? "" : DateUtil.formatDate(base.getDeliverTime(), "yyyy.MM.dd"));
        result.put("createTime", base.getCreateTime() == null ? "" : DateUtil.formatDate(base.getCreateTime(), "yyyy.MM.dd"));
        result.put("productBackTime", base.getProductBackTime() == null ? "" : DateUtil.formatDate(base.getProductBackTime(), "yyyy.MM.dd HH:mm:ss"));
        result.put("orderState", base.getOrderState());
        result.put("moneyActSingle", product.getMoneyActSingle());
        result.put("makeUser", base.getMakeUser());
        result.put("takeCode", base.getTakeCode());
        result.put("orderType", base.getOrderType());
        result.put("specInfo", product.getSpecInfo());
        return result;
    }

    @Override
    public OrderDeliveryInfoRes getOrderDeliveryInfo(Integer orderId) throws TouchCodeException {
        //1、根据id查询订单状态是否在可交货的状态
        OrdersBase ordersBase = ordersBaseMapper.selectByPrimaryKey(orderId);
        if (ordersBase == null) {
            throw new RuntimeException("交货订单对象不存在");
        }
        if (ordersBase.getOrderState() != OrderStatus.ORDER_STATE_155.getCode()) {
            throw new RuntimeException("订单不在可以交货的状态，请核查。");
        }
        OrdersProduct ordersProduct = ordersProductService.getByOrderSn(ordersBase.getOrderSn());
        OrderDeliveryInfoRes res = new OrderDeliveryInfoRes();
        res.setOrderId(ordersBase.getId());
        res.setOrderSn(ordersBase.getOrderSn());
        res.setProductName(ordersProduct.getProductName());
        res.setProLabel(ordersProduct.getProLabel());
        res.setSpecInfo(ordersProduct.getSpecInfo());
        List<OrderImageReq> images =orderImageService.getOrderImagesByOrderSnAndState(ordersBase.getOrderSn(), 0).stream().map(x->{
            OrderImageReq orderImage= new OrderImageReq();
            orderImage.setUrl(x.getUrl());
            orderImage.setRemark(x.getRemark());
            return orderImage;
        }).collect(Collectors.toList());
        res.setOrderImages(images);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderDeliveryCommit(OrderDeliveryCommitReq req) throws TouchCodeException {
        //1、根据id查询订单状态是否在可交货的状态
        OrdersBase ordersBase = ordersBaseMapper.selectByPrimaryKey(req.getOrderId());
        if (ordersBase == null) {
            throw new RuntimeException("交货订单对象不存在");
        }
        if (ordersBase.getOrderState() != OrderStatus.ORDER_STATE_155.getCode()) {
            throw new RuntimeException("订单不在可以交货的状态，请核查。");
        }
        ordersBase.setOrderState(OrderStatus.ORDER_STATE_160.getCode());
        ordersBaseMapper.updateByPrimaryKey(ordersBase);

        //添加图片
        req.getOrderImages().forEach(x -> {
            orderImageService.addOrderImage(ordersBase.getId(), ordersBase.getOrderSn(), OrderImageType._1, x.getUrl(), x.getRemark());
        });
        //TODO:缺少订单操作人信息
        systemLogSupport.orderLogSave(null, null, ordersBase.getId(), ordersBase.getOrderSn(), OrderStatus.getMsgByCode(ordersBase.getOrderState()), ordersBase.getOrderState());
    }

    @Override
    public ListResultRes<Map<String, Object>> findSellerProductList(Integer page, Integer pageSize, SellerGoodsSearch search) throws TouchCodeException {
        Integer[] states = null;
        switch (search.getState()) {
            case 0:
                states = new Integer[]{3};
                break;
            case 1:
                states = new Integer[]{6};
                break;
            default:
                states = new Integer[]{3};
        }
        ListResultRes<Map<String, Object>> result = productService.getList(page, pageSize, search.getSellerId(), states);
        return result;
    }

    @Override
    public List<Map<String, Object>> findCateBrandInfo(Integer sellerId) throws TouchCodeException {
        List<Map<String, Object>> result = Lists.newArrayList();
        //查询
        SellerCateBrandRelQuery query = new SellerCateBrandRelQuery();
        query.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerCateBrandRel> lists = sellerCateBrandRelMapper.selectByExample(query);
        //封装
        Map<Integer, List<Integer>> groupMap = lists.stream().collect(Collectors.groupingBy(SellerCateBrandRel::getCateId, Collectors.mapping(SellerCateBrandRel::getBrandId, Collectors.toList())));
        for (Map.Entry<Integer, List<Integer>> entry : groupMap.entrySet()) {
            //分类
            ProductCate cate = productCateService.getEntityById(entry.getKey());
            if (cate == null) {
                continue;
            }
            Map<String, Object> cateMap = Maps.newHashMap();
            cateMap.put("cateId", cate.getId());
            cateMap.put("cateName", cate.getName());
            List<Map<String, Object>> brands = Lists.newArrayList();
            //品牌
            entry.getValue().forEach(x -> {
                ProductBrand brand = productBrandService.getEntityById(x);
                if (brand != null && brand.getState().equals(2)) {
                    Map<String, Object> brandMap = Maps.newHashMap();
                    brandMap.put("brandId", brand.getId());
                    brandMap.put("brandName", brand.getName());
                    ProductQuery productQuery = new ProductQuery();
                    productQuery.createCriteria().andProductBrandIdEqualTo(brand.getId()).andStateEqualTo(ProductStatus.EFFECTIVE.getCode()).andSellerIdEqualTo(sellerId);
                    List<Product> products = productMapper.selectByExample(productQuery);
                    List<Map<String, Object>> productMaps = Lists.newArrayList();
                    products.forEach(p -> {
                        Map<String, Object> productMap = Maps.newHashMap();
                        productMap.put("productId", p.getId());
                        productMap.put("productName", p.getName1());
                        productMap.put("productNormName", p.getNormName());
                        productMap.put("proLabel", p.getProLabel());
                        productMaps.add(productMap);
                    });
                    //添加商品集合
                    brandMap.put("products", productMaps);
                    //添加品牌map
                    brands.add(brandMap);
                }
            });
            //添加品牌集合
            cateMap.put("brands", brands);
            //添加分类map
            result.add(cateMap);
        }
        return result;
    }

    @Override
    public ListResultRes<Map<String, Object>> getOrderRepayments(Integer orderId, Integer indays, Integer state, Integer page, Integer pageSize) throws TouchCodeException {
        //获取订单用户
        OrdersBase ordersBase = ordersBaseService.getOrderById(orderId);
        if (ordersBase == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0011);
        }
        List<Integer> status = Lists.newArrayList(155, 160, 170, 180, 210, 220, 230);
        if (!status.contains(ordersBase.getOrderState())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0042);
        }
        if (ordersBase.getMemberId() == null) {
            log.error("商户获取订单[{}]还款计划失败：member_id为空", orderId);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0041, "：member_id为空，请联系开发人员", true);
        }
        //调用账户心痛获取用户身份证号
        MemberInfo memberInfo = memberCenterHandler.getMemberById(ordersBase.getMemberId());
        String memberIdCard = memberInfo.getIdcard();
        if (StringUtils.isBlank(memberIdCard)) {
            log.error("商户获取订单[{}]还款计划失败，请求用户中心：缺少用户身份证号", orderId);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004, "请求用户中心：缺少参数");
        }
        //调用账务系统查看还款计划
        Map<String, Object> repayments = billingService.repayments(ordersBase.getMemberId(), memberIdCard, indays, state, page, pageSize);
        if (repayments != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) repayments.get("data");
            Long totalCount = Long.valueOf((Integer) repayments.get("totalCount"));
            Integer currentPage = (Integer) repayments.get("currentPage");
            Integer ps = (Integer) repayments.get("pageSize");
            return ListResultRes.newListResult(list, totalCount, currentPage, ps);
        }
        return ListResultRes.newListResult(null, 0L, 0, 0);
    }

    @Override
    public ProductGoods getSkuByNormName(GetSkuByNormNameSearchReq search) {
        ProductGoodsQuery query = new ProductGoodsQuery();
        ProductGoodsQuery.Criteria criteria = query.createCriteria();
        criteria.andProductIdEqualTo(search.getProductId());
        criteria.andNormNameEqualTo(search.getNormName());
        List<ProductGoods> productGoods = productGoodsMapper.selectByExample(query);
        if (!CollectionUtils.isEmpty(productGoods)) {
            return productGoods.get(0);
        }
        return null;
    }

    @Override
    public JSONObject getProductCateAndSkuInfo(Integer sellerId,Integer productId) {
        Seller seller = sellerMapper.selectByPrimaryKey(sellerId);
        SellerStore sellerStore = sellerStoreManualMapper.selectBySellerId(sellerId);
        if (seller == null || sellerStore == null) {
            throw new RuntimeException("未查询到商户信息");
        }
        String storeType = sellerStore.getStoreType();
        if (StringUtils.isBlank(storeType)) {
            throw new RuntimeException("未查询到此店铺信息");
        }

        JSONObject json = new JSONObject();
        json.put("storeType", SellerStoreTypeEnum.getFrontMessage(storeType));
        Map<String, Object> productMap = productManualMapper.selectOnShelfInfoById(productId);
        if(productMap == null || productMap.isEmpty()){
            throw new RuntimeException("未查询到商品信息");
        }
        String state = ProductStatus.getEnum(Integer.valueOf(String.valueOf(productMap.get("state")))).getMessage();
        productMap.put("state", state);

        //属性配置
        String[] dicValues = null;
        Dictionary dictionary = dictionaryManualMapper.findByCode("CATE_ATTRIBUTE_CONFIGURE");
        if (dictionary != null) {
            String dicValue = dictionary.getDictVal();
            dicValues = dicValue.split(",");
        }

        List<Map<String, Object>> imageList = new ArrayList<>();
        String normName = String.valueOf(productMap.get("normName"));
        if(StringUtils.isNotBlank(normName)){
            imageList = productImageManualMapper.selectByProductId(productId);


            JSONArray normArray = JSONArray.parseArray(normName);

            for(int i = 0; i < normArray.size(); i++){
                JSONObject jsonObject = normArray.getJSONObject(i);
                Set<String> keySet = jsonObject.keySet();
                for(int j = 0; j < dicValues.length; j++){
                    String value = dicValues[j];
                    for(String key : keySet){
                        if(value.equals(key)){
                            JSONArray valueArray = jsonObject.getJSONArray(key);
                            List<String> list =JSONObject.parseArray(valueArray.toJSONString(), String.class);
                            value = value + ":" + list;
                        }
                    }
                    dicValues[j] = value;
                }
            }

        }
        json.put("dicValues", dicValues);
        json.put("imageList", imageList);
        json.putAll(productMap);
        return json;
        /*List<Map<String, Object>> result = Lists.newArrayList();
        //查询 商户下的品类
        SellerCateBrandRelQuery query = new SellerCateBrandRelQuery();
        query.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerCateBrandRel> lists = sellerCateBrandRelMapper.selectByExample(query);
        //封装
        Map<Integer, List<Integer>> groupMap = lists.stream()
                .collect(Collectors.groupingBy(SellerCateBrandRel::getCateId, Collectors.mapping(SellerCateBrandRel::getBrandId, Collectors.toList())));
        //查询品类下的属性
        String[] dicValues = null;
        Dictionary dictionary = dictionaryManualMapper.findByCode("CATE_ATTRIBUTE_CONFIGURE");
        if (dictionary != null) {
            String dicValue = dictionary.getDictVal();
            dicValues = dicValue.split(",");
        }
        for (Map.Entry<Integer, List<Integer>> entry : groupMap.entrySet()) {
            //商品分类-品类
            ProductCate cate = productCateService.getEntityById(entry.getKey());
            if (cate == null) {
                continue;
            }
            Map<String, Object> cateMap = Maps.newHashMap();
            cateMap.put("cateId", cate.getId());
            cateMap.put("cateName", cate.getName());
            //查询品类商品
            Product productParam = new Product();
            productParam.setSellerId(sellerId);
            productParam.setProductCateId(cate.getId());
            productParam.setState(ProductStatus.AUDITING_SUCCESS.getCode());//审核通过，待上架
            if (SellerStoreTypeEnum.STORE_TYPE_5.getCode().equals(storeType)) {//扫码店
                productParam.setIdentification(0);
            } else if (SellerStoreTypeEnum.STORE_TYPE_6.getCode().equals(storeType)) {//店中店
                productParam.setIdentification(1);
            }
            List<Map<String, Object>> productList = productManualMapper.selectByParam(productParam);
            for (Map<String, Object> product : productList){
                product.put("attributes", dicValues);
                String normName = String.valueOf(product.get("normName"));
                List<Map<String, Object>> imageList = new ArrayList<>();
                if(StringUtils.isNotBlank(normName)){//不为空 [{"颜色":["白色"]},{"尺码":["S","M"]},{"容量":["128G","64G"]}]
                    JSONArray normArray = JSONArray.parseArray(normName);
                    for(int i = 0; i < normArray.size(); i++){
                        JSONObject jsonObject = normArray.getJSONObject(i);
                        Set<String> keySet = jsonObject.keySet();
                        for(int j = 0; j < dicValues.length; j++){
                            String value = dicValues[j];
                            for(String key : keySet){
                                if(value.equals(key)){
                                    value = value + ":" + jsonObject.get(key);
                                }
                            }
                            dicValues[j] = value;
                        }

                    }

                    //查询商品图片信息
                    imageList = productImageManualMapper.selectByProductId(Integer.valueOf(String.valueOf(product.get("id"))));
                }
                product.put("imageList",imageList);
            }
            cateMap.put("products", productList);
            result.add(cateMap);*/

//        }


    }

}
