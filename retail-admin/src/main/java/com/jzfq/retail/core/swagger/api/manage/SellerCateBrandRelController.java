package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.ProductBrandSearchReq;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelReq;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelSearchReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerCateBrandRelService;
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
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年07月26日 16:16
 * @Description: 经营品类品牌
 */
@Slf4j
@RestController
@RequestMapping("/api/sellerCateBrandRel")
public class SellerCateBrandRelController {

    @Autowired
    private SessionManage sessionManage;
    @Autowired
    private SellerCateBrandRelService sellerCateBrandRelService;

    @ApiOperation(value = "商户经营品类品牌")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> createSellerCateBrand(@RequestBody @Validated({SellerCateBrandRelReq.CreateMethod.class}) SellerCateBrandRelReq req) {
        try {
            String username = sessionManage.getUserName();
            sellerCateBrandRelService.create(req, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户设定经营品类品牌")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> updateSellerCateBrand(@RequestBody @Validated({SellerCateBrandRelReq.UpdateMethod.class}) SellerCateBrandRelReq req) {
        try {
            String username = sessionManage.getUserName();
            sellerCateBrandRelService.update(req, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "删除经营品类品牌")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            sellerCateBrandRelService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "经营品类品牌-分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                              @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              SellerCateBrandRelSearchReq search) {
        PageReq<SellerCateBrandRelSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = sellerCateBrandRelService.getList(pageReq.getPage(), pageReq.getPageSize(), search);
            return TouchApiResponse.success(result, "操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            log.error("获取商品品牌列表异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    private ResponseEntity<TouchResponseModel> failResponse(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return TouchApiResponse.failed(e.getMessage());
    }
}
