package com.jzfq.retail.core.api.service;

import com.alibaba.fastjson.JSONArray;
import com.jzfq.retail.bean.domain.ProductWithBLOBs;
import com.jzfq.retail.bean.vo.req.ProductOnShelfReq;
import com.jzfq.retail.bean.vo.req.ProductSearchReq;
import com.jzfq.retail.bean.vo.res.ImportExcelResult;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:43
 * @Description: 商品接口
 */
public interface ProductService {

    /**
     * 返回所有列表
     *
     * @param search 查询条件
     * @return
     */
    List<Map<String, Object>> getAllList(ProductSearchReq search);

    /**
     * 分页条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页多少
     * @param search   筛选条件
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, ProductSearchReq search);

    /**
     * 添加
     *
     * @param product
     */
    void create(ProductWithBLOBs product);

    /**
     * 修改
     *
     * @param product
     */
    void update(ProductWithBLOBs product);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询商品
     *
     * @param id
     * @return
     */
    ProductWithBLOBs getProductById(Integer id);

    /**
     *  缺少注释
     * @param file
     * @return
     */
    Map<String, List<ImportExcelResult>> importExcel(MultipartFile file);
    /**
     * 通过商户id查询商品信息-小程序专用
     *
     * @param page
     * @param pageSize
     * @param sellerId
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, Integer sellerId,Integer[] states);

    /**
     * 根据商户ID查询出此商户下的商品名称
     * @param sellerId
     */
    JSONArray getProductNames(Integer sellerId);

    /**
     * 商品上架
     *
     * @return
     */
    Integer productOnShelf(ProductOnShelfReq productOnShelfReq);
}
