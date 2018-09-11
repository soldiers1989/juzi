package com.jzfq.retail.core.swagger.api.selectLog;

import com.alibaba.fastjson.JSONObject;
import com.jzfq.retail.bean.domain.CallAccountsOperation;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.SelectOperationLogReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.service.SelectLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.List;
import java.util.Map;

/**
 * @Title: SelectLogController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月07日 17:28
 * @Description: TODO(用一句话描述该文件做什么)
 */
@Slf4j
@RestController
@RequestMapping("/select/log")
public class SelectLogController {

    @Autowired
    private SelectLogService orderLogService;

    @ApiOperation(value = "通过订单编号(ORDER_SN)查询订单操作日志(ORDER_LOG)")
    @RequestMapping(value = "/orderLog", method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> orderLogList(@RequestBody PageReq<String> req) {
        try {
            ListResultRes<Map<String, Object>> result = orderLogService.getOrderLogList(req.getPage(), req.getPageSize(), req.getSearch());
            return TouchApiResponse.success(result,"查询成功");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("通过订单编号(ORDER_SN:{})查询订单操作日志(ORDER_LOG)异常:{}", req.getSearch(), e.getMessage());
            return TouchApiResponse.failed("通过订单编号(ORDER_SN:" + req.getSearch() + ")查询订单操作日志(ORDER_LOG)异常:" + e.getMessage());
        }
    }

    @ApiOperation(value = "通过MacId和ServiceType查询账务系统调用操作日志(CALL_ACCOUNTS_OPERATION_LOG)",notes = "120开立商户额度账户、130开立商户资金账户、140商户绑卡、200用户绑卡、210恢复信用额度、220扣减信用额度")
    @RequestMapping(value = "/callAccountsOperationLog", method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> callAccountsOperationLogList(@RequestBody @Validated PageReq<SelectOperationLogReq> req) {
        try {
            ListResultRes<Map<String, Object>> result = orderLogService.getCallAccountsLogList(req.getPage(), req.getPageSize(), req.getSearch());
            return TouchApiResponse.success(result,"查询成功");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("通过MacId:{}和ServiceType:{}查询账务系统调用操作日志(CALL_ACCOUNTS_OPERATION_LOG)异常:{}", req.getSearch().getMacId(), req.getSearch().getServiceType(), e.getMessage());
            return TouchApiResponse.failed("通过MacId:" + req.getSearch().getMacId() + "和ServiceType:" + req.getSearch().getServiceType() + "查询账务系统调用操作日志(CALL_ACCOUNTS_OPERATION_LOG)异常:" + e.getMessage());
        }
    }

    @ApiOperation(value = "通过MacId和ServiceType查询资产（资金匹配）系统调用操作日志(CALL_ASSETS_OPERATION_LOG)",notes = " 170资匹生成还款计划、180资匹取消、270还款完成回调")
    @RequestMapping(value = "/callAssetsOperationLog", method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> callAssetsOperationLog(@RequestBody @Validated PageReq<SelectOperationLogReq> req) {
        try {
            ListResultRes<Map<String, Object>> result = orderLogService.getCallAssetsLogList(req.getPage(), req.getPageSize(), req.getSearch());
            return TouchApiResponse.success(result,"查询成功");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("通过MacId:{}和ServiceType:{}查询资产（资金匹配）系统调用操作日志(CALL_ASSETS_OPERATION_LOG)异常:{}", req.getSearch().getMacId(), req.getSearch().getServiceType(), e.getMessage());
            return TouchApiResponse.failed("通过MacId:" + req.getSearch().getMacId() + "和ServiceType:" + req.getSearch().getServiceType() + "查询资产（资金匹配）系统调用操作日志(CALL_ASSETS_OPERATION_LOG)异常:" + e.getMessage());
        }
    }

    @ApiOperation(value = "通过MacId和ServiceType查询风控系统调用操作日志(CALL_RISK_OPERATION_LOG)",notes = " 190风控准入、110风控进件、250风控审核回调、260获取通讯录")
    @RequestMapping(value = "/callRiskOperationLog", method = RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> callRiskOperationLog(@RequestBody @Validated PageReq<SelectOperationLogReq> req) {
        try {
            ListResultRes<Map<String, Object>> result = orderLogService.getCallRiskLogList(req.getPage(), req.getPageSize(), req.getSearch());
            return TouchApiResponse.success(result,"查询成功");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("通过MacId:{}和ServiceType:{}查询风控系统调用操作日志(CALL_RISK_OPERATION_LOG)异常:{}", req.getSearch().getMacId(), req.getSearch().getServiceType(), e.getMessage());
            return TouchApiResponse.failed("通过MacId:" + req.getSearch().getMacId() + "和ServiceType:" + req.getSearch().getServiceType() + "查询风控系统调用操作日志(CALL_RISK_OPERATION_LOG)异常:" + e.getMessage());

        }
    }

    @ApiOperation(value = "通过MacId查询商户开户日志(CALL_ACCOUNTS_OPERATION)")
    @RequestMapping(value = "/callAccountsOperation", method=RequestMethod.POST)
    public ResponseEntity<TouchResponseModel> callAccountsOperation(@RequestBody PageReq<String> req) {
        try {
            ListResultRes<Map<String, Object>> result = orderLogService.getCallAccountsList(req.getPage(), req.getPageSize(), req.getSearch());
            return TouchApiResponse.success(result,"查询成功");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("通过MacId:{}查询通过MacId查询商户开户日志(CALL_ACCOUNTS_OPERATION)异常:{}", req.getSearch(), e.getMessage());
            return TouchApiResponse.failed("通过MacId:" + req.getSearch() + "查询通过MacId查询商户开户日志(CALL_ACCOUNTS_OPERATION)异常:" + e.getMessage());

        }
    }

    public static void main(String[] args){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", 1);
        jsonObject.put("pageSize", 10);
        JSONObject newJson = new JSONObject();
        newJson.put("macId", 330);
        newJson.put("serviceType", 1);

        jsonObject.put("search", newJson);
        System.out.println(jsonObject);
    }


}
