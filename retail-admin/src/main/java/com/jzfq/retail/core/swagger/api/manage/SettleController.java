package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:57
 * @Description: 结算列表对外接口    ----同订单列表
 */
@Slf4j
@RestController
@RequestMapping("/api/settle")
public class SettleController {


    @Autowired
    private OrdersBaseService orderService;

    private ResponseEntity<TouchResponseModel> failResponse(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return TouchApiResponse.success("操作成功");
    }

    //100待确认；110用户已确认，待支付；120待支付；130支付成功，交易复核中；140交易复核通过，待交货；150交易复核失败；160已交货；170分期还款中；180已完成；200已取消；210退货处理中；220已退货；230退货失败
    private static final Integer ALL_LIST = 0;
    private static final Integer FINISHED = 180;


    private ResponseEntity<TouchResponseModel> getAllList(int status) {
        try {
            List<OrdersBase> result = null;
            OrdersBase ordersBase = new OrdersBase();
            if (status > 0) {
                ordersBase.setOrderState(status);
            }

            result = orderService.getAllList(ordersBase);
            return TouchApiResponse.success(result,"操作成功");
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取结算列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> settleList() {
        return getAllList(ALL_LIST);
    }

    @ApiOperation(value = "获取待结算列表")
    @RequestMapping(value = "/list?status=180", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> toSettleList() {
        return getAllList(FINISHED);
    }

    @ApiOperation(value = "获取待对账列表")
    @RequestMapping(value = "/list/bill", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> settleListBill() {
        return getAllList(ALL_LIST);
    }

    @ApiOperation(value = "获取待放款列表")
    @RequestMapping(value = "/list/loan", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> settleListLoan() {
        return getAllList(ALL_LIST);
    }


}
