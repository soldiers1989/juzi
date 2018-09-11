package com.jzfq.retail.core.swagger.api.manage;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.Seller;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerService;
import com.jzfq.retail.bean.vo.req.SellerSearchReq;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年06月29日 17:26
 * @Description: 系统日志管理
 */
@Slf4j
@RestController
@RequestMapping("/api/log")
public class LogController {


    @Autowired
    private SellerService sellerService;


    @ApiOperation(value = "获取全部日志列表-无分页")
    @RequestMapping(value = "/list/all", method = RequestMethod.POST)
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
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取日志列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                              SellerSearchReq search) {
        CommonValid.emptyStringToNull(search);
        try {
            Page<Map<String, Object>> result = sellerService.getList(page, pageSize, search);
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

    @ApiOperation(value = "添加商户日志")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(Seller seller) {
        try {
            sellerService.create(seller);
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

    @ApiOperation(value = "日志数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(Seller seller) {
        try {
            sellerService.update(seller);
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

    @ApiOperation(value = "删除日志")
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteRole(Seller seller) {
        try {
            sellerService.update(seller);
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

}
