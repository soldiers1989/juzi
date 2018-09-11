package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.Areas;
import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.vo.req.AreasSearchReq;
import com.jzfq.retail.bean.vo.req.ProductBrandSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 19:55
 * @Description:
 */
public interface AreasManualMapper {

    /**
     * 通过父类编号查询列表
     * @param parentId
     * @return
     */
    List<Areas> findAreaListByParentId(@Param("parentId") String parentId);

    /**
     * 通过地区编号获取城市
     * @param areaId
     * @return
     */
    Areas findAreaByAreaId(@Param("areaId") String areaId);

    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<Map<String, Object>> findList(AreasSearchReq search);
}
