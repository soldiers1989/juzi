package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.vo.req.OrdersSettleSearch;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.SellerSettlePointReq;
import com.jzfq.retail.bean.vo.req.SellerSettlePointSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.SystemUserStatus;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerSettlePointService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年07月18日 11:35
 * @Description: 商户结算扣点设定控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/SellerSettlePoint")
public class SellerSettlePointController {


    @Autowired
    private SellerSettlePointService sellerSettlePointService;


    @ApiOperation(value = "新增商户结算扣点")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated({SellerSettlePointReq.CreateMethod.class}) SellerSettlePointReq req) {
        try {
            int saveCount = sellerSettlePointService.save(req);
            log.info("新增商户结算扣点接口正常结束，保存记录总数：{}", saveCount);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("新增商户结算扣点接口异常，异常描述：{}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "删除商户结算扣点")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> delete(@ApiParam(value = "商户id") @RequestParam(value = "sellerId") Integer sellerId) {
        log.info("删除商户结算扣点接口开始，请求参数，商户id：{}",sellerId);
        try {
            int deleteCount = sellerSettlePointService.delete(sellerId);
            log.info("删除商户结算扣点接口正常结束，删除记录总数：{}", deleteCount);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("删除商户结算扣点接口异常，异常描述：{}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "更新商户结算扣点")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@RequestBody @Validated({SellerSettlePointReq.UpdateMethod.class}) SellerSettlePointReq req) {
        try {
            int updateCount = sellerSettlePointService.update(req);
            log.info("更新商户结算扣点接口正常结束，更新记录总数：{}", updateCount);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("更新商户结算扣点接口异常，异常描述：{}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询商户结算扣点列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getList(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                      @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                      SellerSettlePointSearchReq search) {
        PageReq<SellerSettlePointSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = sellerSettlePointService.findList(pageReq.getPage(), pageReq.getPageSize(), pageReq.getSearch());
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
}
