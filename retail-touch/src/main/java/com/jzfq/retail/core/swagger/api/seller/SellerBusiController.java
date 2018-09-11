package com.jzfq.retail.core.swagger.api.seller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jzfq.retail.bean.domain.ProductGoods;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.OrderDeliveryInfoRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ProductService;
import com.jzfq.retail.core.api.service.SellerBusiService;
import com.jzfq.retail.core.api.service.SellerService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * @Title: SellerBusiController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年06月29日 18:25
 * @Description: 小程序商户端业务接口
 */
@Slf4j
@RestController
@RequestMapping("/seller")
public class SellerBusiController {

    // 查询商户信息
    private static final String FIND_SELLER_INFO = "/findSellerInfo";
    // 查询品类品牌
    private static final String FIND_CATE_BRAND_INFO = "/findCateBrandInfo/{sellerId}";
    // 创建订单
    private static final String CREATE_ORDER = "/createOrder";
    //通过订单编号获取订单简单信息
    private static final String GET_SIMPLE_ORDER_INFO = "/getSimpleOrderInfo/{orderSn}";
    // 获取订单二维码
    private static final String GET_ORDER_QRCODE = "/getOrderQRCode/{orderSn}";
    // 订单列表
    private static final String ORDER_LIST = "/orderList";
    // 订单详情
    private static final String ORDER_INFO = "/orderInfo/{orderId}";

    // 店铺商品列表
    private static final String SELLER_GOODS_LIST = "/sellerGoodsList";
    //判断商户是否冻结
    private static final String GET_SELLER_IS_FROZEN = "/getSellerIsFrozen/{sellerId}";
    //获取订单还款计划
    private static final String SELLER_GET_ORDER_REPAYMENTS = "/getOrderRepayments";
    // 通过norm_name拼串来查询SKU
    private static final String GET_SKU_BY_NORM_NAME = "/getSkuByNormName";
    //商户店铺装修
    private static final String SELLER_SHOP_DECORATION = "/addSellerShopDecoration";
    //商品上架时查询商品品类 商品 属性信息
    private static final String PRODUCT_CATE_SKU_INFO = "/getProductCateAndSkuInfo/{sellerId}/{productId}";
    //商品上架
    private static final String PRODUCT_ON_SHELF = "/onshelf";
    //订单交货详情
    private static final String GET_ORDER_DELIVERY_INFO = "/getOrderDeliveryInfo/{orderId}";
    // 订单交货确认
    private static final String ORDER_DELIVERY_COMMIT = "/orderDeliveryCommit";

    @Autowired
    private SessionManage sessionManage;

    @Autowired
    private SellerBusiService sellerBusiService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ProductService productService;

