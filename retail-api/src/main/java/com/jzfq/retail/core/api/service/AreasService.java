package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.Areas;
import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.vo.req.AreasSearchReq;
import com.jzfq.retail.bean.vo.req.ProductBrandSearchReq;
import com.jzfq.retail.bean.vo.req.ProductCateSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 19:02
 * @Description: 城市区域操作接口
 */
public interface AreasService {
    
    /**
     * 通过父类编号获取所有城市列表
     * 如果查询根目录需要传入10000值，该值为中国编号
     *
     * @param parentId 父编号
     * @return
     */
    List<Areas> getAreaByParentId(String parentId);

    List<Areas> getAreaByParentId(Areas areas);

    /**
     * 通过编号获取城市对象
     * @param areaId
     * @return
     */
    Areas getAreaByAreaId(String areaId);

    /**
     * 通过编号获取全名称
     * @param areaId
     * @return
     */
    String getMergerNameByAreaId(String areaId);

    /**
     * 通过编号获取去名称的短名称
     * @param areaId
     * @return
     */
    String getMergerShortNameByAreaId(String areaId);

    /**
     * id,name映射
     * @return
     */
    List<Map<String, Object>> getOptions(String parentId);

    /**
     * 获取列表--分页
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, AreasSearchReq search);

}
