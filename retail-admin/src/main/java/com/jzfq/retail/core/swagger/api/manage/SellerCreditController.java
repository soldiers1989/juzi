package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.SellerSingleCredit;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.SellerSingleCreditReq;
import com.jzfq.retail.bean.vo.res.ApiResponse;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerSingleCreditService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:57
 * @Description: 商户单笔授信额度对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/seller")
public class SellerCreditController {

    @Autowired
    SessionManage sessionManage;

    @Autowired
    private SellerSingleCreditService sellerSingleCreditService;

    private SellerSingleCredit transReq(SellerSingleCreditReq req) {
        if (req == null) {
            throw new RuntimeException("接口传入参数为空。");
        }

        SellerSingleCredit sellerSingleCredit = new SellerSingleCredit();
        BeanUtils.copyProperties(req, sellerSingleCredit);
        return sellerSingleCredit;
    }

    @ApiOperation(value = "获取商户单笔授信全部数据-无分页")
    @RequestMapping(value = "/credit/list/all", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> allList(@RequestBody SellerSingleCreditReq search) {
        CommonValid.emptyStringToNull(search);
        try {
            List<SellerSingleCredit> result = sellerSingleCreditService.getAllList(transReq(search));
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取商户单笔授信列表-分页")
    @RequestMapping(value = "/credit/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@Valid @RequestBody PageReq<SellerSingleCreditReq> req) {
        try {
            ListResultRes result = sellerSingleCreditService.getList(req.getPage(), req.getPageSize(), transReq(req.getSearch()));
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

    @ApiOperation(value = "商户单笔授信添加")
    @RequestMapping(value = "/credit/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> add(@Valid @RequestBody SellerSingleCreditReq req) {
        try {
            sellerSingleCreditService.create(transReq(req));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户单笔授信数据修改")
    @RequestMapping(value = "/credit/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@Valid @RequestBody SellerSingleCreditReq req) {
        try {
            sellerSingleCreditService.update(transReq(req));
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户单笔授信数据删除")
    @GetMapping(value = "/credit/delete/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            SellerSingleCredit sellerSingleCredit = sellerSingleCreditService.getEntityById(id);
            sellerSingleCreditService.delete(id);
            return TouchApiResponse.success(sellerSingleCredit,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过ID获取商户单笔授信数据")
    @GetMapping(value = "/credit/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getCreditById(@PathVariable("id") Integer id) {
        try {
            SellerSingleCredit sellerSingleCredit = sellerSingleCreditService.getEntityById(id);
            return TouchApiResponse.success(sellerSingleCredit,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过SellerID获取商户单笔授信数据")
    @GetMapping(value = "/credit/seller/{sellerId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getCreditBySellerId(@PathVariable("sellerId") Integer sellerId) {
        try {
            SellerSingleCredit sellerSingleCredit = sellerSingleCreditService.getEntityBySellerId(sellerId);
            return TouchApiResponse.success(sellerSingleCredit,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户单笔授信设置",notes = "openCredit参数->0：未开启，1：已开启")
    @GetMapping(value = "/opensinglecredit")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> updateSingleCredit(@RequestParam("id") Integer id, @RequestParam("openCredit") int openCredit,
                                                            @RequestParam("editor") String editor) {
        return updateCredit(id, openCredit, new Long(-100), editor);
    }

    @ApiOperation(value = "商户单笔授信设置",notes = "openCredit参数->0：未开启，1：已开启")
    @PostMapping("/creditline")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> updateCredit(@RequestParam("id") Integer id, @RequestParam("openCredit") int openCredit,
                                                      @RequestParam("creditValue") Long creditValue, @RequestParam("editor") String editor) {
        try {
            sellerSingleCreditService.updateCredit(id, openCredit, creditValue, editor);
            return TouchApiResponse.success(sellerSingleCreditService.getEntityById(id),"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "商户单笔授信设置",notes = "openCredit参数->0：未开启，1：已开启")
    @GetMapping("/openCredit/{sellerId}/{isOpen}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> openCredit(@PathVariable Integer sellerId, @PathVariable Integer isOpen) {
        try {
            String username = sessionManage.getUserName();
            sellerSingleCreditService.openCredit(sellerId, isOpen, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
