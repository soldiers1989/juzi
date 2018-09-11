package com.jzfq.retail.core.swagger.api.shop;


import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ShopScanService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @Title: ShopScanController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年08月2日 18:25
 * @Description: 小程序商城端II期店中店接口
 */
@Slf4j
@RestController
@RequestMapping("/shop/scan")
public class ShopScanController {

    private static final String QUERYPRODUCTINFO = "/queryProductInfo"; // 查询商品详情
    private static final String CHECKGOODSSTOCK = "/checkGoodsStock"; // 校验商品库存
    private static final String CHECKREGISTER = "/checkRegister"; // 校验用户是否认证注册
    private static final String GENERATEORDER = "/generateOrder"; // 生成订单
    private static final String WX_REPAY = "/wxRepay"; // 微信小程序还款


    @Autowired
    private ShopScanService shopScanService;


    /**
     * 小程序扫码传递商户id和商品库存id
     * 查询商品详情
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "扫码查询商品详情接口")
    @RequestMapping(value = QUERYPRODUCTINFO, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> queryProductInfo(@RequestBody @Validated ScanProductReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            Map<String, Object> map = shopScanService.queryProductInfo(req);
            return TouchApiResponse.success(map, "扫码查询商品详情成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("扫码查询商品详情接口:{}", e.getMessage());
            return TouchApiResponse.failed("扫码查询商品详情异常");
        }
    }

    /**
     * 小程序扫码传递商品库存id,校验是否有库存
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "校验商品库存接口")
    @RequestMapping(value = CHECKGOODSSTOCK, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> checkGoodsStock(@RequestBody @Validated GoodsStockReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            Map<String, Object> map = shopScanService.checkGoodsStock(req);
            return TouchApiResponse.success(map, "校验商品库存成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("校验商品库存接口:{}", e.getMessage());
            return TouchApiResponse.failed("校验商品库存异常");
        }
    }

    /**
     * 小程序扫码，校验用户是否注册认证
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "校验用户是否注册认证接口")
    @RequestMapping(value = CHECKREGISTER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> checkRegister(@RequestBody @Validated CheckRegisterReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            shopScanService.checkRegister(req);
            return TouchApiResponse.success(TouchApiCode.TOUCH_API_CODE_0008.getMsg());
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("调用后台查询是否注册接口:{}", e.getMessage());
            return TouchApiResponse.failed("调用后台查询是否注册异常");
        }
    }

    /**
     * 小程序扫码，校验用户是否注册认证
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "生成订单")
    @RequestMapping(value = GENERATEORDER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> generateOrder(@RequestBody @Validated GenerateOrderReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            Map<String, Object> map = shopScanService.generateOrder(req);
            return TouchApiResponse.success(map, TouchApiCode.TOUCH_API_CODE_0059.getMsg());
        } catch (TouchCodeException te) {
            log.error("二期生成订单请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("二期生成订单请求返回异常:{}", e.getMessage());
            return TouchApiResponse.failed("生成订单请求返回异常");
        }
    }

    /**
     * 小程序扫码，校验用户是否注册认证
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "微信小程序还款支付")
    @PostMapping(value = WX_REPAY)
    public ResponseEntity<TouchResponseModel> wxRepay(@RequestBody @Validated WXRepayReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            String result = shopScanService.wxRepaymentPay(req.getWxCode(), req.getOrderSn(), req.getPeriod());
            return TouchApiResponse.success(result);
        } catch (TouchCodeException te) {
            log.error("微信小程序还款支付请求异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("微信小程序还款支付请求接口异常:{}", e.getMessage());
            return TouchApiResponse.failed("调用微信小程序还款支付异常");
        }
    }


}
