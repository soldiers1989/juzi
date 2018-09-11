package com.jzfq.retail.core.swagger.api.manage;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.Seller;
import com.jzfq.retail.bean.domain.SellerCateBrandRel;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerBusiService;
import com.jzfq.retail.core.api.service.SellerCateBrandRelService;
import com.jzfq.retail.core.api.service.SellerService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月29日 17:26
 * @Description: 商户表对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/seller")
public class SellerController {


    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerBusiService sellerBusiService;

    @Autowired
    private SellerCateBrandRelService sellerCateBrandRelService;

    @Autowired
    SessionManage sessionManage;

    private Seller transReq(SellerReq req) {
        if (req == null) {
            throw new RuntimeException("接口传入参数为空");
        }

        Seller seller = new Seller();
        BeanUtils.copyProperties(req, seller);
        return seller;
    }

    @ApiOperation(value = "获取商户全部数据-无分页")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> allList(SellerSearchReq search) {
        CommonValid.emptyStringToNull(search);
        try {
            List<Map<String, Object>> result = sellerService.getAllList(search);
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

    @ApiOperation(value = "获取商户列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                              @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              SellerSearchReq search) {
        PageReq<SellerSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = sellerService.getList2(pageReq.getPage(), pageReq.getPageSize(), search);
            return TouchApiResponse.success(result,"操作成功");
        } catch (Exception e) {
            log.error("获取商品品牌列表异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@Valid @RequestBody SellerReq seller) {
        try {
            sellerService.create(transReq(seller));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@Valid @RequestBody SellerReq seller) {
        try {
            sellerService.update(transReq(seller));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "删除商户")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            sellerService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过ID获取商户数据")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getSellerById(@PathVariable("id") Integer id) {
        try {
            Seller seller = sellerService.getSellerById(id);
            return TouchApiResponse.success(seller,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户冻结状态修改")
    @RequestMapping(value = "/updateAuditStatus", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> updateAuditStatus(Integer id, Integer auditStatus) {
        try {
            sellerService.updateAuditStatus(id, auditStatus);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户冻结状态修改")
    @RequestMapping(value = "/freeze/{sellerId}/{freezeType}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> freeze(@PathVariable Integer sellerId, @PathVariable String freezeType) {
        try {
            sellerService.freeze(sellerId, freezeType);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户冻结状态查询")
    @RequestMapping(value = "/getAuditStatus", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getAuditStatus(Integer id) {
        try {
            boolean isFrozen = sellerService.sellerIsFrozen(id);
            return TouchApiResponse.success(isFrozen? "商户冻结中" : "商户未冻结");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户审核列表TODO")
    @RequestMapping(value = "/sellerApprovalList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> sellerApprovalList(@RequestBody SellerCateBrandRel sellerCateBrandRel) {
        try {
            List<Map<String, Object>> data = sellerCateBrandRelService.findOptList(sellerCateBrandRel);
            return TouchApiResponse.success(data,"操作成功");

        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取id/name映射")
    @GetMapping(value = "/options")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getOptions(OptionsSearchReq search) {
        try {
            List<Map<String, Object>> options = sellerService.getOptions(search);
            return TouchApiResponse.success(options,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询还款计划")
    @GetMapping(value = "/getOrderRepaymentsTest")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getOrderRepaymentsTest(Integer orderId) {
        try {
            ListResultRes<Map<String, Object>> list = sellerBusiService.getOrderRepayments(orderId,1000,0,1,20);
            return TouchApiResponse.success(list.getList(),"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户详细信息")
    @GetMapping(value = "/sellerDetailInfo/{sellerId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getSellerDetailInfo(@PathVariable Integer sellerId) {
        try {
            Map<String, Object> result = sellerService.getSellerDetailInfo(sellerId);
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
}
