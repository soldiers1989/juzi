package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.domain.ProductCateBrandAreas;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.ProductCateBrandAreasService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月28日 16:17
 * @Description: 分类-品牌-城市-区间价 对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/rule")
public class ProductCateBrandAreasController {


    @Autowired
    private ProductCateBrandAreasService productCateBrandAreasService;

    @Autowired
    SessionManage sessionManage;


    @ApiOperation(value = "分类-品牌-城市-区间价列表筛选查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<TouchResponseModel> getList(@ApiParam(value = "页码") @RequestParam(value = "page", required = false) Integer page,
                                                      @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                      ProductCateBrandAreasSearchReq search) {
        PageReq<ProductCateBrandAreasSearchReq> pageReq = new PageReq<>(page, pageSize, search);
        try {
            ListResultRes<Map<String, Object>> result = productCateBrandAreasService.getList(pageReq.getPage(), pageReq.getPageSize(), pageReq.getSearch());
            return TouchApiResponse.success(result, "操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分类-品牌-城市-区间价列表筛选查询异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "分类-品牌-城市-区间价新增")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(@RequestBody @Validated({ProductCateBrandAreasReq.CreateMethod.class}) ProductCateBrandAreasReq entity) {
        try {
            String username = sessionManage.getUserName();
            productCateBrandAreasService.create(entity, username);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分类-品牌-城市-区间价新增异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "分类-品牌-城市-区间价修改", notes = "只有在状态为‘0-创建’的时候才可以修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(@RequestBody @Validated({ProductCateBrandAreasReq.UpdateMethod.class}) ProductCateBrandAreasReq entity) {
        try {
            productCateBrandAreasService.update(entity);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分类-品牌-城市-区间价修改异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

//    @ApiOperation(value = "分类-品牌-城市-区间价 通过ID获取实例")
//    @GetMapping("/get/by/{id}")
//    @ResponseBody
//    public ResponseEntity<ResponseModel> getEntityById(@PathVariable("id") Integer id) {
//        try {
//            ProductCateBrandAreas entity = productCateBrandAreasService.getEntityById(id);
//            return ApiResponse.success(entity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("分类-品牌-城市-区间价 通过ID获取实例异常：{}", e.getMessage());
//            return ApiResponse.failed(e.getMessage());
//        }
//    }


    @ApiOperation(value = "分类-品牌-城市-区间价 审核操作", notes = "opt参数->success：审核通过，fail：审核失败")
    @RequestMapping(value = "/operation/auditing", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> operationAuditing(@RequestBody @Validated PCBAOperationAuditing operationAuditing) {
        try {
            String userName = sessionManage.getUserName();
            productCateBrandAreasService.operationAuditing(operationAuditing.getId(), operationAuditing.getOpt(), userName);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分类-品牌-城市-区间价 审核操作异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "分类-品牌-城市-区间价 删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<TouchResponseModel> deleteById(@PathVariable("id") Integer id) {
        try {
            productCateBrandAreasService.delete(id);
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商品品牌删除异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }
}
