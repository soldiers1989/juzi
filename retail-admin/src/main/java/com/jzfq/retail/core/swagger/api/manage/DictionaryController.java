package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.Dictionary;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.DictionaryReq;
import com.jzfq.retail.bean.vo.req.DictionarySearchReq;
import com.jzfq.retail.bean.vo.req.GpsCheckRuleSearchReq;
import com.jzfq.retail.bean.vo.req.PageReq;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.DictionaryService;
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
 * @Date 2018年06月29日 15:01
 * @Description: 字典表对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {
    @Autowired
    SessionManage sessionManage;

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation(value = "获取字典列表-分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<TouchResponseModel> list(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                              @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              DictionarySearchReq search) {
        PageReq<DictionarySearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = dictionaryService.getList(pageReq.getPage(), pageReq.getPageSize(), pageReq.getSearch());
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "字典添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated({DictionaryReq.CreateMethod.class}) DictionaryReq req) {
        try {
            String username = sessionManage.getUserName();
            dictionaryService.create(req, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "字典数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@RequestBody @Validated({DictionaryReq.UpdateMethod.class}) DictionaryReq req) {
        try {
            String username = sessionManage.getUserName();
            dictionaryService.update(req, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "字典数据删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            dictionaryService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过ID获取字典数据")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getDictionaryById(@PathVariable("id") Integer id) {
        try {
            Dictionary dictionary = dictionaryService.getEntityById(id);
            return TouchApiResponse.success(dictionary, "操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过Code获取字典数据")
    @GetMapping(value = "/code/{code}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getDictionaryByCode(@PathVariable("code") String code) {
        try {
            Dictionary dictionary = dictionaryService.getEntityByCode(code);
            return TouchApiResponse.success(dictionary,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "通过Name获取字典数据")
    @GetMapping(value = "/name/{name}")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getDictionaryByName(@PathVariable("name") String name) {
        try {
            Dictionary dictionary = dictionaryService.getEntityByName(name);
            return TouchApiResponse.success(dictionary,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
