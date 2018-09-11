package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.Product;
import com.jzfq.retail.bean.vo.req.ProductSearchReq;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:01
 * @Description:
 */
public interface ProductManualMapper {

    /**
     * 分页查询 pagehelper 使用，查询条件比较全
     *
     * @param search
     * @return
     */
    Page<Map<String, Object>> findList(ProductSearchReq search);

    /**
     * 通过商户id查询商户产品
     *
     * @param sellerId
     * @return
     */
    Page<Map<String, Object>> findListBySellerId(@Param("sellerId") Integer sellerId, @Param("states") Integer[] states);

    /**
     * 通过sellerId和商品标识查询商品信息
     *
     * @param sellerId
     * @param identification 商品标识：0一期健身，1二期店中店商品
     * @return
     */
    List<Map<String, Object>> findListBySellerIdAndIdentification(@Param("sellerId") Integer sellerId, @Param("identification") Integer identification, @Param("state") Integer state);

    List<Map<String, Object>> selectByParam(Product product);

    /**
     * 查询商品上架时产品的信息
     * @param id
     * @return
     */
    Map<String, Object> selectOnShelfInfoById(@Param("id") Integer id);
}