    /**
     * 登录成功后 小程序调用后台接口 查询商户的信息。传递参数：token，后台通过token获取当前登录用户的商户信息。
     *
     * @return
     */
    @ApiOperation(value = "查询商户信息")
    @RequestMapping(value = FIND_SELLER_INFO, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> findSellerInfo() {
        try {
            //1、通过token获取商户编码
            String sellerLogin = sessionManage.getSellerLogin();
            //2、通过商户登录用的编码查询商户信息
            Map<String, Object> resultMap = sellerBusiService.findSellerInfo(sellerLogin);
            return TouchApiResponse.success(resultMap);
        } catch (TouchCodeException te) {
            log.error("查询商户信息:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("查询商户信息:{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }


    /**
     * 进入到创建订单页面，小程序传递token，根据token获取当前商户有权经营的品类、品牌、名称，封装大对象传给小程序。
     *
     * @param sellerId
     * @return
     */
    @ApiOperation(value = "查询品类品牌")
    @GetMapping(FIND_CATE_BRAND_INFO)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> findCateBrandInfo(@PathVariable("sellerId") Integer sellerId) {
        try {
            List<Map<String, Object>> result = sellerBusiService.findCateBrandInfo(sellerId);
            return TouchApiResponse.success(result);
        } catch (TouchCodeException te) {
            log.error("查询品类品信息:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("小程序商户端-查询品类品牌:{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    /**
     * 小程序将之前选择的信息传递后台，创建订单，返回创建成功失败消息，如果成功，则返回订单号。
     * result 1  成功，0 失败 { result:0, data:'key:value', errorCode:'', message:'' }
     */
    @ApiOperation(value = "创建订单")
    @RequestMapping(value = CREATE_ORDER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> createOrder(@Validated @RequestBody CreateOrderReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            //1、根据传递的消息从创建订单 orders_base，orders_product
            String orderSn = sellerBusiService.createOrder(req);
            Map<String, Object> map = Maps.newHashMap();
            map.put("orderSn", orderSn);
            return TouchApiResponse.success(map, "订单创建成功");
        } catch (TouchCodeException te) {
            log.error("查询商创建订单户信息:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("查询商创建订单户信息:{}", e.getMessage());
            return TouchApiResponse.failed("订单创建失败：" + e.getMessage());
        }
    }

    /**
     * 小程序请求生成二维码接口传递订单号，后台将之前选择的品类、品牌、名称等字段、二维码连接传递给小程序。
     * 重新生成二维码接口 与此接口是同一个接口。
     *
     * @param orderSn 订单编号
     * @return
     */
    @ApiOperation(value = "通过订单编号获取简单订单信息")
    @GetMapping(value = GET_SIMPLE_ORDER_INFO, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> getSimpleOrderInfo(@PathVariable("orderSn") String orderSn) {
        CommonValid.stringTrim(orderSn);
        try {
            Map<String, Object> map = sellerBusiService.getSimpleOrderInfoByOrderSn(orderSn);
            return TouchApiResponse.success(map, "获取信息成功");
        } catch (TouchCodeException te) {
            log.error("获取简单订单信息异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("获取简单订单信息异常:{}", e.getMessage());
            return TouchApiResponse.failed("获取简单订单信息异常：" + e.getMessage());
        }
    }

    /**
     * 通过订单编号获取订单二维码
     *
     * @param orderSn
     * @return 二维码订单地址
     */
    @ApiOperation(value = "获取订单二维码")
    @GetMapping(value = GET_ORDER_QRCODE, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> generateQRCode(@PathVariable("orderSn") String orderSn) {
        CommonValid.stringTrim(orderSn);
        try {
            String url = sellerBusiService.getOrderQRCode(orderSn);
            return TouchApiResponse.success(url, "获取订单二维码成功");
        } catch (TouchCodeException te) {
            log.error("获取订单二维码失败:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("获取订单二维码失败:{}", e.getMessage());
            return TouchApiResponse.failed("获取订单二维码失败：" + e.getMessage());
        }
    }

    /**
     * 小程序传递token，后台获取全部订单
     * 传递参数：
     * token 自动
     * type: 1全部，2待支付， 3待交货
     * page：传当前页数
     * 每页条数，后台固定
     * <p>
     * 返回：
     * data：
     * page：当前页
     * totalPage：总页数
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "订单列表")
    @RequestMapping(value = ORDER_LIST, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> orderList(@RequestBody PageReq<OrderSearchReq> req) {
        CommonValid.stringParamsTrim(req);
        try {
            //1、通过token获取商户编码
            String sellerLogin = sessionManage.getSellerLogin();
            //2、查询商户下所有订单
            ListResultRes<Map<String, Object>> result = sellerBusiService.findOrderListBySeller(req.getPage(), req.getPageSize(), req.getSearch(), sellerLogin);
            return TouchApiResponse.success(result, "查询订单列表成功");
        } catch (TouchCodeException te) {
            log.error("订单列表:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("订单列表:{}", e.getMessage());
            return TouchApiResponse.failed("询订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "订单详情")
    @GetMapping(value = ORDER_INFO, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> orderInfo(@PathVariable("orderId") Integer orderId) {
        try {
            Map<String, Object> info = sellerBusiService.findOderInfoById(orderId);
            return TouchApiResponse.success(info, "查询订单详情成功");
        } catch (TouchCodeException te) {
            log.error("订单详情:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("订单详情:{}", e.getMessage());
            return TouchApiResponse.failed("查询订单详情失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "订单交货详情")
    @GetMapping(value = GET_ORDER_DELIVERY_INFO, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> getOrderDeliveryInfo(@PathVariable("orderId") Integer orderId) {
        try {
            OrderDeliveryInfoRes res = sellerBusiService.getOrderDeliveryInfo(orderId);
            return TouchApiResponse.success(res, "获取订单交货详情成功");
        } catch (TouchCodeException te) {
            log.error("获取订单交货详情异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("获取订单交货详情异常:{}", e.getMessage());
            return TouchApiResponse.failed("获取订单交货详情失败");
        }
    }

    @ApiOperation(value = "订单交货确认")
    @RequestMapping(value = ORDER_DELIVERY_COMMIT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> orderDeliveryCommit(@RequestBody OrderDeliveryCommitReq req) {
        try {
            sellerBusiService.orderDeliveryCommit(req);
            return TouchApiResponse.success("订单交货成功");
        } catch (TouchCodeException te) {
            log.error("交货接:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("交货接:{}", e.getMessage());
            return TouchApiResponse.failed("订单交货失败：" + e.getMessage());
        }
    }

    /**
     * 只需传递token， 查询店铺所有商品列表。
     * page：1
     * token
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "店铺商品列表")
    @RequestMapping(value = SELLER_GOODS_LIST, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> sellerGoodsList(@RequestBody @Validated PageReq<SellerGoodsSearch> req) {
        try {
            ListResultRes<Map<String, Object>> result = sellerBusiService.findSellerProductList(req.getPage(), req.getPageSize(), req.getSearch());
            return TouchApiResponse.success(result, "查询店铺商品列表成功");
        } catch (TouchCodeException te) {
            log.error("店铺商品列表:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("店铺商品列表:{}", e.getMessage());
            return TouchApiResponse.failed("查询店铺商品列表失败：" + e.getMessage());
        }
    }

    /**
     * 判断商户是否冻结
     *
     * @return
     */
    @ApiOperation(value = "判断商户是否冻结")
    @GetMapping(value = GET_SELLER_IS_FROZEN, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> getSellerIsFrozen(@PathVariable("sellerId") Integer sellerId) {
        try {
            boolean result = sellerService.sellerIsFrozen(sellerId);
            return TouchApiResponse.success(result, "获取商是否冻结成功");
        } catch (TouchCodeException te) {
            log.error("获取商是否冻结异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("获取商是否冻结异常:{}", e.getMessage());
            return TouchApiResponse.failed("获取商是否冻结异常：" + e.getMessage());
        }
    }

    /**
     * 获取订单还款计划
     *
     * @param pageReq 订单id
     * @return
     */
    @ApiOperation(value = "获取订单还款计划")
    @PostMapping(value = SELLER_GET_ORDER_REPAYMENTS, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> getOrderRepayments(@RequestBody @Validated PageReq<GetOrderRepaymentsReq> pageReq) {
        try {
            GetOrderRepaymentsReq req = pageReq.getSearch();
            ListResultRes<Map<String, Object>> result = sellerBusiService.getOrderRepayments(req.getOrderId(), req.getIndays(), req.getState(), pageReq.getPage(), pageReq.getPageSize());
            return TouchApiResponse.success(result, "获取订单还款详情成功");
        } catch (TouchCodeException te) {
            log.error("商户获取订单还款详情异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("商户获取订单还款详情异常:{}", e.getMessage());
            return TouchApiResponse.failed("商户获取订单还款详情异常：" + e.getMessage());
        }
    }


    @ApiOperation(value = "通过规格属性查询SKU", notes = "例如：颜色,黑;容量,64G")
    @RequestMapping(value = GET_SKU_BY_NORM_NAME, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> getSkuByNormName(@RequestBody @Validated GetSkuByNormNameSearchReq search) {
        CommonValid.stringParamsTrim(search);
        try {
            ProductGoods result = sellerBusiService.getSkuByNormName(search);
            return TouchApiResponse.success(result, "通过规格属性查询SKU成功");
        } catch (TouchCodeException te) {
            log.error("通过规格属性查询SKU异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("通过规格属性查询SKU异常:{}", e.getMessage());
            return TouchApiResponse.failed("通过规格属性查询SKU失败");
        }
    }

    @ApiOperation("保存店铺装修的图片信息")
    @RequestMapping(value = SELLER_SHOP_DECORATION, method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> addSellerImage(Integer sellerId,
                                                             String[] urlList,
                                                             String proName) {
        try {
            sellerService.addSellerImage(sellerId, urlList, proName);
            return TouchApiResponse.success("商铺装修成功");
        } catch (TouchCodeException te) {
            log.error("请求返回数据异常：{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation("商品上架时查询商品品类 商品 属性信息")
    @RequestMapping(value = PRODUCT_CATE_SKU_INFO, method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> getProductCateAndSkuInfo(@PathVariable("sellerId") Integer sellerId,
                                                                       @PathVariable("productId") Integer productId) {
        try {
            JSONObject result = sellerBusiService.getProductCateAndSkuInfo(sellerId, productId);
            return TouchApiResponse.success(result);
        } catch (TouchCodeException te) {
            log.info("商品上架时查询商品品类商品属性信息:{}, {}", sellerId, te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.info("商品上架时查询商品品类商品属性信息:{}, {}", sellerId, e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    /**
     * 商品上架
     * @param productOnShelfReq
     * @return
     */
    @ApiOperation(value = "商户商品上架", notes = "attrParams=颜色:白色,黑色,红色|尺码:S,M,L,XL|容量:128G,64G,32G")
    @RequestMapping(value = PRODUCT_ON_SHELF, method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> productOnShelf(@RequestBody @Validated ProductOnShelfReq productOnShelfReq){
        try {
            Integer result = productService.productOnShelf(productOnShelfReq);
            if(result == 1){
                return TouchApiResponse.success("操作成功");
            } else {
                return TouchApiResponse.failed("商品上架失败");
            }
        } catch (TouchCodeException te) {
//            log.info("商户商品上架：{},{},{}", sellerId, productId, te.getTouchApiCode().getMsg());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
//            log.info("商户商品上架：{},{},{}", sellerId, productId, e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }
}
