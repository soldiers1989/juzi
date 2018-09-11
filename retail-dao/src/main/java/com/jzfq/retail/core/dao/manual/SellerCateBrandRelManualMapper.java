package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.SellerCateBrandRel;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelSearchReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:01
 * @Description:Ø
 */
public interface SellerCateBrandRelManualMapper {


    /**
     * 通过商户登录的编码获取此商户所有订单
     *
     * @return
     */
    Page<Map<String, Object>> findOptList(@Param("search") SellerCateBrandRel search);

    /**
     * 缺少注释
     *
     * @param sellerCateBrandRel
     */
    void updateStatusById(SellerCateBrandRel sellerCateBrandRel);

    /**
     * 项目：admin
     * 功能：经营品类品牌列表
     * @param req
     */
    Page<Map<String, Object>> getSellerCateBrandRelList(SellerCateBrandRelSearchReq req);

    /**
     * 查询品类品牌
     * @param id
     * @return
     */
    List<Map<String, Object>> getSellerCateBrandRelListBySellerId(@Param("sellerId") Integer sellerId);

}
