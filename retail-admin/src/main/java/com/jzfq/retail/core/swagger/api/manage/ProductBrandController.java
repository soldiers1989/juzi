package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ProductBrandService;
import com.jzfq.retail.core.config.SessionManage;
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
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月26日 15:01
 * @Description: 商品分类对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/product/brand")
public class ProductBrandController {

    @Autowired
    private ProductBrandService productBrandService;

    @Autowired
    private SessionManage sessionManage;

    @ApiOperation(value = "获取商品品牌列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                   @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   ProductBrandSearchReq search) {
        PageReq<ProductBrandSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes result = productBrandService.getList(pageReq.getPage(), pageReq.getPageSize(), pageReq.getSearch());
            return TouchApiResponse.success(result, "操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("获取商品品牌列表异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商品品牌添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated({ProductBrandReq.CreateMethod.class}) ProductBrandReq entity) {
        try {
            String username = sessionManage.getUserName();
            productBrandService.create(entity, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商品品牌添加异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商品品牌修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@RequestBody @Validated({ProductBrandReq.UpdateMethod.class}) ProductBrandReq entity) {
        try {
            String username = sessionManage.getUserName();
            productBrandService.update(entity, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商品品牌修改异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商品品牌删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            String username = sessionManage.getUserName();
            productBrandService.delete(id, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商品品牌删除异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

//    @ApiOperation(value = "通过ID获取商品品牌")
//    @GetMapping(value = "/{id}")
//    @ResponseBody
//    public ResponseEntity<ResponseModel> getProductBrandById(@PathVariable("id") Integer id) {
//        try {
//            ProductBrand productBrand = productBrandService.getEntityById(id);
//            return ApiResponse.success(productBrand);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("通过ID获取商品品牌异常：{}", e.getMessage());
//            return ApiResponse.failed(e.getMessage());
//        }
//    }

    @ApiOperation(value = "获取id/name映射")
    @GetMapping(value = "/options/{cateId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getOptions(@PathVariable("cateId") Integer cateId) {
        try {
            List<Map<String, Object>> result = productBrandService.getOptions(cateId);
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

    @ApiOperation(value = "通过商品分类ID获取下级品牌列表")
    @GetMapping(value = "/get/by/{cateId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getProductBrandByProductCateId(@PathVariable("cateId") Integer cateId) {
        try {
            List<ProductBrand> result = productBrandService.getProductBrandByProductCateId(cateId);
            return TouchApiResponse.success(result,"操作成功");

        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过商品分类ID获取下级品牌列表异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }
}
