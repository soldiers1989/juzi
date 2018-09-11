package com.jzfq.retail.core.swagger.api.manage;


import com.jzfq.retail.bean.domain.SellerLoginPermission;
import com.jzfq.retail.bean.valid.CommonValid;
import com.jzfq.retail.bean.vo.res.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerLoginPermissionService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: SellerLoginPermissionController
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 17:25
 * @Description: 商户账户对外接口
 */
@Slf4j
@RestController
@RequestMapping("/api/seller/permission")
public class SellerLoginPermissionController {


    @Autowired
    private SellerLoginPermissionService sellerLoginPermissionService;

    @Autowired
    private SessionManage sessionManage;

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    @ApiOperation(value = "获取商户账户列表")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> getList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                                      SellerLoginPermission search) {
        CommonValid.emptyStringToNull(search);
        try {
            ListResultRes<SellerLoginPermission> result = sellerLoginPermissionService.getList(page, pageSize, search);
            return TouchApiResponse.success(result,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取商户账户列表异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

    /**
     * 添加商户账户
     *
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加商户账户")
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> create(SellerLoginPermission entity) {
        try {
            sellerLoginPermissionService.create(entity,sessionManage.getUserName());
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加商户账户异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }


    /**
     * 修改商户账户信息
     *
     * @param entity
     * @return
     */
    @ApiOperation(value = "修改商户账户信息，不可以修改密码操作", notes = "接口暂时无法使用")
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> update(SellerLoginPermission entity) {
        try {
            sellerLoginPermissionService.update(entity,sessionManage.getUserName());
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改商户账户信息异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "修改商户账户密码")
    @PostMapping("/update/password")
    @ResponseBody
    public ResponseEntity<TouchResponseModel> updatePassword(@RequestParam("id") Integer id, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        try {
            sellerLoginPermissionService.updatePassword(id, oldPassword, newPassword,sessionManage.getUserName());
            return TouchApiResponse.success("操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改商户账户密码异常：{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }

}
