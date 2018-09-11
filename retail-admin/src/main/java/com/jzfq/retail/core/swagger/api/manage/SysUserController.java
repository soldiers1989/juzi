package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.req.SysUserSearchReq;
import com.jzfq.retail.bean.vo.req.SysUserVo;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/sysUser")
public class SysUserController{
    @Autowired
    SysUserService sysUerService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    ResponseEntity<TouchResponseModel> getByPage(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                 @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 SysUserSearchReq search){
        PageReq<SysUserSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = sysUerService.getList(pageReq.getPage(), pageReq.getPageSize(), pageReq.getSearch());
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增", notes = "新增 ")
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> save(@RequestBody @Validated SysUserVo sysUser){
        try {
            sysUerService.save(sysUser);
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

    @ApiOperation(value = "编辑", notes = "编辑")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> update(@RequestBody SysUserVo sysUser){
        try {
            sysUerService.update(sysUser);
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

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> delete(@ApiParam(value = "主键") @Valid @RequestParam(value = "id", required = true) Integer id){
        try {
            sysUerService.removeById(id);
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

    @ApiOperation(value = "批量删除",notes = "批量删除")
    @RequestMapping(value = "/remove", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> remove(@ApiParam(value = "主键拼串") @Valid @RequestParam(value = "ids", required = true) String ids){
        try {
            String failMsg = sysUerService.batchRemove(ids);
            if(StringUtils.isBlank(failMsg)){
                return TouchApiResponse.success("操作成功");
            }else{
                return TouchApiResponse.badRequest(failMsg);
            }
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }


    public Map page(List list , int count){
        Map map 			            = new HashMap();
        map.put("recordsTotal", 		count);
        map.put("data",                 list);
        return map;
    }
}
