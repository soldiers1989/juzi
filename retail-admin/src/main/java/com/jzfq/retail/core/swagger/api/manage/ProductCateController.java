package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.ProductCateReq;
import com.jzfq.retail.bean.vo.req.ProductCateSearchReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ProductCateService;
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
@RequestMapping("/api/product/cate")
public class ProductCateController {


    @Autowired
    private ProductCateService productCateService;

    @Autowired
    private SessionManage sessionManage;


    @ApiOperation(value = "获取商品分类列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                              @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              ProductCateSearchReq search) {
        PageReq<ProductCateSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = productCateService.getList(pageReq.getPage(), pageReq.getPageSize(), search);
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

    @ApiOperation(value = "商品分类添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated({ProductCateReq.CreateMethod.class}) ProductCateReq entity) {
        try {
            String userName = sessionManage.getUserName();
            productCateService.create(entity, userName);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商品分类修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@RequestBody @Validated({ProductCateReq.UpdateMethod.class}) ProductCateReq entity) {
        try {
            String userName = sessionManage.getUserName();
            productCateService.update(entity, userName);
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

    @ApiOperation(value = "商品分类删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            productCateService.delete(id);
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

//    @ApiOperation(value = "通过ID获取商品分类")
//    @GetMapping(value = "/{id}")
//    @ResponseBody
//    public ResponseEntity<ResponseModel> getProductCateById(@PathVariable("id") Integer id) {
//        try {
//            ProductCate productCate = productCateService.getEntityById(id);
//            return ApiResponse.success(productCate);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error(e.getMessage());
//            return ApiResponse.failed(e.getMessage());
//        }
//    }

    @ApiOperation(value = "获取id/name映射")
    @GetMapping(value = "/options")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getOptions() {
        try {
            List<Map<String, Object>> result = productCateService.getOptions();
            return TouchApiResponse.success(result, "操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取id/name映射")
    @GetMapping(value = "/options/by/{sellerId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getOptionsBySellerId(@PathVariable Integer sellerId) {
        try {
            List<Map<String, Object>> result = productCateService.getOptionsBySellerId(sellerId);
            return TouchApiResponse.success(result, "操作成功");
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
