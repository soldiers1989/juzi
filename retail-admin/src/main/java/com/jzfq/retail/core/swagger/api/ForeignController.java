package com.jzfq.retail.core.swagger.api;


import com.jzfq.retail.bean.vo.crm.CRMSystemReq;
import com.jzfq.retail.bean.vo.req.CapitalBackReq;
import com.jzfq.retail.bean.vo.req.RepaymentCompleteReq;
import com.jzfq.retail.bean.vo.req.RiskCallbackReq;
import com.jzfq.retail.bean.vo.req.WithdrawCashCallbackReq;
import com.jzfq.retail.bean.vo.res.ApiResponse;
import com.jzfq.retail.bean.vo.res.CapitalBackRes;
import com.jzfq.retail.bean.vo.res.ResponseModel;
import com.jzfq.retail.core.api.service.ForeignCRMService;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import com.jzfq.retail.core.api.service.OrdersSettleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @Title: ForeignCRMController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月02日 10:26
 * @Description: CRM调用接口
 */
@Slf4j
@RestController
@RequestMapping("/foreign")
public class ForeignController {

    @Autowired
    private ForeignCRMService foreignCRMService;

    @Autowired
    private OrdersBaseService ordersBaseService;

    @Autowired
    private OrdersSettleService ordersSettleService;

    @ApiOperation(value = "CRM系统调用入住商户")
    @RequestMapping(value = "/seller/info", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> sellerInfo(@RequestBody CRMSystemReq info) {
        try {
            foreignCRMService.sellerInfo(info);
            return ApiResponse.success("入住成功");
        } catch (Exception e) {
            log.error("CRM系统调用入住商户异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "账务系统-还款完成回调", notes = "")
    @RequestMapping(value = "/order/repaymentComplete", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> repaymentComplete(@RequestBody RepaymentCompleteReq req) {
        try {
            log.info("账务系统-还款完成回调接口请求参数：{}", req);
            ordersBaseService.orderComplete(req.getOrderId(), req.getIsFinished());
            return ApiResponse.success("调用成功");
        } catch (Exception e) {
            log.error("账务系统-还款完成回调接口异常：{}", e.getMessage());
            return ApiResponse.success("调用失败");
        }
    }

    @ApiOperation(value = "风控订单审核回调", notes = "orderSN:订单编号,status:状态 140/150,remark:备注")
    @RequestMapping(value = "/order/riskCallback", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> riskCallback(@Valid @RequestBody RiskCallbackReq req) {
        try {
            log.info("风控订单审核回调：{}", req);
            ordersBaseService.riskCallback(req);
            return ApiResponse.success("调用成功");
        } catch (Exception e) {
            log.error("风控订单审核回调异常：{}", e.getMessage());
            return ApiResponse.success("风控订单审核回调失败");
        }
    }

    /**
     * 此接口用不到，推送资匹进件成功即可默认资匹成功
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "资匹匹配回调接口", notes = "capitalCode:资方代码,capitalOrderId:资方返回单号,contractNum:进件订单号,orderId:订单号")
    @RequestMapping(value = "/order/capitalBack", method = RequestMethod.POST)
    public Map<String, Object> capitalBack(@RequestBody CapitalBackReq req) {
        try {
            log.info("资匹匹配回调请求参数：{}", req);
            ordersBaseService.capitalBack(req);
            return CapitalBackRes.success("调用成功");
        } catch (Exception e) {
            log.error("账务系统-账单调用订单还款/风控订单审核回调异常：{}", e.getMessage());
            return CapitalBackRes.error("调用失败");
        }
    }

    /**
     * 商户账户提现回调
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "商户账户提现回调")
    @RequestMapping(value = "/merchantAccount/withdrawCashReturn", method = RequestMethod.POST)
    public Map<String, Object> withdrawCashReturn(@RequestBody WithdrawCashCallbackReq req) {
        try {
            log.info("商户账户提现回调请求参数：{}", req);
            ordersSettleService.withdrawCaseCallback(req);
            return CapitalBackRes.success("调用成功");
        } catch (Exception e) {
            log.error("商户账户提现回调异常：{}", e.getMessage());
            return CapitalBackRes.error("调用失败");
        }
    }



}
