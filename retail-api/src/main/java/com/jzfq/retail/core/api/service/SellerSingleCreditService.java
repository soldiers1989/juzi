package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.SellerSingleCredit;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:43
 * @Description: 商户单笔授信额度接口
 */
public interface SellerSingleCreditService {

    /**
     * 返回所有列表
     *
     * @param search 查询条件
     * @return
     */
    List<SellerSingleCredit> getAllList(SellerSingleCredit search);

    /**
     * 分页条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页多少
     * @param search   筛选条件
     * @return
     */
    ListResultRes<SellerSingleCredit> getList(Integer page, Integer pageSize, SellerSingleCredit search);

    /**
     * 添加
     *
     * @param entity
     */
    void create(SellerSingleCredit entity);

    /**
     * 修改
     *
     * @param entity
     */
    void update(SellerSingleCredit entity);

    /**
     * 商户单笔授信设置
     *
     * @param id
     * @param openCredit
     * @param creditValue
     * @param editor
     */
    void updateCredit(Integer id, int openCredit, Long creditValue, String editor);


    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询商户单笔授信额度
     *
     * @param id
     * @return
     */
    SellerSingleCredit getEntityById(Integer id);

    /**
     * 通过SellerID查询商户单笔授信额度
     *
     * @param sellerId
     * @return
     */
    SellerSingleCredit getEntityBySellerId(Integer sellerId);

    /**
     * 开启单笔授信
     * @param sellerId
     * @param isOpen
     * @param username
     */
    void openCredit(Integer sellerId, Integer isOpen, String username);
}
