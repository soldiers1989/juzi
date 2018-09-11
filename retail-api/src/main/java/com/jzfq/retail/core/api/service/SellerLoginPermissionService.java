package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.SellerLoginPermission;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;

/**
 * @Title: SellerLoginPermissionService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 11:17
 * @Description: 商户登录权限操作接口
 */
public interface SellerLoginPermissionService {

    /**
     * 获取全部列表 -不知支持分页
     *
     * @param search
     * @return
     */
    List<SellerLoginPermission> getAllList(SellerLoginPermission search);


    /**
     * 获取全部列表 -支持分页
     *
     * @param search
     * @return
     */
    ListResultRes<SellerLoginPermission> getList(Integer page, Integer pageSize, SellerLoginPermission search);

    /**
     * 添加
     * 只有在商户信息在风控审核之后才可以创建商户登录权限
     *
     * @param entity
     */
    void create(SellerLoginPermission entity, String createUserName);

    /**
     * 更新
     * 此操作不需要提供给BD后才，需要CRM操作系统条用
     *
     * @param entity
     */
    void update(SellerLoginPermission entity, String updateUserName);

    /**
     * 商户更新密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(Integer id, String oldPassword, String newPassword, String changeUserName);

    /**
     * 根据token校验用户openID是否存在
     *
     * @param tokenStr
     * @return Boolean
     */
    Boolean checkSellerLogin(String tokenStr);
}
