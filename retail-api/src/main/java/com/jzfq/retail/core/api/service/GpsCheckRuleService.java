package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.GpsCheckRule;
import com.jzfq.retail.bean.vo.req.GpsCheckRuleReq;
import com.jzfq.retail.bean.vo.req.GpsCheckRuleSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.GpsCheckFlag;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: GPS校验规则service接口
 */
public interface GpsCheckRuleService {

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, GpsCheckRuleSearchReq search);

    /**
     * 添加
     * @param req
     * @param username
     */
    void create(GpsCheckRuleReq req, String username);

    /**
     * 修改
     *
     * @param item
     */
    void update(GpsCheckRuleReq req, String username);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询字典数据
     *
     * @param id
     * @return
     */
    GpsCheckRule getEntityById(Integer id);

    /**
     * 是否校验Gps
     * @param gpsCheckFlag
     * @return
     */
    Boolean isCheckGps(GpsCheckFlag gpsCheckFlag);

    /**
     * 通过场景获取gpsCheckRule
     *
     * @param flag
     * @return
     */
    GpsCheckRule getEntityByFlag(String flag);

}
