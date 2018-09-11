package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.domain.RemittanceAccount;
import com.jzfq.retail.bean.vo.req.ProductCateBrandAreasReq;
import com.jzfq.retail.bean.vo.req.RemittanceAccountReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.common.enmu.SystemUserStatus;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.service.RemittanceAccountService;
import com.jzfq.retail.core.api.service.SellerSettlePointService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年07月18日 11:35
 * @Description: 打款账户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/RemittanceAccount")
public class RemittanceAccountController {


    @Autowired
    private RemittanceAccountService remittanceAccountService;


    @ApiOperation(value = "新增打款账户")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated({RemittanceAccountReq.CreateMethod.class}) RemittanceAccountReq entity) {
        log.info("新增打款账户接口开始，请求参数：{}",new JsonMapper().toJson(entity));
        try {
            int saveCount = remittanceAccountService.save(entity);
            log.info("新增打款账户接口正常结束，保存记录总数：{}", saveCount);
            return TouchApiResponse.success("操作成功");
        } catch (Exception e) {
            log.error("新新增打款账户接口异常，异常描述：{}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "删除打款账户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> delete(@RequestBody @Validated({RemittanceAccountReq.UpdateMethod.class}) RemittanceAccountReq remittanceAccountReq) {
        log.info("删除打款账户接口开始，请求参数，商户id：{}",new JsonMapper().toJson(remittanceAccountReq));
        try {
            int deleteCount = remittanceAccountService.delete(remittanceAccountReq);
            log.info("删除打款账户接口正常结束，删除记录总数：{}", deleteCount);
            return TouchApiResponse.success("操作成功");
        } catch (Exception e) {
            log.error("删除打款账户接口异常，异常描述：{}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "更新打款账户")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@RequestBody @Validated({RemittanceAccountReq.UpdateMethod.class}) RemittanceAccountReq entity) {
        log.info("更新打款账户接口开始，请求参数：{}",new JsonMapper().toJson(entity));
        try {
            // 更新
            int updateCount = remittanceAccountService.update(entity);
            log.info("更新打款账户接口正常结束，更新记录总数：{}", updateCount);
            return TouchApiResponse.success("操作成功");
        } catch (Exception e) {
            log.error("更新打款账户接口异常，异常描述：{}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询帐户列表")
    @RequestMapping(value = "/queryRemittanceAccountList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> queryRemittanceAccountList(@ApiParam(value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                    @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                    @ApiParam(value = "店铺名称") @RequestParam(value = "sellerName",required = false) String sellerName) {
        log.info("查询帐户列表接口开始，商户号：{}",sellerName);
        try {
            // 查询
            ListResultRes<HashMap<String,Object>> result =  remittanceAccountService.queryRemittanceAccountList(page,pageSize,sellerName);
            log.info("查询帐户列表接口正常结束，查询结果：{}", new JsonMapper().toJson(result.getList()));
            return TouchApiResponse.success(result,"操作成功");
        } catch (Exception e) {
            log.error("查询帐户列表接口异常，异常描述：{}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }
}
