package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.domain.SellerRatio;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerRatioService;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.SellerRatioReq;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:57
 * @Description: 商户分期首付表对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/seller/ratio")
public class SellerRatioController {


    @Autowired
    private SellerRatioService sellerRatioService;

    private SellerRatio transReq(SellerRatioReq req) {
        if (req == null) {
            throw new RuntimeException("接口传入参数为空。");
        }

        SellerRatio SellerRatio = new SellerRatio();
        BeanUtils.copyProperties(req, SellerRatio);
        return SellerRatio;
    }

    @ApiOperation(value = "获取商户分期首付全部数据-无分页")
    @RequestMapping(value = "/list/all", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> allList(@RequestBody SellerRatioReq search) {
        CommonValid.emptyStringToNull(search);
        try {
            List<SellerRatio> result = sellerRatioService.getAllList(transReq(search));
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    private ResponseEntity<TouchResponseModel> failResponse(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return TouchApiResponse.failed(e.getMessage());
    }

    @ApiOperation(value = "获取商户分期首付列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@Valid @RequestBody PageReq<SellerRatioReq> req) {
        try {
            ListResultRes result = sellerRatioService.getList(req.getPage(), req.getPageSize(), transReq(req.getSearch()));
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户分期首付添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@Valid @RequestBody SellerRatioReq req) {
        try {
            sellerRatioService.create(transReq(req));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户分期首付数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@Valid @RequestBody SellerRatioReq req) {
        try {
            sellerRatioService.update(transReq(req));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户分期首付数据删除")
    @GetMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            sellerRatioService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过ID获取商户分期首付数据")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getSellerById(@PathVariable("id") Integer id) {
        try {
            SellerRatio entity = sellerRatioService.getEntityById(id);
            return TouchApiResponse.success(entity,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
