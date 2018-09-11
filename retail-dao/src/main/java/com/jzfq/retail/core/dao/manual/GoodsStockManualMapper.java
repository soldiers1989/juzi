package com.jzfq.retail.core.dao.manual;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsStockManualMapper {

    List<Map<String, Object>> selectByProductId(@Param("productId") Integer productId);

}