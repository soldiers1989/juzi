package com.jzfq.retail.core.swagger.api.manage;


import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.Seller;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.OrdersSettleSearch;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.SellerSearchReq;
import com.jzfq.retail.bean.vo.req.WithdrawCashReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.common.util.ExcelUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.OrdersSettleService;
import com.jzfq.retail.core.api.service.SellerService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年06月29日 17:26
 * @Description: 商户结算
 */
@Slf4j
@RestController
public class SellerSettlementController {


    @Autowired
    private SellerService sellerService;

    @Autowired
    private OrdersSettleService ordersSettleService;

    @Autowired
    SessionManage sessionManage;

    @ApiOperation(value = "商户结算列表")
    @RequestMapping(value = "/api/seller/Settlement/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                   @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   OrdersSettleSearch search) {
        PageReq<OrdersSettleSearch> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<OrdersSettleRes> result = ordersSettleService.getList(pageReq.getPage(), pageReq.getPageSize(), search);
            return TouchApiResponse.success(result);
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("商户结算列表异常：{}", e.getMessage());
            return TouchApiResponse.failed("获取商户结算列表异常");
        }
    }

    @ApiOperation(value = "导出商户结算列表")
    @RequestMapping(value = "/seller/Settlement/export", method = RequestMethod.GET)
    public void export(OrdersSettleSearch search, HttpServletResponse response) {
        CommonValid.stringParamsTrim(search);
        CommonValid.emptyStringToNull(search);
        List<OrdersSettleRes> list = ordersSettleService.getListAll(search);
        String fileName = "结算列表";
        String[] columnNames = {"订单id", "订单编号", "下单时间", "结算状态 1、待结算 2、已结算 3、拒绝 4、未到结算日 10、待对账；20、待核账；30、待放款；40、已放款；50、取消结算", "商品名称", "商品规格", "订单商品数量", "优惠总金额", "订单总金额", "首付金额", "品类名称", "品牌名称", "买家名称", "账期", "商户名称","商家扣点","商户公司名称","开户行名称","银行卡号","订单备注","订单类型 1、扫码店订单，2、便利店订单","订单发货时间","确认收货时间","成本价","资方id","订单月供","实际结算时间","月利率","收货人","收货城市","期数","月服务费"};
        ExcelUtil.export(fileName, columnNames, list, response);
    }

    @ApiOperation(value = "扣点比例设置")
    @RequestMapping(value = "/api/seller/Settlement/setSpikeRatio", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> setSpikeRatio(Seller seller) {
        try {
            sellerService.create(seller);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "账户列表")
    @RequestMapping(value = "/api/seller/Settlement/accountList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> accountList(Integer page, Integer pageSize, String sellerName) {
        try {
            ListResultRes<OrdersSettleRes> result = ordersSettleService.getAccountList(page, pageSize, sellerName);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "提现")
    @RequestMapping(value = "/api/seller/Settlement/withdrawCash", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> withdrawCash(@RequestBody @Validated WithdrawCashReq withdrawCashReq) {
        try {
            String username = sessionManage.getUserName();
            ordersSettleService.withdrawCash(withdrawCashReq, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }



    @ApiOperation(value = "待对账")
    @RequestMapping(value = "/api/seller/Settlement/balanceAccount/{orderSn}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> balanceAccount(@PathVariable String orderSn) {
        try {
            ordersSettleService.balanceAccount(orderSn);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "待放款")
    @RequestMapping(value = "/api/seller/Settlement/pendingMoney/{orderSn}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> pendingMoney(@PathVariable String orderSn) {
        try {
            ordersSettleService.pendingMoney(orderSn);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }
}
