package com.jzfq.retail.core.api.service;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.SellerTerm;
import com.jzfq.retail.bean.vo.req.SellerTermSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:43
 * @Description: 商户service接口
 */
public interface SellerTermService {

    /**
     * 分页条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页多少
     * @param search   筛选条件
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, SellerTermSearchReq search);

    /**
     * 添加
     *
     * @param sellerTerm
     */
    void create(SellerTerm sellerTerm, String username);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询商户分期
     *
     * @param id
     * @return
     */
    SellerTerm getSellerTermById(Integer id);

    /**
     * 获取商户的分期列表
     *
     * @param sellerId
     * @return
     */
    List<SellerTerm> getListBySellerId(Integer sellerId);

    SellerTerm getSellerTermByParams(Integer sellerId, Integer term);
}
