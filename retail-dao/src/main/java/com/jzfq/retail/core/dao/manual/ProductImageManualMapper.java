package com.jzfq.retail.core.dao.manual;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author MaoLixia
 * @Date 2018/9/5 17:46
 */
public interface ProductImageManualMapper {
    List<Map<String, Object>> selectByProductId(@Param("productId") Integer productId);

    void deleteByProductId(@Param("productId") Integer productId);
}
