package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.SellerTerm;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.SellerTermReq;
import com.jzfq.retail.bean.vo.req.SellerTermSearchReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerTermService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:57
 * @Description: 商户分期表对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/seller/term")
public class SellerTermController {

    @Autowired
    private SellerTermService sellerTermService;

    @Autowired
    SessionManage sessionManage;

    @ApiOperation(value = "获取商户分期列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                   @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   SellerTermSearchReq search) {
        PageReq<SellerTermSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = sellerTermService.getList(pageReq.getPage(), pageReq.getPageSize(), pageReq.getSearch());
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户分期添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated SellerTermReq req) {
        try {
            SellerTerm target = new SellerTerm();
            TransferUtil.transferIgnoreNull(req, target);
            String username = sessionManage.getUserName();
            sellerTermService.create(target, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户分期数据删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            sellerTermService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过ID获取商户分期数据")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getSellerById(@PathVariable("id") Integer id) {
        try {
            SellerTerm sellerTerm = sellerTermService.getSellerTermById(id);
            return TouchApiResponse.success(sellerTerm,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
