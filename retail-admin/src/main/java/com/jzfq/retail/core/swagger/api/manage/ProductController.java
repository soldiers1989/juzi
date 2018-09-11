package com.jzfq.retail.core.swagger.api.manage;

import com.alibaba.fastjson.JSONArray;
import com.jzfq.retail.bean.domain.Product;
import com.jzfq.retail.bean.domain.ProductWithBLOBs;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:57
 * @Description: 商品表对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/goods")
public class ProductController {


    @Autowired
    private ProductService productService;

    @ApiOperation(value = "获取商品列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                              @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              ProductSearchReq search) {
        PageReq<ProductSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = productService.getList(pageReq.getPage(), pageReq.getPageSize(), search);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商品添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated({ProductWithBLOBsReq.CreateMethod.class}) ProductWithBLOBsReq product) {
        try {
            ProductWithBLOBs target = new ProductWithBLOBs();
            TransferUtil.transferIgnoreNull(product, target);
            target.setState(3);
            target.setCreateTime(new Date());
            productService.create(target);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商品数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@RequestBody @Validated({ProductWithBLOBsReq.UpdateMethod.class}) ProductWithBLOBsReq req) {
        try {
            ProductWithBLOBs dist = productService.getProductById(req.getId());
            TransferUtil.transferIgnoreNull(req, dist);
            productService.update(dist);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商品数据删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            productService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过ID获取商品数据")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getSellerById(@PathVariable("id") Integer id) {
        try {
            ProductWithBLOBs productWithBLOBs = productService.getProductById(id);
            return TouchApiResponse.success(productWithBLOBs,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequestMapping(value = "/importExcel",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> importExcel(@RequestPart("file") MultipartFile file) {
        Map<String, List<ImportExcelResult>> result = productService.importExcel(file);
        return TouchApiResponse.success(result,"操作成功");
    }

    @ApiOperation(value = "店铺装修查询店铺下的商品信息")
    @RequestMapping(value = "/getSellerProdct/{sellerId}",method = RequestMethod.GET)
    public ResponseEntity<TouchResponseModel> getSellerProduct(@PathVariable("sellerId") Integer sellerId){
        try {
            JSONArray jsonArray = productService.getProductNames(sellerId);
            return TouchApiResponse.success(jsonArray,"查询成功");
        } catch (TouchCodeException te){
            log.error("请求返回数据异常：{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
