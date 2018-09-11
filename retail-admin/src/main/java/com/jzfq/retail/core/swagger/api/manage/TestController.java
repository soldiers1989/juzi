package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.service.ProductService;
import com.jzfq.retail.core.api.service.RocketMQService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年07月15日 17:45
 * @Description: 商品表对外接口
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private ProductService productService;

    @Autowired
    private RocketMQService rocketMQService;

    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequestMapping(value = "/import/excel/product",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> importExcelProduct(@RequestPart("file") MultipartFile file) {
        Map<String, List<ImportExcelResult>> result = productService.importExcel(file);
        return TouchApiResponse.success(result,"操作成功");
    }

    @ApiOperation(value = "RocketMQ Test", notes = "RocketMQ Test")
    @RequestMapping(value = "/mq/test",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> mqTest() {
        rocketMQService.sendMq();
        return TouchApiResponse.success("操作成功");
    }
}
