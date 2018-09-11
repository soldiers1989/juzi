package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.SellerRatio;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:43
 * @Description: 商户分期首付比例接口
 */
public interface SellerRatioService {

    /**
     * 返回所有列表
     * @param search 查询条件
     * @return
     */
    List<SellerRatio> getAllList(SellerRatio search);

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<SellerRatio> getList(Integer page, Integer pageSize, SellerRatio search);

    /**
     * 添加
     * @param sellerRatio
     */
    void create(SellerRatio sellerRatio);

    /**
     * 修改
     * @param sellerRatio
     */
    void update(SellerRatio sellerRatio);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询商户分期
     * @param id
     * @return
     */
    SellerRatio getEntityById(Integer id);
}
