package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.vo.res.ApiResponse;
import com.jzfq.retail.bean.vo.res.ResponseModel;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.core.api.service.OrderRiskService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Title: OrderRiskController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月08日 14:13
 * @Description: 订单风控进件补单
 */
@Slf4j
@RestController
@RequestMapping("/order/risk")
public class OrderRiskController {

    @Autowired
    private OrderRiskService orderRiskService;

    @ApiOperation(value = "订单风控进件补单")
    @GetMapping(value = "/supplement/riskReceive/{orderSn}")
    public ResponseEntity<ResponseModel> supplementRiskReceive(@PathVariable("orderSn") String orderSn) {
        try {
            orderRiskService.supplementRiskReceive(orderSn);
            return ApiResponse.success("操作生效，请查看订单状态");
        } catch (Exception e) {
            log.error("订单{}风控进件补单异常：{}", orderSn, e.getMessage());
            return ApiResponse.failed("订单" + orderSn + "风控进件补单异常：" + e.getMessage());
        }
    }

    @ApiOperation(value = "订单风控准进补单")
    @RequestMapping(value = "/supplement/riskCheck", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> supplementRiskCheck(@RequestParam("orderSn") String orderSn, @RequestParam("isSingleCredit") Integer isSingleCredit) {
        try {
            orderRiskService.supplementRiskCheck(orderSn, isSingleCredit);
            return ApiResponse.success("操作成功");
        } catch (Exception e) {
            log.error("订单{}风控准进补单异常：{}", orderSn, e.getMessage());
            return ApiResponse.failed("订单" + orderSn + "风控准进补单异常：" + e.getMessage());
        }
    }
}
