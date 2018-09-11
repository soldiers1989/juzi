package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.Dictionary;
import com.jzfq.retail.bean.vo.req.DictionaryReq;
import com.jzfq.retail.bean.vo.req.DictionarySearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月29日 14:06
 * @Description: 数据字典service接口
 */
public interface DictionaryService {

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, DictionarySearchReq search);

    /**
     * 添加
     * @param req
     */
    void create(DictionaryReq req, String username);

    /**
     * 修改
     * @param req
     */
    void update(DictionaryReq req, String username);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询字典数据
     * @param id
     * @return
     */
    Dictionary getEntityById(Integer id);
    
    Dictionary getEntityByName(String name);
    
    Dictionary getEntityByCode(String code);
}
