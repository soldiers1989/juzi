package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.vo.res.ApiResponse;
import com.jzfq.retail.bean.vo.res.ResponseModel;
import com.jzfq.retail.common.util.ExcelUtil;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.DataTransferService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jzfq.retail.common.util.date.DateUtil.FORMATTING_DATETIME_SECOND;

@Slf4j
@RestController
@RequestMapping("/foreign/dataTransfer")
public class DataTransferController {

    @Autowired
    private DataTransferService dataTransferService;

    @ApiOperation(value = "导入初始化数据")
    @RequestMapping(value = "/initDataImport", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseModel> initDataImport(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        log.info("初始化数据导入接口请求开始，时间：{}", DateUtil.getDate(FORMATTING_DATETIME_SECOND));
        ResponseEntity<ResponseModel> responseResult = null;
        try {
            // 品类
            List<Map<Integer, String>> productCates = ExcelUtil.readExcel(file,5);
            // 品牌
            List<Map<Integer, String>> productBrands = ExcelUtil.readExcel(file,6);
            // 分控规则（区间价）
            List<Map<Integer, String>> productCateBrandAreas = ExcelUtil.readExcel(file,7);
            // 商户商品
            List<Map<Integer, String>> products = ExcelUtil.readExcel(file,8);
            // 商户管理725
            List<Map<Integer, String>> sellers = ExcelUtil.readExcel(file,10);
            // 商户对应分期规则
            List<Map<Integer, String>> sellerTerms = ExcelUtil.readExcel(file,12);
            // 商户帐号密码
            List<Map<Integer, String>> sellerLoginPermissions = ExcelUtil.readExcel(file,13);

            // == 解决 异常【getWriter() has already been called for this response】，非业务代码  start ==
            response.reset();
            // == 解决 异常【getWriter() has already been called for this response】，非业务代码  end ==
            dataTransferService.initDataImport(sellers,products,productCateBrandAreas,
                            sellerTerms,sellerLoginPermissions, productCates,productBrands);

            log.info("初始化数据导入接口请求结束，时间：{}", DateUtil.getDate(FORMATTING_DATETIME_SECOND));
            responseResult = ApiResponse.success("已接收数据初始化指令，请稍候验证！");
        } catch (Exception e) {
            log.error("初始化数据导入异常，异常描述：{}", e);
            responseResult = ApiResponse.failed("初始化数据导入异常，异常原因：" + e.getMessage());
        }
        return responseResult;
    }

    @ApiOperation(value = "商户入住调用账务系统(初始化数据时调用)")
    @RequestMapping(value = "/callAccounts", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseModel> callAccounts(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        log.info("商户入住调用账务系统，时间：{}", DateUtil.getDate(FORMATTING_DATETIME_SECOND));
        ResponseEntity<ResponseModel> responseResult = null;
        try {
            // 商户管理725
            List<Map<Integer, String>> sellers = ExcelUtil.readExcel(file,10);


            // == 解决 异常【getWriter() has already been called for this response】，非业务代码  start ==
            response.reset();
            // == 解决 异常【getWriter() has already been called for this response】，非业务代码  end ==

            dataTransferService.batchCallAccounts(sellers);
            log.info("商户入住调用账务系统，时间：{}", DateUtil.getDate(FORMATTING_DATETIME_SECOND));
            responseResult = ApiResponse.success("商户入住调用账务系统已调用，请稍候验证！");
        } catch (Exception e) {
            log.error("商户入住调用账务系统，异常描述：{}", e);
            responseResult = ApiResponse.failed("商户入住调用账务系统异常，异常原因：" + e.getMessage());
        }
        return responseResult;
    }
}
