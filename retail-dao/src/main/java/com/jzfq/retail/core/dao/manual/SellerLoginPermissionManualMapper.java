package com.jzfq.retail.core.dao.manual;


import com.jzfq.retail.bean.domain.SellerLoginPermission;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * @Title: SellerLoginPermissionManualMapper
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 11:19
 * @Description: 商户登录权限持久化扩展
 */
public interface SellerLoginPermissionManualMapper {
    /**
     * 筛选查询，支持分页
     *
     * @param search
     * @return
     */
    Page<SellerLoginPermission> findList(SellerLoginPermission search);

    Long checkSellerLogin(Map<String, String> map);
}
