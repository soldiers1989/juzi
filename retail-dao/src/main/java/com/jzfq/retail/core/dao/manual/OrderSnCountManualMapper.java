package com.jzfq.retail.core.dao.manual;


import com.jzfq.retail.bean.domain.OrderSnCount;
import org.apache.ibatis.annotations.Param;

public interface OrderSnCountManualMapper {

    OrderSnCount getBySellerIdAndDateStr(@Param("sellerId") Integer sellerId, @Param("dateStr") String dateStr);

    void initCurrentVal(@Param("sellerId") Integer sellerId, @Param("dateStr") String dateStr);

    void updateCurrentValById(Integer id);


}