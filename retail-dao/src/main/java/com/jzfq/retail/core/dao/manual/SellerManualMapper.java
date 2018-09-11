package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.Seller;
import com.jzfq.retail.bean.vo.req.OptionsSearchReq;
import com.jzfq.retail.bean.vo.req.SellerSearchReq;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月27日 17:46
 * @Description:
 */
public interface SellerManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param search
     * @return
     */
    Page<Map<String, Object>> findList(SellerSearchReq search);

    /**
     * 通过商户编号查询商户
     *
     * @param code
     * @return
     */
    Seller findBySellerCode(String code);

    /**
     * 通过商户店铺编码获取商户信息
     * @param sellerLogin
     * @return
     */
    Map<String,Object> findSellerInfoBySellerLogin(String sellerLogin);

    /**
     * id,name映射
     * @return
     */
    List<Map<String, Object>> getOptions(OptionsSearchReq search);

    /**
     * 查询商户品类品牌
     * @param sellerId
     * @return
     */
    List<Map<String, Object>> getSellerCateBrand(Integer sellerId);

}
