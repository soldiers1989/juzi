package com.jzfq.retail.core.swagger.api.shop;


import com.google.common.collect.Lists;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerBusiService;
import com.jzfq.retail.core.api.service.ShopBusiService;
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
 * @Title: ShopBusiController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年06月29日 18:25
 * @Description: 小程序商城端业务接口
 */
@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopBusiController {

    private static final String CHECK_REGISTERED = "/checkRegistered"; // 调用后台查询是否注册接口
    private static final String INTO_CONFIRM_ORDER = "/intoConfirmOrder"; // 进入确认订单接口
    private static final String COMMIT_ORDER = "/commitOrder"; // 点击提交订单接口
    private static final String INTO_ORDER_PAY = "/intoOrderPay"; // 进入信用支付接口
    private static final String COMMIT_PAY = "/commitPay"; // 输入交易密码后提交接口
    private static final String ORDER_INFO = "/orderInfo/{orderId}"; // 订单详情接口
    private static final String AUTH_SUC_BACK = "/authSucback/{wxCode}"; // 认证成功后回调接口
    private static final String ORDER_LIST = "/orderList";// 订单列表

    @Autowired
    private ShopBusiService shopBusiService;

    @Autowired
    private SellerBusiService sellerBusiService;

    /**
     * 认证调用核心接口。在是否注册接口中返回
     * 商城端小程序传递 微信code，后台调用用户中心判断是否注册，是否绑定。
     * 注册、登录（绑定）、认证
     * 第一步，如果未注册，返回未注册；如果已注册未登录，返回未登录，以此类推验证到最后。
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "调用后台查询是否注册接口")
    @RequestMapping(value = CHECK_REGISTERED, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> checkRegistered(@RequestBody @Validated CheckRegisterReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            shopBusiService.checkRegister(req);
            return TouchApiResponse.success(TouchApiCode.TOUCH_API_CODE_0008.getMsg());
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("调用后台查询是否注册接口:{}", e.getMessage());
            return TouchApiResponse.failed("调用后台查询是否注册失败：" + e.getMessage());
        }
    }

    /**
     * 小程序扫码后，解析二维码之后传递订单号到后台，后台查询订单信息给到小程序，同时返回分期数，和分期还款计划详情
     */
    @ApiOperation(value = "进入确认订单接口")
    @RequestMapping(value = INTO_CONFIRM_ORDER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> intoConfirmOrder(@RequestBody @Validated IntoConfirmOrderReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            Map<String, Object> map = shopBusiService.intoConfirmOrder(req);
            return TouchApiResponse.success(map, "进入确认订单接口成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("进入确认订单接口:{}", e.getMessage());
            return TouchApiResponse.failed("进入确认订单接口失败：" + e.getMessage());
        }
    }

    //小程序会传递相关的信息（期数、订单号）给到后台，记录。返回成功失败结果。
    @ApiOperation(value = "点击提交订单接口")
    @RequestMapping(value = COMMIT_ORDER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> commitOrder(@RequestBody @Validated CommitOrderReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            shopBusiService.commitOrder(req);
            return TouchApiResponse.success("进入确认订单接口成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("点击提交订单接口:{}", e.getMessage());
            return TouchApiResponse.failed("点击提交订单接口失败：" + e.getMessage());
        }
    }

    //小程序传递订单号，后台查询订单的相关信息及是否绑卡和是否已设置交易密码的信息，返回小程序。
    @ApiOperation(value = "进入信用支付接口")
    @RequestMapping(value = INTO_ORDER_PAY, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> intoOrderPay(@RequestBody @Validated IntoOrderPayReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            Map<String, Object> map = shopBusiService.intoOrderPay(req.getOrderSn(), req.getLng(), req.getLat());
            return TouchApiResponse.success(map, "进入信用支付接口成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            log.error("进入信用支付接口:{}", e.getMessage());
            return TouchApiResponse.failed("进入信用支付接口失败：" + e.getMessage());
        }
    }

    //小程序自动跳转到输入交易密码界面，输入密码后提交到后台，后台验证密码是否正确，
    //如果正确，后台扣减信用额度，扣减成功后，返回小程序成功消息，返回订单号；否则返回密码错误消息给小程序。
    @ApiOperation(value = "输入交易密码后提交接口")
    @RequestMapping(value = COMMIT_PAY, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> commitPay(@RequestBody @Validated CommitPayReq req) {
        CommonValid.stringParamsTrim(req);
        try {
            shopBusiService.commitPay(req);
            return TouchApiResponse.success("输入交易密码后提交接口成功");
        } catch (TouchCodeException te) {
            te.printStackTrace();
            log.error("输入交易密码后提交接口:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("输入交易密码后提交接口:{}", e.getMessage());
            return TouchApiResponse.failed("信用支付失败：" + e.getMessage());
        }
    }

    //小程序传递订单号查询订单信息。
    @ApiOperation(value = "订单详情接口")
    @RequestMapping(value = ORDER_INFO, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> orderInfo(@PathVariable("orderId") Integer orderId) {
        try {
            Map<String, Object> orderBase = sellerBusiService.findOderInfoById(orderId);
            return TouchApiResponse.success(orderBase, "查询订单详情成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("订单[{}]查看详情详情异常:{}", orderId, e.getMessage());
            return TouchApiResponse.failed("查询订单详情失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "认证成功后回调接口")
    @RequestMapping(value = AUTH_SUC_BACK, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> authSucBack(@PathVariable("wxCode") String wxCode) {
        CommonValid.stringTrim(wxCode);
        try {
            shopBusiService.authSucBack(wxCode);
            return TouchApiResponse.success("认证成功后回调成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("认证成功后回调失败:{}", wxCode);
            return TouchApiResponse.failed("认证成功后回调失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "订单列表")
    @RequestMapping(value = ORDER_LIST, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> orderList(@RequestBody @Validated ShopOrderListReq req) {
        try {
            Integer[] status = null;
            switch (req.getOrderState()) {
                case 0:
                    status = new Integer[]{110, 130, 135, 140, 150, 155, 160, 170, 180, 200, 210, 220, 230};
                    break;
                case 1:
                    status = new Integer[]{110};
                    break;
                case 2:
                    status = new Integer[]{155};
                    break;
                case 3:
                    status = new Integer[]{160};
                    break;
            }
            ListResultRes<Map<String, Object>> resultRes = shopBusiService.findOrderListByWxcode(req.getPage(), req.getPageSize(), status, req.getWxcode());
            return TouchApiResponse.success(resultRes, "查询订单列表成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("订单列表:{}", e.getMessage());
            return TouchApiResponse.failed("询订单列表失败：" + e.getMessage());
        }
    }
}
