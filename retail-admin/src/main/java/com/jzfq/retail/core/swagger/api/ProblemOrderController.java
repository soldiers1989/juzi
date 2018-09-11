package com.jzfq.retail.core.swagger.api;

import com.jzfq.retail.bean.vo.crm.CRMSystemReq;
import com.jzfq.retail.bean.vo.res.ApiResponse;
import com.jzfq.retail.bean.vo.res.OrderStatisticsRes;
import com.jzfq.retail.bean.vo.res.ResponseModel;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import com.jzfq.retail.core.api.service.ProblemOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/problem")
public class ProblemOrderController {
    @Autowired
    private ProblemOrderService problemOrderService;
    @Autowired
    private OrdersBaseService ordersBaseService;

    @ApiOperation(value = "刘巍-根据条件（手机号、身份证、姓名）查询客户认证状态")
    @RequestMapping(value = "/queryCustomerAuth", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> queryCustomerAuth(@RequestParam("value") String value) {
        try {
            String result = problemOrderService.queryCustomerAuth(value);
            HashMap<String, Object> map = new HashMap<>();
            map.put("auth", result);

            return ApiResponse.success(map);
        } catch (Exception e) {
            log.error("根据条件（手机号、身份证、姓名）查询客户认证状态异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "刘巍-根据条件（手机号、订单号、姓名）查询订单状态")
    @RequestMapping(value = "/queryOrderBaseStatus", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> queryOrderBaseStatus(String phone, String orderSn, String memberName) {
        try {
            List<Map<String, Object>> result = ordersBaseService.findOrderStatusByQuery(phone, orderSn, memberName);
            return ApiResponse.success(result,"根据条件（手机号、订单号、姓名）查询订单状态成功");
        } catch (Exception e) {
            log.error("根据条件（手机号、订单号、姓名）查询订单状态异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    /*@ApiOperation(value = "刘学良-添加应用场景中出现的问题")
    @RequestMapping(value = "/saveProblem", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> saveProblem(@RequestBody CRMSystemReq info) {
        try {

            return ApiResponse.success("添加应用场景中出现的问题成功");
        } catch (Exception e) {
            log.error("添加应用场景中出现的问题异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }*/

    @ApiOperation(value = "统计未认证、认证审核成功、认证审核失败的数量及比例")
    @RequestMapping(value = "/statisticsAuthRate", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> statisticsAuthRate() {
        try {
            Map<String, Object> result = problemOrderService.getAuthenticationStateOfCountAndRatio();
            return ApiResponse.success(result,"统计未认证、认证审核成功、认证审核失败的数量及比例成功");
        } catch (Exception e) {
            log.error("统计未认证、认证审核成功、认证审核失败的数量及比例异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "李哲-统计认证失败又认证成功的数量及转化率")
    @RequestMapping(value = "/statisticsAuthFailSuccessRate", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> statisticsAuthFailSuccessRate() {
        try {
            Map<String, Object> result = problemOrderService.getMap1();
            return ApiResponse.success(result,"统计认证失败又认证成功的数量及转化率成功");
        } catch (Exception e) {
            log.error("统计认证失败又认证成功的数量及转化率异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "蔡世建-统计一天内进件笔数、客单价、总额")
    @RequestMapping(value = "/statisticsOrderNumPrice", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> statisticsOrderNumPrice() {
        try {
            OrderStatisticsRes res = problemOrderService.countUpOrderBase();
            return ApiResponse.success(res);
        } catch (Exception e) {
            log.error("统计一天内进件笔数、客单价、总额异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    /*@ApiOperation(value = "张建伟-记录用户使用的优惠券数量、价格")
    @RequestMapping(value = "/recordCustomerUseCouponNum", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> recordCustomerUseCouponNum(@RequestBody CRMSystemReq info) {
        try {

            return ApiResponse.success("记录用户使用的优惠券数量、价格成功");
        } catch (Exception e) {
            log.error("记录用户使用的优惠券数量、价格异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "张建伟-记录店员使用优惠券的数量、价格")
    @RequestMapping(value = "/recordSellerUseCouponNum", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> recordSellerUseCouponNum(@RequestBody CRMSystemReq info) {
        try {

            return ApiResponse.success("记录店员使用优惠券的数量、价格成功");
        } catch (Exception e) {
            log.error("记录店员使用优惠券的数量、价格异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }*/

    @ApiOperation(value = "刘学良-统计商户的订单数")
    @RequestMapping(value = "/statisticsSellerOrderNum", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> statisticsSellerOrderNum() {
        try {
            Integer entryNum = problemOrderService.countEntryOrder();
            return ApiResponse.success(entryNum);
        } catch (Exception e) {
            log.error("统计商户的订单数异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "张建伟-统计商户的返利")
    @RequestMapping(value = "/statisticsSellerRebate", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> statisticsSellerRebate() {
        try {
            List<Map<String,Object>> res = problemOrderService.statisticsSellerRebate();
            return ApiResponse.success(res, "统计商户的返利成功");
        } catch (Exception e) {
            log.error("统计商户的返利异常：{}", e.getMessage());
            return ApiResponse.failed(e.getMessage());
        }
    }
}
