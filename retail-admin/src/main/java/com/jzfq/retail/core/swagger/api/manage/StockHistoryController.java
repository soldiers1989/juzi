package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.PurchaseCollectGoods;
import com.jzfq.retail.bean.domain.StockHistory;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.PurchaseCollectGoodsReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.StockHistoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: 入库对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/stock/history")
public class StockHistoryController {


    @Autowired
    private StockHistoryService stockHistoryService;

    @ApiOperation(value = "获取入库全部数据-无分页")
    @RequestMapping(value = "/list/all", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> allList(@RequestBody StockHistory search) {
        CommonValid.emptyStringToNull(search);
        try {
            List<StockHistory> result = stockHistoryService.getAllList(search);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }
    
    @ApiOperation(value = "获取入库列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@Valid @RequestBody PageReq<StockHistory> req) {
        try {
            ListResultRes result = stockHistoryService.getList(req.getPage(), req.getPageSize(), req.getSearch());
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "入库添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@Valid @RequestBody StockHistory req) {
        try {
            stockHistoryService.create(req);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "入库数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@Valid @RequestBody StockHistory req) {
        try {
            stockHistoryService.update(req);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "入库数据删除")
    @GetMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            stockHistoryService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过ID获取入库数据")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getSellerById(@PathVariable("id") Integer id) {
        try {
            StockHistory entity = stockHistoryService.getEntityById(id);
            return TouchApiResponse.success(entity,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
