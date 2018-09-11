package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.domain.OrdersBaseRelatedInfo;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import io.netty.util.internal.ObjectUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:57
 * @Description: 订单表对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrdersBaseController {


    @Autowired
    private OrdersBaseService orderService;

    private OrdersBase transReq(OrdersBaseReq req) {
        if (req == null) {
            throw new RuntimeException("接口传入的对象为空。");
        }

        OrdersBase ordersBase = new OrdersBase();
        BeanUtils.copyProperties(req, ordersBase);
        return ordersBase;
    }

    @ApiOperation(value = "获取订单全部数据-无分页")
    @RequestMapping(value = "/list/all", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> allList(@RequestBody OrdersBaseReq search) {
        CommonValid.emptyStringToNull(search);
        try {
            OrdersBase ordersBase = transReq(search);
            List<OrdersBase> result = orderService.getAllList(ordersBase);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return failResponse(e);
        }
    }

    private ResponseEntity<TouchResponseModel> failResponse(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return TouchApiResponse.failed(e.getMessage());
    }

    //100待确认；110用户已确认，待支付；120待支付；130支付成功，交易复核中；140交易复核通过，待交货；150交易复核失败；160已交货；170分期还款中；180已完成；200已取消；210退货处理中；220已退货；230退货失败
    private static final Integer TO_PAY = 120;
    private static final Integer IN_CHECKING = 130;
    private static final Integer CHECKED_TO_SHIP = 140;
    private static final Integer SHIPPED = 160;
    private static final Integer IN_TERM = 170;
    private static final Integer FINISHED = 180;
    private static final Integer CANCELLED = 200;


    private ResponseEntity<TouchResponseModel> getAllList(int status) {
        try {
            OrdersBase ordersBase = new OrdersBase();
            ordersBase.setOrderState(status);
            List<OrdersBase> result = orderService.getAllList(ordersBase);
            return TouchApiResponse.success(result,"操作成功");
        } catch (Exception e) {
            return failResponse(e);
        }
    }

    @ApiOperation(value = "获取订单全部数据-无分页-待支付")
    @RequestMapping(value = "/topay", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> toPayList() {
        return getAllList(TO_PAY);
    }

    @ApiOperation(value = "获取订单全部数据-无分页-审核中")
    @RequestMapping(value = "/auditing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> inCheckingList() {
        return getAllList(IN_CHECKING);
    }

    @ApiOperation(value = "获取订单全部数据-无分页-待发货")
    @RequestMapping(value = "/tosend", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> toShipList() {
        return getAllList(CHECKED_TO_SHIP);
    }

    @ApiOperation(value = "获取订单全部数据-无分页-已发货")
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> shippedList() {
        return getAllList(SHIPPED);
    }

    @ApiOperation(value = "获取订单全部数据-无分页-已完成")
    @RequestMapping(value = "/already", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> finishedList() {
        return getAllList(FINISHED);
    }

    @ApiOperation(value = "获取订单全部数据-无分页-已取消")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> cancelledList() {
        return getAllList(CANCELLED);
    }

    @ApiOperation(value = "获取订单列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                   @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   OrderListReq search) {
        PageReq<OrderListReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes result = orderService.getOrderBaseRelatedInfoList(pageReq.getPage(), pageReq.getPageSize(), pageReq.getSearch());
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return failResponse(e);
        }
    }

    @ApiOperation(value = "订单添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@Valid @RequestBody OrdersBaseReq req) {
        try {
            orderService.create(transReq(req));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return failResponse(e);
        }
    }

    @ApiOperation(value = "订单数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@Valid @RequestBody OrdersBaseReq req) {
        try {
            orderService.update(transReq(req));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return failResponse(e);
        }
    }

    @ApiOperation(value = "订单数据删除")
    @GetMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            OrdersBase ordersBase = orderService.getOrderById(id);
            orderService.delete(id);
            return TouchApiResponse.success(ordersBase,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return failResponse(e);
        }
    }

    @ApiOperation(value = "通过ID获取订单数据")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getOrderById(@PathVariable("id") Integer id) {
        try {
            OrdersBase ordersBase = orderService.getOrderById(id);
            return TouchApiResponse.success(ordersBase,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return failResponse(e);
        }
    }

}
