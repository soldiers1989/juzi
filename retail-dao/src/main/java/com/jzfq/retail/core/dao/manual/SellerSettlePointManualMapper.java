package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.vo.req.SellerSettlePointSearchReq;

import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 14:01
 * @Description:
 */
public interface SellerSettlePointManualMapper {


    /**
     * id,name映射
     * @return
     */
    Page<Map<String, Object>> findList(SellerSettlePointSearchReq search);
}
