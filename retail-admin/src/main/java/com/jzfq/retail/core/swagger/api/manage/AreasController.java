package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.domain.Areas;
import com.jzfq.retail.bean.vo.req.AreasSearchReq;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.ProductCateSearchReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.AreasService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月28日 11:16
 * @Description: 城市操作对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/areas")
public class AreasController {

    @Autowired
    private AreasService areasService;

    @ApiOperation(value = "获取商品分类列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                   @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   AreasSearchReq search) {
        PageReq<AreasSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = areasService.getList(pageReq.getPage(), pageReq.getPageSize(), search);
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

    /**
     * 通过父类编号，查询所属下级城市
     *
     * @param parentId
     * @return
     */
    @ApiOperation(value = "通过父类编号获取城市地区列表")
    @GetMapping("/get/by/parentId/{parentId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getAreaByParentId(@PathVariable("parentId") String parentId) {
        try {
            List<Areas> result = areasService.getAreaByParentId(parentId);
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

    @ApiOperation(value = "通过城市编号，查询单个城市")
    @GetMapping("/get/by/areaId/{areaId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getAreaByAreaId(@PathVariable("areaId") String areaId) {
        try {
            Areas result = areasService.getAreaByAreaId(areaId);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();log.error("操作异常: {}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "通过城市编号查询城市全称")
    @GetMapping("/get/mergerName/by/{areaId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getMergerNameByAreaId(@PathVariable("areaId") String areaId) {
        try {
            Object result = areasService.getMergerNameByAreaId(areaId);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();log.error("操作异常: {}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过城市编号查询城市全称的简称")
    @GetMapping("/get/mergerShortName/by/{areaId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getMergerShortNameByAreaId(@PathVariable("areaId") String areaId) {
        try {
            Object result = areasService.getMergerShortNameByAreaId(areaId);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();log.error("操作异常: {}", e);
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取id/name映射")
    @GetMapping(value = "/options/{parentId}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getOptions(@PathVariable("parentId") String parentId) {
        try {
            List<Map<String, Object>> result = areasService.getOptions(parentId);
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
