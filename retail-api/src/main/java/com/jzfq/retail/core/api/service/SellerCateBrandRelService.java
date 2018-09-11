package com.jzfq.retail.core.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.SellerCateBrandRel;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelReq;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelSearchReq;
import com.jzfq.retail.bean.vo.req.SellerSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月29日 17:43
 * @Description: 商户service接口
 */
public interface SellerCateBrandRelService {
    /**
     * 添加
     * @param item
     */
    void create(SellerCateBrandRelReq item, String username);

    /**
     * 修改
     * @param item
     */
    void update(SellerCateBrandRelReq req, String username);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询商户
     * @param id
     * @return
     */
    SellerCateBrandRel getEntityById(Integer id);

    void optRelation(Integer id, Integer state);

    List<Map<String, Object>> findOptList(SellerCateBrandRel search);

    /**
     * 项目：admin
     * 功能：经营品类品牌列表-分页
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, SellerCateBrandRelSearchReq search);
}
