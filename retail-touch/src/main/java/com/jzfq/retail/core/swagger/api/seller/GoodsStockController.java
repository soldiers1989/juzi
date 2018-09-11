package com.jzfq.retail.core.swagger.api.seller;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.gson.JsonObject;
import com.jzfq.retail.bean.vo.req.PickUpGoodsReq;
import com.jzfq.retail.bean.vo.req.TakeCodeReq;
import com.jzfq.retail.bean.vo.res.TakeCodeOrderRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.FileUtil;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.GoodsStockService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: GoodsStockController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月03日 10:10
 * @Description: 小程序端商品存货管理
 */
@Slf4j
@RestController
@RequestMapping("/goodsStock")
public class GoodsStockController {

    @Autowired
    private GoodsStockService goodsStockService;

    /**
     * 根据验证码获取订单信息
     * @return
     */
    @ApiOperation(value = "根据取货码查询订单")
    @RequestMapping(value = "/queryOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> queryOrder(@Valid @RequestBody TakeCodeReq takeCodeReq) {
        log.info("根据取货码查询订单请求处理开始，取货码：{}", takeCodeReq.getTakeCode());
        ResponseEntity<TouchResponseModel> respModel = null;
        try {
            TakeCodeOrderRes takeCodeOrderRes = goodsStockService.getOrderInfo(takeCodeReq.getTakeCode(),takeCodeReq.getSellerId());
            respModel = TouchApiResponse.success(takeCodeOrderRes);
        } catch (TouchCodeException te) {
            log.error("根据取货码查询订单请求处理异常，异常描述:{}", te);
            respModel = TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("根据取货码查询订单请求处理异常，异常描述:{}", e);
            respModel = TouchApiResponse.failed(e.getMessage());
        }
        log.info("根据取货码查询订单请求处理结束，响应参数：{}", JsonMapper.nonDefaultMapper().toJson(respModel));
        return respModel;
    }

    @ApiOperation(value = "根据序列号取货")
    @RequestMapping(value = "/pickUpGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<TouchResponseModel> pickUpGoods(@Valid @RequestBody PickUpGoodsReq pickUpGoodsReq) {
        log.info("根据序列号取货请求处理开始，序列号：{}", 1,pickUpGoodsReq.getSeqNum());
        ResponseEntity<TouchResponseModel> respModel = null;
        try {
            // 解析序列号
            goodsStockService.pickUpGoods(pickUpGoodsReq.getSeqNum(),pickUpGoodsReq.getOrderSn());
            respModel = TouchApiResponse.success("取货成功！");
        } catch (TouchCodeException te) {
            log.error("根据序列号取货请求处理异常，异常描述:{}", te);
            respModel = TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg());
        } catch (Exception e) {
            log.error("根据序列号取货请求处理异常，异常描述:{}", e);
            respModel = TouchApiResponse.failed(e.getMessage());
        }
        log.info("根据序列号取货请求处理结束，响应参数：{}", JsonMapper.nonDefaultMapper().toJson(respModel));
        return respModel;
    }
}
