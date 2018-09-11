package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.SysPermission;
import com.jzfq.retail.bean.domain.SysPermissionDTO;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SysPermissionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/sysPermission")
public class SysPermissionController {
    @Autowired
    SysPermissionService sysPermissionService;

    @ApiOperation(value = "查询整棵树", notes = "查询整棵树")
    @RequestMapping(value = "/getTree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> getTree(){
        try {
            SysPermissionDTO sysPermissionDTO = sysPermissionService.getTree2();
            return TouchApiResponse.success(sysPermissionDTO,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商品品牌添加异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增", notes = "新增 ")
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> save(@RequestBody SysPermission sysPermission){
        try {
            sysPermissionService.save(sysPermission);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("权限添加异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> update(@RequestBody List<SysPermission> sysPermissionList){
        try {
            sysPermissionService.updateMulti(sysPermissionList);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("权限编辑异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> delete(@ApiParam(value = "主键") @Valid @RequestParam(value = "ids", required = false) String ids){
        try {
            sysPermissionService.deleteMulti(ids);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("权限编辑异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "ID查询", notes = "ID查询")
    @RequestMapping(value = "/getById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> getById(@ApiParam(value = "ID查询") @Valid @RequestParam(value = "id", required = false) Integer id){
        try {
            SysPermission sysPermission = sysPermissionService.findById(id);
            SysPermissionDTO sysPermissionDTO = new SysPermissionDTO();
            TransferUtil.transfer(sysPermissionDTO, sysPermission);
            return TouchApiResponse.success(sysPermissionDTO,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("权限编辑异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }
}
