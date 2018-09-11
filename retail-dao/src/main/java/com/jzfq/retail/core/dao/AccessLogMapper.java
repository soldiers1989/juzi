package com.jzfq.retail.core.dao;

import com.jzfq.retail.bean.mapper.AccessLog;

import java.util.List;

/**
 * @author lagon
 * @time 2017/10/25 17:18
 * @description 访问日志DAO
 */
public interface AccessLogMapper extends CrudMapper<AccessLog>{

    public AccessLog findById(Long id);

    public List<AccessLog> findAllList();

    /**
     * 保存访问日志
     * @param accessLog
     */
    void saveAccessLog(AccessLog accessLog);

    /**
     * 变更访问日志
     * @param accessLog
     * @return
     */
    int updateAccessLog(AccessLog accessLog);

}
