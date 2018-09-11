package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.Seller;
import com.jzfq.retail.bean.vo.req.OptionsSearchReq;
import com.jzfq.retail.bean.vo.req.SellerSearchReq;
import com.github.pagehelper.Page;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月29日 17:43
 * @Description: 商户service接口
 */
public interface SellerService {

    /**
     * 返回所有列表
     *
     * @param search 查询条件
     * @return
     */
    List<Map<String, Object>> getAllList(SellerSearchReq search);

    /**
     * 分页条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页多少
     * @param search   筛选条件
     * @return
     */
    Page<Map<String, Object>> getList(Integer page, Integer pageSize, SellerSearchReq search);

    ListResultRes<Map<String, Object>> getList2(Integer page, Integer pageSize, SellerSearchReq search);
    /**
     * 添加
     *
     * @param seller
     */
    void create(Seller seller);

    /**
     * 修改
     *
     * @param seller
     */
    void update(Seller seller);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询商户
     *
     * @param id
     * @return
     */
    Seller getSellerById(Integer id);

    /**
     * 查询是否冻结账户
     *
     * @param id 商户id编号
     * @return true:冻结 false:没有冻结
     */
    boolean sellerIsFrozen(Integer id);

    /**
     * 修改商户冻结状态
     *
     * @param id          商户id编号
     * @param auditStatus 商户审核状态
     */
    void updateAuditStatus(Integer id, Integer auditStatus);

    /**
     * 修改商户冻结状态
     *
     * @param code          商户code编号
     * @param auditStatus 商户审核状态
     */
    void updateAuditStatus(String code, Integer auditStatus);

    /**
     * 冻结/解冻 商户
     *
     * @param sellerId
     * @param freezeType
     */
    void freeze(Integer sellerId, String freezeType);

    /**
     * id,name映射
     *
     * @return
     */
    List<Map<String, Object>> getOptions(OptionsSearchReq search);

    /**
     * 店铺装修 保存店铺上传的图片信息
     * @param sellerId
     * @param urlList
     */
    void addSellerImage(Integer sellerId, String[] urlList, String proName);

    /**
     * 查询商户详细信息
     * @param sellerId
     * @return
     */
    Map<String, Object> getSellerDetailInfo(Integer sellerId);
}
