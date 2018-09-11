package com.jzfq.retail.core.swagger.api;


import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.vo.req.OrdersBaseReq;
import com.jzfq.retail.bean.vo.res.ApiResponse;
import com.jzfq.retail.bean.vo.res.ResponseModel;
import com.jzfq.retail.common.exception.BadRequestException;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Title: BDController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年07月25日 13:40
 * @Description: 补单专用调用接口
 */
@Slf4j
@RestController
@RequestMapping("/bd")
public class BDController {

    @Autowired
    private OrdersBaseService ordersBaseService;

    @ApiOperation(value = "查询订单信息", notes = "查询订单信息")
    @RequestMapping(value = "/orderBase/getById", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> repaymentEnd(Integer id) {
        if(id == null){
            throw new BadRequestException("id不能为空");
        }
        OrdersBase orderById = ordersBaseService.getOrderById(id);
        return ApiResponse.success(orderById,"操作成功");
    }

    @ApiOperation(value = "修改订单信息", notes = "修改订单信息")
    @RequestMapping(value = "/orderBase", method = RequestMethod.PUT)
    public ResponseEntity<ResponseModel> repaymentEnd(@Valid @RequestBody OrdersBaseReq req) {
        if(req == null){
            throw new BadRequestException("参数不能为空");
        }
        if(req.getId() == null){
            throw new BadRequestException("id不能为空");
        }
        OrdersBase ordersBase = ordersBaseService.getOrderById(req.getId());
        TransferUtil.transferIgnoreNull(req, ordersBase);
        ordersBaseService.update(ordersBase);
        OrdersBase orderById = ordersBaseService.getOrderById(req.getId());
        return ApiResponse.success(orderById,"操作成功");
    }
}
